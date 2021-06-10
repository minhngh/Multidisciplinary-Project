package com.example.securitycamera.ui.history;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.UserInfo;
import com.example.securitycamera.data.model.UserInfoAdapter;
import com.example.securitycamera.data.model.UserInfoConvert;
import com.example.securitycamera.ui.main.MainActivity;
import com.example.securitycamera.viewmodel.HistoryViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    ListView listViewHistory;
    TextView textViewDateTime;
    ArrayList<UserInfo> userInfoList;
    UserInfoAdapter adapter;
    ArrayList<UserInfoConvert> listUserConvert = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        historyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        findViews(root);

        // Set TextView DateTime
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        textViewDateTime.setText(date);


        userInfoList = new ArrayList<>();
        String token = "RANDOM_STRING";
        String start_time = "02/06/2021";
        String end_time = "02/06/2021";
        historyViewModel.getImageHistory(token, start_time, end_time);

        historyViewModel.getListUserInfos().observe(getViewLifecycleOwner(), listConvert ->
        {
            userInfoList = UserInfoConvertToUserInfo(listConvert);
            listUserConvert = listConvert;

            adapter = new UserInfoAdapter(requireContext(), R.layout.user_info_item, userInfoList);

            listViewHistory.setAdapter(adapter);

            listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), UserInfoHistoryActivity.class);
                    intent.putExtra("user_info", (Serializable) listUserConvert.get(position));
                    startActivity(intent);
                }
            });
        });
//        userInfoList.add(new UserInfo("Guest", "13:50", decodedByte));
//        userInfoList.add(new UserInfo("Guest", "9:12", decodedByte));







        return root;
    }

    private void findViews(View root)
    {
        textViewDateTime = (TextView) root.findViewById(R.id.textViewDateTime);
        listViewHistory = (ListView) root.findViewById(R.id.listViewHistory);
    }




    private ArrayList<UserInfo> UserInfoConvertToUserInfo(ArrayList<UserInfoConvert> listUserConvert)
    {
        ArrayList<UserInfo> userInfoList = new ArrayList<>();

        for(int i = 0; i < listUserConvert.size(); i++)
        {
            byte[] decodedString = Base64.decode(listUserConvert.get(i).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            userInfoList.add(new UserInfo(listUserConvert.get(i).getType(), listUserConvert.get(i).getTime(), decodedByte));
        }

        return userInfoList;
    }

//    private void Test(){
//        AssetManager assetManager = getContext().getAssets();
//
//        InputStream input;
//        try {
//            input = assetManager.open("data.json");
//
//            int size = input.available();
//            byte[] buffer = new byte[size];
//            input.read(buffer);
//            input.close();
//
//            // byte buffer into a string
//            String text = new String(buffer);
//            Gson gson = new Gson();
//
//
//            UserInfoConvert[] userInfos = gson.fromJson(text, UserInfoConvert[].class);
//
//            String data = "";
//            for(int i = 0; i< userInfos.length; i++)
//            {
//                listDemo.add(userInfos[i]);
//            }
//
//
//
//
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}


