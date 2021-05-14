package com.example.securitycamera.ui.slashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.securitycamera.ui.login.LoginActivity;
import com.example.securitycamera.ui.main.MainActivity;
import com.example.securitycamera.utils.NetworkStatus;
import com.example.securitycamera.viewmodel.LoginViewModel;

public class SlashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!NetworkStatus.isNetworkConnected(this)){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Lỗi")
                    .setMessage("Không thể kết nối với internet")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog1, which) ->  finish()).create();
            dialog.show();
            return;
        }

        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.checkLoggedin();
        loginViewModel.isLoggedin().observe(this, isLoggedin -> {
            Handler handler = new Handler();
            Intent intent;

            if (isLoggedin == null || !isLoggedin){
                intent = new Intent(this, LoginActivity.class);
            }
            else{
                intent = new Intent(this, MainActivity.class);
            }
            handler.post(()->{
                startActivity(intent);
                finish();
            });
        });
    }
}
