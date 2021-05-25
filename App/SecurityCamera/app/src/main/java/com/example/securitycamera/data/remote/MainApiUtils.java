package com.example.securitycamera.data.remote;

<<<<<<< HEAD
import androidx.annotation.NonNull;

import com.example.securitycamera.data.model.User;
=======
import com.example.securitycamera.data.model.Token;
>>>>>>> feature/mqtt_services
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApiUtils {
    private MainApiUtils(){}
<<<<<<< HEAD
    public static final String BASE_URL = "http://172.17.27.204:8000";
    private static MainApiService apiService = new Retrofit.Builder()
                                            .baseUrl(BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build()
                                            .create(MainApiService.class);
=======
    public static final String BASE_URL = "http://192.168.1.7:8000";
    private static MainApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainApiService.class);
>>>>>>> feature/mqtt_services
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
<<<<<<< HEAD
    public Call<JsonObject> login(@NonNull String username, @NonNull String password){
        return apiService.login(new User(username, password));
    }
}
=======
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


>>>>>>> feature/mqtt_services
