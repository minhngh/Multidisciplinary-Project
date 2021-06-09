package com.example.securitycamera.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String type;
    private String time;
    private Bitmap image;

    public UserInfo()
    {
        type = "";
        image = null;
        time = "";
    }

    public UserInfo(String type, String time, Bitmap image) {
        this.type = type;
        this.time = time;
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}




