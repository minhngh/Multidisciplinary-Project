package com.example.securitycamera.worker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.securitycamera.utils.WorkerUtils;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION = "NOTIFY_MODE_CHANGE";
    public static final String KEY_IS_TO_CAUTIOUS_MODE = "IS_TO_CAUTIOUS_MODE";

    public AlarmReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
        if (intent.getAction().equals(ACTION)) {
            boolean isToCautiousMode = intent.getBooleanExtra(KEY_IS_TO_CAUTIOUS_MODE, false);
            WorkerUtils.makeModeChangeNotification(context, isToCautiousMode);
        }
    }
}