package com.example.securitycamera.data.model;

public class UserInfo {
    private String type;
    private String time;
    private int imageId;

    public UserInfo()
    {
        type = "";
        imageId = -1;
        time = "";
    }

    public UserInfo(String type, String time, int imageId) {
        this.type = type;
        this.time = time;
        this.imageId = imageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

