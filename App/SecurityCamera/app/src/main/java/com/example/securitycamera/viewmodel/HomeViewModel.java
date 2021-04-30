package com.example.securitycamera.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.model.DoorState;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<DoorState> doorState = new MutableLiveData<>();


    public LiveData<DoorState> getDoorState() {
        return doorState;
    }
    public void setDoorState(boolean doorState){
        this.doorState.setValue(new DoorState(doorState));
    }
}