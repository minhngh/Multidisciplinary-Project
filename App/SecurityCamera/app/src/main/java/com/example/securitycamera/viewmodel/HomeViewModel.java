package com.example.securitycamera.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.local.PreferenceManager;
import com.example.securitycamera.data.model.DoorState;
import com.example.securitycamera.data.model.Mode;

import org.jetbrains.annotations.NotNull;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<DoorState> doorState = new MutableLiveData<>();
    private MutableLiveData<Mode> mode = new MutableLiveData<>();
    private PreferenceManager preferenceManager;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        preferenceManager = new PreferenceManager(application);
    }

    public LiveData<DoorState> getDoorState() {
        return doorState;
    }

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
}