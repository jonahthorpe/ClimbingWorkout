package com.example.climbingworkout;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.lifecycle.ViewModelProvider;


public class AlarmReceiverWorkout extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        int cardID = intent.getIntExtra("cardId", 1);
        int difficultySelection = intent.getIntExtra("difficultySelection", 0);
        String imgName = intent.getStringExtra("imageName");
        String workoutTitle = intent.getStringExtra("workoutTitle");
        String workoutCategory = intent.getStringExtra("workoutCategory");
        int notificationId = intent.getIntExtra("notificationId", 0);


        Intent resultIntent = new Intent(context, WorkoutOverview.class);
        resultIntent.putExtra("cardID", cardID);
        resultIntent.putExtra("difficultySelection", difficultySelection);
        resultIntent.putExtra("imageName", imgName);
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
            switch (difficultySelection) {
                case 0:
                    builder.setContentText("Beginner " + workoutTitle + " " + workoutCategory);
                    break;
                case 1:
                    builder.setContentText("Intermediate" + workoutTitle + " " + workoutCategory);
                    break;
                case 2:
                    builder.setContentText("Advanced" + workoutTitle + " " + workoutCategory);
                    break;
            }



            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationId, builder.build());

    }
}
