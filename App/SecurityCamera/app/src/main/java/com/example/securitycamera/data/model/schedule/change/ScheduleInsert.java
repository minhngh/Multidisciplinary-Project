package com.example.securitycamera.data.model.schedule.change;

import androidx.annotation.NonNull;

import com.example.securitycamera.data.model.schedule.Schedule;
import com.example.securitycamera.data.local.ScheduleManager;

import java.io.Serializable;

class ScheduleInsert implements ScheduleChange, Serializable {
    @NonNull
    private final Schedule schedule;

    public ScheduleInsert(@NonNull Schedule schedule) {
        this.schedule = schedule;
    }

    @NonNull
    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public boolean apply(@NonNull ScheduleManager mgr) {
        return mgr.insertSchedule(schedule);
    }
}
