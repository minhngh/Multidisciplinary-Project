package com.example.securitycamera.data.remote;

import androidx.annotation.NonNull;

import com.example.securitycamera.data.model.Token;
import com.example.securitycamera.data.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApiUtils {
    private MainApiUtils(){}
    public static final String BASE_URL = "http://192.168.1.102:8000";
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
    public Call<JsonObject> login(@NonNull String username, @NonNull String password){
        return apiService.login(new User(username, password));
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
    public Call<JsonObject> turnOnCaution(String accessToken){
        return apiService.turnOnCaution(new Token(accessToken));
    }
    public Call<JsonObject>turnOffCaution(String accessToken){
        return apiService.turnOffCaution(new Token(accessToken));

    public Call<JsonArray> getImageHistory(String token)
    {
        return apiService.getImageHistory(token);
    }
}
