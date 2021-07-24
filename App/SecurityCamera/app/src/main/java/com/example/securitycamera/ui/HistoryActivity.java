package com.example.securitycamera.ui;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.UserInfo;
import com.example.securitycamera.data.model.UserInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewHistory;
    TextView textViewDateTime;
    ArrayList<UserInfo> userInfoList;
    UserInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);
        //Mapping();

        //adapter = new UserInfoAdapter(this, R.layout.user_info_item, userInfoList);
       // listViewHistory.setAdapter(adapter);


//        ArrayList<String> arrayList = new ArrayList<String>();
//        arrayList.add("Android");
//        arrayList.add("PHP");
//        arrayList.add("iOs");
//
//        ArrayAdapter adapter =
//                new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
//        listViewHistory.setAdapter(adapter);
    }

//    private void Mapping()
//    {
//        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime);
//        listViewHistory = (ListView) findViewById(R.id.listViewHistory);
//
//        userInfoList = new ArrayList<>();
//        userInfoList.add(new UserInfo("Guest", "13:50", R.drawable.nam2));
//        userInfoList.add(new UserInfo("Guest", "9:12", R.drawable.nu1));
//    }
}