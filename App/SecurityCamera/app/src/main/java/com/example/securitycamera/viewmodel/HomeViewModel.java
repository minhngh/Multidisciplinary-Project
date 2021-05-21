package com.example.securitycamera.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.model.DoorState;
import com.example.securitycamera.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<DoorState> doorState = new MutableLiveData<>();
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
    }
    public void unmute(){
        Call<JsonObject>call = mainApiService.unmute("TOKEN");
    }
    public LiveData<DoorState> getDoorState() {
        return doorState;
    }
}