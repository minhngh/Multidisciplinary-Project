package com.example.securitycamera.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.model.DoorState;
import com.example.securitycamera.data.model.Mode;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<DoorState> doorState = new MutableLiveData<>();
    private MutableLiveData<Mode> mode = new MutableLiveData<>();

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
}