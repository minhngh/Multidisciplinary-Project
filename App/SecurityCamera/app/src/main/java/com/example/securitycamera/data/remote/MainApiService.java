package com.example.securitycamera.data.remote;

import com.example.securitycamera.data.model.Token;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MainApiService {
    @POST("/check-door")
    Call<JsonObject> checkDoorState(@Body Token token);
}