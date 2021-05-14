package com.example.securitycamera.data.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class ScheduleContainer {
    public class Schedule {
        private Date mTime;
        private String mDescription;
        private boolean mActiveness;
        private boolean[] mActivenessOnDay = new boolean[7];

        Schedule(
                @NotNull Date time,
                @NotNull String description,
                boolean activeness,
                @NotNull boolean[] activenessOnDay)
        {
            mTime = time;
            mDescription = description;
            mActiveness = activeness;

            for(int i = 0; i < 7; ++i) {
                mActivenessOnDay = activenessOnDay;
            }
        }

        public Date getTime() {
            return mTime;
        }

        public void setTime(Date time) {
            mTime = time;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public boolean getActiveness() {
            return mActiveness;
        }

        public void setActiveness(boolean activeness) {
            mActiveness = activeness;
        }

        public boolean getActivenessOnDayOfWeek(int day) {
            return mActivenessOnDay[day];
        }

        public void setActivenessOnDayOfWeek(int day, boolean activeness) {
            mActivenessOnDay[day] = activeness;
        }
    }

    private ArrayList<Schedule> mSchedules = new ArrayList<>();

    ScheduleContainer(@NotNull ArrayList<Schedule> container) {
        mSchedules = container;
    }

    public void push(Schedule schedule) {
        mSchedules.add(schedule);
    }

    public void pop() {
        mSchedules.remove(mSchedules.size()-1);
    }

    public int size() { return mSchedules.size(); }
    public Schedule get(int index) { return mSchedules.get(index); }
}