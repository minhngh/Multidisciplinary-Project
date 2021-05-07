package com.example.securitycamera.data.remote;

import com.example.securitycamera.data.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MainApiService {
    @POST("/user/login")
    @Headers({"Accept: application/json"})
    Call<JsonObject> login(@Body User user);
}
