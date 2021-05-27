
package com.example.securitycamera.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.local.PreferenceManager;
import com.example.securitycamera.data.model.DoorState;
import com.example.securitycamera.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<DoorState> doorState = new MutableLiveData<>();
    private MainApiUtils mainApiService = MainApiUtils.getInstance();
    private PreferenceManager preferenceManager;
    private MutableLiveData<Boolean> isAlertMode = new MutableLiveData<>();

    public HomeViewModel(@NonNull @NotNull Application application) {
        super(application);
        preferenceManager = new PreferenceManager(application);
        isAlertMode.setValue(true);
    }

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
    public void turnOnCaution(){
        Call<JsonObject> call = mainApiService.turnOnCaution("TOKEN");
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    isAlertMode.setValue(true);
                }
                else{
                    isAlertMode.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isAlertMode.setValue(false);
            }
        });
    }
    public void turnOffCaution(){
        Call<JsonObject> call = mainApiService.turnOffCaution("TOKEN");
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    isAlertMode.setValue(false);
                }
                else{
                    isAlertMode.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isAlertMode.setValue(true);
            }
        });
    }

    public void switchMode(){
        if (isAlertMode.getValue()){
            turnOffCaution();
        }
        else{
            turnOnCaution();
        }
    }

    public LiveData<Boolean> getIsAlertMode() {
        return isAlertMode;
    }

    public LiveData<DoorState> getDoorState() {
        return doorState;
    }
    public void logout(){
        preferenceManager.deleteLogInState();
    }
}