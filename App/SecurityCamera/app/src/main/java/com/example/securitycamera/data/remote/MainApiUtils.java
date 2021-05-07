package com.example.securitycamera.data.remote;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApiUtils {
    private MainApiUtils(){}
    public static final String BASE_URL = "https://reqres.in";
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
        return apiService.login(username, password);
    }
}