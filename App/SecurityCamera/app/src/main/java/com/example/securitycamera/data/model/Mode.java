package com.example.securitycamera.data.model;

public class Mode {
    private boolean isAlert;
    public Mode(){
        isAlert = false;
    }
    public Mode(boolean isAlert) {
        this.isAlert = isAlert;
    }
    public boolean isAlert() {
        return isAlert;
    }
}
