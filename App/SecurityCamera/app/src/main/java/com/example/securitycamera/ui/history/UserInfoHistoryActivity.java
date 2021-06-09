package com.example.securitycamera.ui.history;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.UserInfo;
import com.example.securitycamera.data.model.UserInfoConvert;

public class UserInfoHistoryActivity extends AppCompatActivity {

    ImageView imgUser;
    TextView txtUserType;
    TextView txtUserTime;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_history);
        findViews();
        Intent intent = getIntent();

        UserInfoConvert userInfo = (UserInfoConvert) intent.getSerializableExtra("user_info");
        if(userInfo != null)
        {
            byte[] decodedString = Base64.decode(userInfo.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgUser.setImageBitmap(decodedByte);
            String time = userInfo.getTime();
            String[] time_infos = time.split(",");
            String time_display = time_infos[0] + "\n            " + time_infos[1];

            txtUserTime.setText("Time: \t" + time_display);
            txtUserType.setText("Type: \t" + userInfo.getType());
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews()
    {
        imgUser = findViewById(R.id.imageViewUser);
        txtUserType = findViewById(R.id.txtUserType);
        txtUserTime = findViewById(R.id.txtUserTime);
        btnBack = findViewById(R.id.btnBack);
    }
}