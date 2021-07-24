package com.example.securitycamera.data.model;

public class Mode {
    private boolean isAlert;
    private String time;
    public Mode(){
        isAlert = false;
        time = null;
    }
    public Mode(boolean isAlert, String time) {
        this.isAlert = isAlert;
        this.time = time;
    }
    public Mode(boolean isAlert) {
        this.isAlert = isAlert;
    }
    public boolean isAlert() {
        return isAlert;
    }
}
