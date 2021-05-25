package com.example.securitycamera.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.local.PreferenceManager;
import com.example.securitycamera.data.model.DoorState;
<<<<<<< HEAD
import com.example.securitycamera.data.model.Mode;

import org.jetbrains.annotations.NotNull;

public class HomeViewModel extends AndroidViewModel {
=======
import com.example.securitycamera.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
>>>>>>> feature/mqtt_services

public class HomeViewModel extends ViewModel {
    private MutableLiveData<DoorState> doorState = new MutableLiveData<>();
<<<<<<< HEAD
    private MutableLiveData<Mode> mode = new MutableLiveData<>();
    private PreferenceManager preferenceManager;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        preferenceManager = new PreferenceManager(application);
    }
=======
    private MainApiUtils mainApiService = MainApiUtils.getInstance();

    public void checkDoorState(){
        Call<JsonObject> call =  mainApiService.checkDoorState("TOKEN");
        call.enqueue(new Callback<JsonObject>(){

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    doorState.postValue(new DoorState(response.body().get("door_state").toString().contains("open"), response.body().get("time").toString().replace("\"", "")));
                }
                else{
                    doorState.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                doorState.postValue(null);
            }
        });
    }

    public void mute(){
        Call<JsonObject> call = mainApiService.mute("TOKEN");
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }
>>>>>>> feature/mqtt_services

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public void unmute(){
        Call<JsonObject> call = mainApiService.unmute("TOKEN");
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public LiveData<DoorState> getDoorState() {
        return doorState;
    }
<<<<<<< HEAD

    public void setDoorState(boolean doorState){
        this.doorState.setValue(new DoorState(doorState));
    }

    public LiveData<Mode> getMode() {
        return this.mode;
    }
    public void setMode(boolean mode){
        this.mode.setValue(new Mode(mode));
    }

    public void logout(){
        preferenceManager.deleteLogInState();
    }
=======
>>>>>>> feature/mqtt_services
}