package com.example.securitycamera.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.Result;
import com.example.securitycamera.ui.main.MainActivity;
import com.example.securitycamera.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText usernameEt;
    EditText passwordEt;
    ProgressBar loadingPb;
    LoginViewModel loginViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        findViews();

        TextWatcher afterTextChangedListener = new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChange(usernameEt.getText().toString(),
                                                passwordEt.getText().toString());
            }
        };
        usernameEt.addTextChangedListener(afterTextChangedListener);
        passwordEt.addTextChangedListener(afterTextChangedListener);
        loginBtn.setOnClickListener(v -> {
           loginViewModel.login(usernameEt.getText().toString(),
                                passwordEt.getText().toString());
           loginBtn.setVisibility(View.INVISIBLE);
           loadingPb.setVisibility(View.VISIBLE);
        });

        loginViewModel.getLoginDataValid().observe(this, loginDataState -> {
           if (loginDataState == null){
               loginBtn.setEnabled(false);
               return;
           }
           if (loginDataState.getUsernameError() != null){
               usernameEt.setError(loginDataState.getUsernameError());
           }
           else if (loginDataState.getPasswordError() != null){
               passwordEt.setError(loginDataState.getPasswordError());
           }
           else{
               loginBtn.setEnabled(true);
           }
        });
        loginViewModel.getLoginResult().observe(this, result ->{
            if (result instanceof Result.Error){
                Toast.makeText(this, ((Result.Error) result).getError().getMessage(), Toast.LENGTH_SHORT).show();
                loginBtn.setVisibility(View.VISIBLE);
                loadingPb.setVisibility(View.INVISIBLE);
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void findViews() {
        loginBtn = findViewById(R.id.btn_login);
        usernameEt = findViewById(R.id.et_username);
        passwordEt = findViewById(R.id.et_password);
        loadingPb = findViewById(R.id.pb_loading);
    }
}
