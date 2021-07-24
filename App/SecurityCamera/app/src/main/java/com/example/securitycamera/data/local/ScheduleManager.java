package com.example.securitycamera.data.local;

import androidx.annotation.NonNull;

import com.example.securitycamera.data.model.schedule.Schedule;
import com.example.securitycamera.data.model.schedule.change.ScheduleChange;
import com.example.securitycamera.data.model.schedule.change.ScheduleChangeLog;

import java.util.ArrayList;
import java.util.Arrays;


public class ScheduleManager {
    private static ScheduleManager instance = null;

    private int version;

    @NonNull
    private ArrayList<Schedule> schedules;

    private ScheduleManager() {
        version = -1;
        schedules = new ArrayList<>();
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @NonNull
    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(@NonNull ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public boolean insertSchedule(@NonNull Schedule schedule) {
        int index = Arrays.binarySearch(this.schedules.toArray(), schedule);

        if (index >= 0)
            return false;

        index = -(index + 1);

        this.schedules.add(index, schedule);
        return true;
    }

    public boolean removeSchedule(@NonNull Schedule schedule) {
        return this.schedules.remove(schedule);
    }


    public boolean applyChange(@NonNull ScheduleChangeLog changeLog) {
        return changeLog.apply(this);
    }

    public boolean applyChange(@NonNull ScheduleChange change) {
        return change.apply(this);
    }

    @NonNull
    public static ScheduleManager getInstance(){
        if (instance == null) {
            synchronized (ScheduleManager.class){
                if (instance == null){
                    instance = new ScheduleManager();
                }
            }
        }
        return instance;
    }
}
