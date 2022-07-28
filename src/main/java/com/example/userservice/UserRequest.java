package com.example.userservice;

public class UserRequest {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddressPinCode() {
        return addressPinCode;
    }

    private String email;
    private String addressPinCode;
}
