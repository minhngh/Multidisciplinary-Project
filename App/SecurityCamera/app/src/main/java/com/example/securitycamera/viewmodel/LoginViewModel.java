package com.example.securitycamera.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.model.LoginDataState;

public class LoginViewModel extends ViewModel {
    public final String USERNAME_ERROR_MSG = "Tên đăng nhập không hợp lệ";
    public final String PASSWORD_ERROR_MSG = "Mật khẩu không hợp lệ";
    private MutableLiveData<LoginDataState> loginDataState = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    public void loginDataChange(String username, String password){
        if (!isUsernameValid(username)){
            loginDataState.setValue(new LoginDataState(USERNAME_ERROR_MSG, null));
        }
        else if (!isPasswordValid(password)){
            loginDataState.setValue(new LoginDataState(null, PASSWORD_ERROR_MSG));
        }
        else{
            loginDataState.setValue(new LoginDataState(true));
        }
    }

    public void login(String username, String password){
        if (username.equals("admin") && password.equals("admin")){
            loginResult.setValue(true);
        }
        else{
            loginResult.setValue(false);
        }
    }

    public LiveData<LoginDataState> getLoginDataValid() {
        return loginDataState;
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    private boolean isUsernameValid(String username){
        return username != null && username.length() > 4;
    }
    private boolean isPasswordValid(String password){
        return password != null && password.length() > 4;
    }
}
