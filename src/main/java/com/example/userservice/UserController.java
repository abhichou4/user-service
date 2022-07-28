package com.example.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<User>> getUserById(@PathVariable String id) {
        return Optional.of(userRepository.findById(id)).map(user -> ResponseEntity.ok().body(user)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            String stateName = getUserStateName(userRequest.getAddressPinCode());
            User newUser = new User();
            newUser.setName(userRequest.getName());
            newUser.setEmail(validatedEmail(userRequest.getEmail()));
            newUser.setAddressPinCode(userRequest.getAddressPinCode());
            newUser.setStateName(stateName);
            User savedUser = userRepository.save(newUser);
            return ResponseEntity.ok().body(savedUser.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user email");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String getUserStateName(String addressPinCode) throws Exception {
        final String stateNameURI = String.format("https://api.postalpincode.in/pincode/%s", addressPinCode);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object[]> response = restTemplate.getForEntity(stateNameURI, Object[].class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Failed to fetch state name from given api");
        } else if (response.getBody() == null ||  response.getBody().length > 1) {
            throw new Exception("Response body has changed, handle it in api");
        }
        Object responseBody = response.getBody()[0];
        ObjectMapper mapper = new ObjectMapper();
        PinCodeResponse pinCodeResponse = mapper.convertValue(responseBody, PinCodeResponse.class);
        List<PostOffice> postOfficeList = pinCodeResponse.getPostOffices();
        return postOfficeList.size() > 1 ? null : postOfficeList.get(0).getState();
    }

    private String validatedEmail(String email) throws IllegalArgumentException {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("Invalid user e-mail");
        }

        return email;
    }
}
