package com.example.securitycamera.ui.history;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.UserInfoConvert;
import com.example.securitycamera.viewmodel.HistoryViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserInfoHistoryActivity extends AppCompatActivity {

    private HistoryViewModel historyViewModel;
    ImageView imgUser;
    TextView txtUserType;
    TextView txtUserTime;
    Button btnBack;
    Button btnDeleteInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_history);

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        findViews();
        Intent intent = getIntent();
        int position = intent.getIntExtra("id_position", 0);
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
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });


        btnDeleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = Integer.toString(userInfo.getId());
                if(ConfirmDelete(id) == true)
                {
                    setResult(RESULT_OK, new Intent().putExtra("position", position));
                    finish();
                }
            }
        });
    }

    private void findViews()
    {
        imgUser = findViewById(R.id.imageViewUser);
        txtUserType = findViewById(R.id.txtUserType);
        txtUserTime = findViewById(R.id.txtUserTime);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteInfo = findViewById(R.id.btnDeleteInfo);
    }

    private boolean ConfirmDelete(String id){

        final
        AtomicBoolean successful = new AtomicBoolean(false);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông báo!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có muốn xóa thông tin này không?");
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String access_token = "1";
                historyViewModel.deleteUserInfo(access_token, id);

                historyViewModel.getDeleteResponse().observeForever(new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if(s.equals("\"successful\""))
                        {
                            successful.set(true);
                            throw new RuntimeException();
                        }
                    }
                });

            }
        });

        alertDialog.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return successful.get();
    }
}