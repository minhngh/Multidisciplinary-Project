package com.example.securitycamera.data.model.schedule;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;


public class Schedule implements Serializable {
    @NonNull
    private String time;

    @NonNull
    private String description;

    @NonNull
    private boolean[] isActives;

    private boolean toCautiousMode;

    private boolean isEnabled;


    public Schedule(@NonNull String time, @NonNull String description, @NonNull boolean[] isActives, boolean toCautiousMode, boolean isEnabled) {
        if (isActives.length != 7)
            throw new IllegalArgumentException();

        this.time = time;
        this.description = description;
        this.isActives = isActives;
        this.toCautiousMode = toCautiousMode;
        this.isEnabled = isEnabled;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public boolean[] getIsActives() {
        return isActives;
    }

    public void setIsActives(@NonNull boolean[] isActives) {
        if (isActives.length != 7)
            throw new IllegalArgumentException();

        this.isActives = isActives;
    }

    public boolean isToCautiousMode() {
        return toCautiousMode;
    }

    public void setToCautiousMode(boolean toCautiousMode) {
        this.toCautiousMode = toCautiousMode;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
