package com.example.securitycamera.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.securitycamera.data.local.PreferenceManager;
import com.example.securitycamera.data.model.LoginDataState;
import com.example.securitycamera.data.model.Result;
import com.example.securitycamera.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    public final String USERNAME_ERROR_MSG = "Tên đăng nhập không hợp lệ";
    public final String PASSWORD_ERROR_MSG = "Mật khẩu không hợp lệ";
    private MutableLiveData<LoginDataState> loginDataState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> loginResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedin = new MutableLiveData<>();
    private PreferenceManager preferenceManager;
    private MainApiUtils mainApiUtils = MainApiUtils.getInstance();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        preferenceManager = new PreferenceManager(application);
    }

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
        Call<JsonObject> call = mainApiUtils.login(username, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    loginResult.postValue(new Result.Success<>(true));
                    preferenceManager.saveLogInState();
                }
                else{
                    loginResult.postValue(new Result.Error(new Exception(response.body() == null ? "Đã có lỗi. Vui lòng thử lại!" : response.body().get("access_token").toString())));
                }
//                Log.d("LOGIN", response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loginResult.postValue(new Result.Error(new Exception(t.getMessage())));
            }
        });
    }

    public LiveData<LoginDataState> getLoginDataValid() {
        return loginDataState;
    }

    public LiveData<Result<Boolean>> getLoginResult() {
        return loginResult;
    }

    public void checkLoggedin(){
        isLoggedin.setValue(preferenceManager.getLogInState());
    }
    public LiveData<Boolean> isLoggedin() {
        return isLoggedin;
    }

    private boolean isUsernameValid(String username){
        return username != null && username.length() > 4;
    }
    private boolean isPasswordValid(String password){
        return password != null && password.length() > 4;
    }
}
