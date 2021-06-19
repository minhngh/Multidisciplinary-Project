package com.example.securitycamera.data.remote;

import com.example.securitycamera.data.model.ImageInfo;
import com.example.securitycamera.data.model.Token;
import com.example.securitycamera.data.model.User;
import com.example.securitycamera.data.model.UserInfoDeletion;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MainApiService {
    @POST("/user/login")
    Call<JsonObject> login(@Body User user);
    @POST("/check-door")
    Call<JsonObject> checkDoorState(@Body Token token);
    @POST("/mute")
    Call<JsonObject> mute(@Body Token token);
    @POST("/unmute")
    Call<JsonObject> unmute(@Body Token token);
    @POST("/turn-on-caution")
    Call<JsonObject> turnOnCaution(@Body Token token);
    @POST("/turn-off-caution")
    Call<JsonObject> turnOffCaution(@Body Token token);

    @POST("/get-log")
    @Headers({"Accept: application/json"})
    Call<JsonObject> getImageHistory(@Body ImageInfo imgInfo);

    @POST("/remove-log")
    @Headers({"Accept: application/json"})
    Call<JsonObject> deleteUserInfo(@Body UserInfoDeletion userInfoDeletion);
}
