package com.example.securitycamera.data.model.schedule.change;

import androidx.annotation.NonNull;

import com.example.securitycamera.data.local.ScheduleManager;

import java.io.Serializable;


public class ScheduleChangeLog implements Serializable {
    private final int version;

    @NonNull
    private final ScheduleChange[] scheduleChanges;

    public ScheduleChangeLog(int version, @NonNull ScheduleChange[] scheduleChanges) {
        this.version = version;
        this.scheduleChanges = scheduleChanges;
    }

    public boolean apply(@NonNull ScheduleManager mgr) {
        boolean res = false;
        for (ScheduleChange scheduleChange : scheduleChanges) {
            res |= scheduleChange.apply(mgr);
        }

        return res;
    }

    public int getVersion() {
        return version;
    }

    @NonNull
    public ScheduleChange[] getScheduleChanges() {
        return scheduleChanges;
    }
}
