package com.example.securitycamera.data.model;

public class Token {
    private String access_token;
    public Token(String access_token){
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }
}
