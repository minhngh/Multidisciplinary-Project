package com.example.securitycamera.data.remote;

import com.example.securitycamera.data.model.Token;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApiUtils {
    private MainApiUtils(){}
    public static final String BASE_URL = "http://192.168.1.7:8000";
    private static MainApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainApiService.class);
    private static MainApiUtils instance = null;
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
    public Call<JsonObject> checkDoorState(String accessToken){
        return apiService.checkDoorState(new Token(accessToken));
    }
    public Call<JsonObject>mute(String accessToken){
        return apiService.mute(new Token(accessToken));
    }

    public Call<JsonObject>unmute(String accessToken){
        return apiService.unmute(new Token(accessToken));
    }
}


