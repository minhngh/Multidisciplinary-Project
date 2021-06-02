package com.example.securitycamera.ui.history;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.UserInfo;

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

        UserInfo userInfo = (UserInfo) intent.getSerializableExtra("user_info");
        if(userInfo != null)
        {
            imgUser.setImageResource(userInfo.getImageId());
            txtUserTime.setText("Time: \t" + userInfo.getTime());
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