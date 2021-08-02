package com.example.securitycamera.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.securitycamera.R;
import com.example.securitycamera.data.remote.MainApiUtils;
import com.example.securitycamera.ui.main.MainActivity;
import static com.example.securitycamera.utils.AlarmBroadcastReceiver.TITLE;
import static com.example.securitycamera.application.App.CHANNEL_ID;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlarmService extends Service {
    private MainApiUtils mainApiService;
    @Override
    public void onCreate() {
        super.onCreate();
        mainApiService = MainApiUtils.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s Schedule", intent.getStringExtra(TITLE));

        Call<JsonObject> call = mainApiService.turnOnCaution("TOKEN");
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    Notification notification = new NotificationCompat.Builder(AlarmService.this, CHANNEL_ID)
                            .setContentTitle(alarmTitle)
                            .setContentText("Switched to ALERT mode successfully")
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .build();

                    startForeground(1, notification);
                }
                else{
                    Notification notification = new NotificationCompat.Builder(AlarmService.this, CHANNEL_ID)
                            .setContentTitle(alarmTitle)
                            .setContentText("Failed to switch to ALERT mode")
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .build();

                    startForeground(1, notification);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Notification notification = new NotificationCompat.Builder(AlarmService.this, CHANNEL_ID)
                        .setContentTitle("Schedule")
                        .setContentText("Failed to ALERT mode")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();

                startForeground(1, notification);
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
