package com.example.securitycamera.viewmodel;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private boolean mute = true;

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
