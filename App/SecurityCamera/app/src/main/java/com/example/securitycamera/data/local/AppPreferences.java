package com.example.securitycamera.data.local;

import android.app.Application;

import androidx.annotation.NonNull;

public class AppPreferences {
    public final static String PREFERENCE_NAME = "login";

    public final static String ACCESS_TOKEN_KEY = "access_token";
    public final static String LOGIN_STATE_KEY = "is_logged_in";
    public final static String USERNAME_KEY = "username";
    public final static String MODE_CHANGE_CHANNEL_ID_KEY = "mode_change_channel_id";

    private final Preferences preferences;

    public AppPreferences(@NonNull Application application) {
        preferences = new Preferences(application, PREFERENCE_NAME);
    }


    public String getAccessToken() {
        return preferences.getStringValue(ACCESS_TOKEN_KEY);
    }

    public void setAccessToken(String accessToken) {
        preferences.saveStringValue(ACCESS_TOKEN_KEY, accessToken);
    }

    public void clearAccessToken() {
        preferences.saveStringValue(ACCESS_TOKEN_KEY, "");
    }

    public boolean getLogInState() {
        return preferences.getBooleanValue(LOGIN_STATE_KEY);
    }

    public void setLogInState() {
        preferences.saveBooleanValue(LOGIN_STATE_KEY, true);
    }

    public void clearLogInState() {
        preferences.saveBooleanValue(LOGIN_STATE_KEY, false);
    }


    public String getUsername() {
        return preferences.getStringValue(USERNAME_KEY);
    }

    public void setUsername(String username) {
        preferences.saveStringValue(USERNAME_KEY, username);
    }

    public void clearUsername() {
        preferences.saveStringValue(USERNAME_KEY, "");
    }
}