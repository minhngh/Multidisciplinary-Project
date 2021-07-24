package com.example.securitycamera.data.model;

public class ImageInfo {
    private String access_token;
    private String start_time;
    private String end_time;

    public ImageInfo(String access_token, String start_time, String end_time) {
        this.access_token = access_token;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
