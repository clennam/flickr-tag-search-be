package com.example.appengine.springboot;

import org.springframework.http.HttpStatus;

public class PasswordResponse {

    public PasswordResponse(){
    }

    private HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
