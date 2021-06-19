package com.example.securitycamera.viewmodel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securitycamera.data.model.DoorState;
import com.example.securitycamera.data.model.Result;
import com.example.securitycamera.data.model.User;
import com.example.securitycamera.data.model.UserInfo;
import com.example.securitycamera.data.model.UserInfoConvert;
import com.example.securitycamera.data.remote.MainApiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryViewModel extends ViewModel {
    private MainApiUtils mainApiUtils = MainApiUtils.getInstance();
    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<UserInfoConvert>> listUserInfos = new MutableLiveData<>();
    private MutableLiveData<String> deleteResponse = new MutableLiveData<>();

    public HistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is history fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getImageHistory(String access_token, String start_time, String end_time)
    {
        Call<JsonObject> call = mainApiUtils.getImageHistory(access_token, start_time, end_time);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    JsonObject jAObject = (JsonObject) response.body();
                    String info = jAObject.get("log").toString();
                    info = info.substring(1, info.length() - 1);
                    Gson gson = new Gson();
                    UserInfoConvert[] userInfos = gson.fromJson(info, UserInfoConvert[].class);

                    ArrayList<UserInfoConvert> temp = new ArrayList<>();
                    Log.d("data", info);
                    for(int i = 0; i< userInfos.length; i++)
                    {
                        temp.add(userInfos[i]);
                    }
                    listUserInfos.setValue(temp);

                }
                else{
                    Log.d("errorImage", "Không nhận được ảnh!");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("errorImage", "Không nhận được ảnh!");
            }

        });

    }

    public MutableLiveData<ArrayList<UserInfoConvert>> getListUserInfos() {
        return listUserInfos;
    }

    public MutableLiveData<String> getDeleteResponse() {
        return deleteResponse;
    }

    public void deleteUserInfo(String access_token, String id)
    {
        Call<JsonObject> call = mainApiUtils.deleteUserInfo(access_token, id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    JsonObject jAObject = (JsonObject) response.body();
                    String info = jAObject.get("remove").toString();
                    deleteResponse.setValue(info);
                }
                else{
                    Log.d("errorDelete", "Không xóa được thông tin!");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("errorImage", "Xóa thất bại!");
            }

        });
    }
}