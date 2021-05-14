package com.example.securitycamera.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.model.ScheduleContainer;

public class ScheduleViewModel extends ViewModel {

    private MutableLiveData<ScheduleContainer> mScheduleContainer;

    public ScheduleViewModel() {
        mScheduleContainer = new MutableLiveData<>();
    }

    public LiveData<ScheduleContainer> getContainer() {
        return mScheduleContainer;
    }
}