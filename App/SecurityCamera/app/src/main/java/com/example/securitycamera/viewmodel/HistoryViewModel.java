package com.example.securitycamera.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.model.Result;
import com.example.securitycamera.data.model.User;
import com.example.securitycamera.data.model.UserInfo;
import com.example.securitycamera.data.remote.MainApiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryViewModel extends ViewModel {
    private MainApiUtils mainApiUtils = MainApiUtils.getInstance();
    private MutableLiveData<String> mText;

    public HistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is history fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public ArrayList<UserInfo> getImageHistory(String token)
    {
        Call<JsonArray> call = mainApiUtils.getImageHistory(token);
        ArrayList<UserInfo> listUserInfo = new ArrayList<UserInfo>();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200){
                    JsonArray jArray = (JsonArray) response.body();
                    Gson gson = new Gson();
                    UserInfo[] userInfos = gson.fromJson(jArray, UserInfo[].class);

                    for(int i = 0; i< userInfos.length; i++)
                    {
                        listUserInfo.add(userInfos[i]);
                    }
                }
                else{
                    Log.d("errorImage", "Không nhận được ảnh!");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("errorImage", "Không nhận được ảnh!");
            }
        });

        return listUserInfo;
    }
}