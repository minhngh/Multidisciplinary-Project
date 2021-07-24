package com.example.securitycamera.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.securitycamera.R;


final public class WorkerUtils {
    public static final CharSequence MODE_NOTIFICATION_CHANNEL_NAME = "Mode Change";
    public static final String MODE_NOTIFICATION_CHANNEL_DESCRIPTION = "To notify change in mode.";
    public static final String MODE_NOTIFICATION_CHANNEL_ID = "MODE_CHANGE.";

    public static final int MODE_NOTIFICATION_ID = 1;


    /**
     * Create a Mode Change notification.
     * @param context Context needed to create Notification
     * @param toCautiousMode Whether mode change to cautious or to normal.
     */
    public static void makeModeChangeNotification(@NonNull Context context, boolean toCautiousMode) {
        // Make a channel if necessary
        createModeNotificationChannel(context);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context, MODE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(toCautiousMode? "Cautious mode ON": "Cautious mode OFF")
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setTimeoutAfter(1000 * 60); // 1 min

        // Show the notification
        NotificationManagerCompat.from(context).notify(MODE_NOTIFICATION_ID, builder.build());
    }

    private static void createModeNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(
                    MODE_NOTIFICATION_CHANNEL_ID,
                    MODE_NOTIFICATION_CHANNEL_NAME,
                    importance);
            channel.setDescription(MODE_NOTIFICATION_CHANNEL_DESCRIPTION);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private WorkerUtils() {}
}