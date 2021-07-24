package com.example.securitycamera.data.model.schedule.change;

import androidx.annotation.NonNull;

import com.example.securitycamera.data.model.schedule.Schedule;
import com.example.securitycamera.data.local.ScheduleManager;

import java.io.Serializable;
import java.util.ArrayList;

class ScheduleDeleteAll implements ScheduleChange, Serializable {
    public ScheduleDeleteAll() {}

    @Override
    public boolean apply(@NonNull ScheduleManager mgr) {
        mgr.setSchedules(new ArrayList<Schedule>());
        return true;
    }
}
