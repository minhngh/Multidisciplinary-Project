package com.example.securitycamera.data.local;

import android.app.Application;

import androidx.annotation.NonNull;

public class PreferenceManager {
    public final static String PREFERENCE_NAME = "login";
    public final static String LOGGED_IN_STATE_KEY = "is_logged_in";
    public final static String SAVED_USERNAME_KEY = "username";

    private final Preferences preferences;

    public PreferenceManager(@NonNull Application application) {
        preferences = new Preferences(application, PREFERENCE_NAME);
    }

    public void saveLogInState() {
        preferences.saveBooleanValue(LOGGED_IN_STATE_KEY, true);
    }

    public void deleteLogInState() {
        preferences.saveBooleanValue(LOGGED_IN_STATE_KEY, false);
    }

    public void setUsername(String username) {
        preferences.saveStringValue(SAVED_USERNAME_KEY, username);
    }

    public void clearUsername() {
        preferences.saveStringValue(SAVED_USERNAME_KEY, "");
    }

    public boolean getLogInState() {
        return preferences.getBooleanValue(LOGGED_IN_STATE_KEY);
    }

    public String getUsername() {
        return preferences.getStringValue(SAVED_USERNAME_KEY);
    }
}