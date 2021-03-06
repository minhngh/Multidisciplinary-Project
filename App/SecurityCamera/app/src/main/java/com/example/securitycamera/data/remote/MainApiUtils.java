package com.example.securitycamera.data.remote;

import androidx.annotation.NonNull;

import com.example.securitycamera.data.model.ImageInfo;
import com.example.securitycamera.data.model.Token;
import com.example.securitycamera.data.model.User;
import com.example.securitycamera.data.model.UserInfoDeletion;
import com.example.securitycamera.data.model.schedule.Schedule;
import com.example.securitycamera.data.model.schedule.change.ScheduleChangeLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApiUtils {
    private static MainApiUtils instance = null;

    public static final String BASE_URL = "http://192.168.10.117:8000";
    public static final Gson gson = new GsonBuilder()
            .setDateFormat("HH:mm:SS.sss")
            .create();
    private static final MainApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MainApiService.class);


    private MainApiUtils(){}

    public static MainApiUtils getInstance(){
        if (instance == null){
            synchronized (MainApiUtils.class){
                if (instance == null){
                    instance = new MainApiUtils();
                }
            }
        }
        return instance;
    }


    public Call<JsonObject> login(@NonNull String username, @NonNull String password){
        return apiService.login(new User(username, password));
    }
    public Call<JsonObject> checkDoorState(String accessToken){
        return apiService.checkDoorState(new Token(accessToken));
    }
    public Call<JsonObject> checkMode(String accessToken){
        return apiService.checkMode(new Token(accessToken));
    }
    public Call<JsonObject>mute(String accessToken){
        return apiService.mute(new Token(accessToken));
    }

    public Call<JsonObject>unmute(String accessToken){
        return apiService.unmute(new Token(accessToken));
    }
    public Call<JsonObject> turnOnCaution(String accessToken){
        return apiService.turnOnCaution(new Token(accessToken));
    }
    public Call<JsonObject>turnOffCaution(String accessToken) {
        return apiService.turnOffCaution(new Token(accessToken));
    }

    public Call<JsonObject> getImageHistory(String access_token, String start_time, String end_time)
    {
        return apiService.getImageHistory(new ImageInfo(access_token, start_time, end_time));
    }

    public Call<JsonObject> deleteUserInfo(String access_token, String id)
    {
        return apiService.deleteUserInfo(new UserInfoDeletion(access_token, id));
    }

    public Call<Schedule[]> getScheduleChange(@NonNull String token, int version) {
        return apiService.getScheduleChange(token, version);
    }

    public Call<Integer> changeSchedule(@NonNull String token, @NonNull ScheduleChangeLog changeLog) {
        return apiService.changeSchedule(token, changeLog);
    }
}
