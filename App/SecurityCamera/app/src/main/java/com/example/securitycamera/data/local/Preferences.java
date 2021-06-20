package com.example.securitycamera.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Facade SharedPreferences for several simple use cases.
 */
public class Preferences {
    private final SharedPreferences sharedPreferences;

    public Preferences(@NonNull Application application, @NonNull String name){
        sharedPreferences = application.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void saveBooleanValue(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public void saveStringValue(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getStringValue(String key){
        return sharedPreferences.getString(key, "");
    }
}