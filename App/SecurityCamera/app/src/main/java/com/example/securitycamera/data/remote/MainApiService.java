package com.example.securitycamera.data.remote;

<<<<<<< HEAD
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
=======
import com.example.securitycamera.data.model.Token;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MainApiService {
    @POST("/check-door")
    Call<JsonObject> checkDoorState(@Body Token token);

    @POST("/mute")
    Call<JsonObject> mute(@Body Token token);

    @POST("/unmute")
    Call<JsonObject> unmute(@Body Token token);
}
>>>>>>> feature/mqtt_services
