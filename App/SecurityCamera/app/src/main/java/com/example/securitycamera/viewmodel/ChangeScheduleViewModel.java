package com.example.securitycamera.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.securitycamera.data.local.AppPreferences;
import com.example.securitycamera.data.local.ScheduleManager;
import com.example.securitycamera.data.model.Result;
import com.example.securitycamera.data.model.schedule.change.ScheduleChangeLog;
import com.example.securitycamera.data.remote.MainApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ChangeScheduleViewModel extends AndroidViewModel {
    private final MutableLiveData<Result<ScheduleChangeLog>> result = new MutableLiveData<>();
    private final AppPreferences appPreferences;
    private final MainApiUtils mainApiUtils = MainApiUtils.getInstance();
    private final ScheduleManager scheduleManager = ScheduleManager.getInstance();

    public ChangeScheduleViewModel(@NonNull Application application) {
        super(application);
        appPreferences = new AppPreferences(application);
    }

    public void checkSchedules(){

    }

    public LiveData<Result<ScheduleChangeLog>> getResult() {
        return result;
    }
}
