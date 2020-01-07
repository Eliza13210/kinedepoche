package com.oc.liza.kinedepoche;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.oc.liza.kinedepoche.controllers.MainActivity;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotifyWorker extends Worker {

    private Context context = getApplicationContext();
    private String messageBody = context.getString(R.string.notification_message_body);

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Method to trigger an instant notification
        triggerNotification();

        return Result.success();
    }

    private void triggerNotification() {
        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), MainActivity.CHANNEL_ID)
                        .setSmallIcon(R.drawable.outline_add_alert_black_24)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(createIntent())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody));

        // Add the Notification to the Notification Manager and show it.
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Show notification
        assert notificationManager != null;
        int NOTIFICATION_ID = 123;
        String NOTIFICATION_TAG = "NOTIFY";
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

    private PendingIntent createIntent() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
    }
}
