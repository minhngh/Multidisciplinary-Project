package com.example.securitycamera.data.model.schedule.change;

import androidx.annotation.NonNull;

import com.example.securitycamera.data.local.ScheduleManager;

import java.io.Serializable;

public interface ScheduleChange extends Serializable {
    boolean apply(@NonNull ScheduleManager mgr);
}
