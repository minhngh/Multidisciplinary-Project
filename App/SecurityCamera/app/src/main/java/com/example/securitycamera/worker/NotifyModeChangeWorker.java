package com.example.securitycamera.worker;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.securitycamera.R;
import com.example.securitycamera.data.local.AppPreferences;
import com.example.securitycamera.utils.WorkerUtils;

import org.jetbrains.annotations.NotNull;

public class NotifyModeChangeWorker extends Worker {
    public static final String TAG = "MODE_NOTIFY";

    public static final String MODE_KEY = "MODE";

    public NotifyModeChangeWorker(
            @NonNull Application application,
            @NonNull WorkerParameters params) {
        super(application, params);
    }

    @Override
    @NonNull
    public Result doWork() {
        Context context = getApplicationContext();
        boolean toCautiousMode = getInputData().getBoolean(MODE_KEY, false);
        WorkerUtils.makeModeChangeNotification(context, toCautiousMode);

        return Result.success();
    }
}
