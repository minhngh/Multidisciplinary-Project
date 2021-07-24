package com.example.securitycamera.data.model;

public class UserInfoDeletion {
    private String access_token;
    private String id;

    public UserInfoDeletion(){
        this.access_token = "";
        this.id = "";
    }

    public UserInfoDeletion(String access_token, String id) {
        this.access_token = access_token;
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
