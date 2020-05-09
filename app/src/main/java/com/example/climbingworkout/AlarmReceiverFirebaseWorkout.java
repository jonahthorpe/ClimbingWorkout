package com.example.climbingworkout;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

public class AlarmReceiverFirebaseWorkout extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = intent.getStringExtra("key");

        Intent resultIntent = new Intent(context, FirebaseWorkoutOverview.class);
        resultIntent.putExtra("key", key);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Utility.createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Utility.MAIN_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Workout Reminder")
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);


        builder.setContentText("My Workout");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }
}
