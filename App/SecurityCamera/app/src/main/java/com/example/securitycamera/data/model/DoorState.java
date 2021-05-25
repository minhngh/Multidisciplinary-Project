package com.example.securitycamera.data.model;

public class DoorState {
    private boolean isOpen;
<<<<<<< HEAD
    private boolean isAlert;
=======
    private String time;
>>>>>>> feature/mqtt_services
    public DoorState(){
        isOpen = false;
        time = null;
    }
    public DoorState(boolean isOpen, String time) {
        this.isOpen = isOpen;
        this.time = time;
    }
    public boolean isOpen() {
        return isOpen;
    }

    public String getTime() {
        return time;
    }
}
