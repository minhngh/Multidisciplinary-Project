package com.example.securitycamera.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.securitycamera.data.local.AppPreferences;
import com.example.securitycamera.data.model.Result;
import com.example.securitycamera.data.local.ScheduleManager;
import com.example.securitycamera.data.model.schedule.Schedule;
import com.example.securitycamera.data.model.schedule.change.ScheduleChangeLog;
import com.example.securitycamera.data.remote.MainApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class GetScheduleChangeViewModel extends AndroidViewModel {
    private final MutableLiveData<Result<Schedule[]>> result = new MutableLiveData<>();
    private final AppPreferences appPreferences;
    private final MainApiUtils mainApiUtils = MainApiUtils.getInstance();
    private final ScheduleManager scheduleManager = ScheduleManager.getInstance();

    public GetScheduleChangeViewModel(@NonNull Application application) {
        super(application);
        appPreferences = new AppPreferences(application);
    }

    public void checkSchedules(){
        Call<Schedule[]> call = mainApiUtils.getScheduleChange(
                appPreferences.getAccessToken(),
                scheduleManager.getVersion());

        call.enqueue(new Callback<Schedule[]>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Schedule[]> call, Response<Schedule[]> response) {
                if (response.code() == 200 && response.body() != null) {
                    result.postValue(new Result.Success<>(response.body()));
                }
                else if (response.code() == 403) {
                    result.postValue(
                            new Result.Error<>(
                                    new Exception("Token might have expired.")));
                }
                else {
                    result.postValue(
                            new Result.Error<>(
                                    new Exception("Error " + response.code() + " encountered.")));
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Schedule[]> call, Throwable t) {
                result.postValue(new Result.Error<>(new Exception(t.getMessage())));
            }
        });
    }

    public LiveData<Result<Schedule[]>> getResult() {
        return result;
    }
}
