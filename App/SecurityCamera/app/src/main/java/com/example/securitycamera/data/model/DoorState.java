package com.example.securitycamera.data.model;

public class DoorState {
    private boolean isOpen;
    public DoorState(){
        isOpen = false;
    }
    public DoorState(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
