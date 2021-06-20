package com.example.securitycamera.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserInfoConvert implements Serializable {
    private int id;
    private String type;
    private String time;
    private String image;

    public UserInfoConvert()
    {
        id = -1;
        type = "";
        image = "";
        time = "";
    }

    public UserInfoConvert(int id, String type, String time, String image) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
