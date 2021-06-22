package com.example.securitycamera.data.remote;

import com.example.securitycamera.data.model.ImageInfo;
import com.example.securitycamera.data.model.Token;
import com.example.securitycamera.data.model.User;

import com.example.securitycamera.data.model.schedule.Schedule;
import com.example.securitycamera.data.model.schedule.change.ScheduleChangeLog;

import com.example.securitycamera.data.model.UserInfoDeletion;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MainApiService {
    @POST("/user/login")
    Call<JsonObject> login(@Body User user);
    @POST("/check-door")
    Call<JsonObject> checkDoorState(@Body Token token);
    @POST("/check-mode")
    Call<JsonObject> checkMode(@Body Token token);
    @POST("/mute")
    Call<JsonObject> mute(@Body Token token);
    @POST("/unmute")
    Call<JsonObject> unmute(@Body Token token);
    @POST("/turn-on-caution")
    Call<JsonObject> turnOnCaution(@Body Token token);
    @POST("/turn-off-caution")
    Call<JsonObject> turnOffCaution(@Body Token token);

    @POST("/get-schedule-change")
    Call<Schedule[]> getScheduleChange(@Header("Authorization") String token, @Body int version);

    @POST("/change-schedule")
    Call<Integer> changeSchedule(@Header("Authorization") String token, @Body ScheduleChangeLog changeLog);


    @POST("/get-log")
    @Headers({"Accept: application/json"})
    Call<JsonObject> getImageHistory(@Body ImageInfo imgInfo);

    @POST("/remove-log")
    @Headers({"Accept: application/json"})
    Call<JsonObject> deleteUserInfo(@Body UserInfoDeletion userInfoDeletion);
}
