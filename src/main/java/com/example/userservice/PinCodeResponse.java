package com.example.userservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PinCodeResponse {
    @JsonProperty("Message")
    public String message;
    @JsonProperty("Status")
    public String status;
    @JsonProperty("PostOffice")
    public List<PostOffice> postOffices;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public List<PostOffice> getPostOffices() {
        return postOffices;
    }
}
