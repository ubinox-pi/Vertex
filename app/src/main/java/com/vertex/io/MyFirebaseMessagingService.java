package com.vertex.io;


import static android.app.PendingIntent.FLAG_ONE_SHOT;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Send the new token to your server or save it for future use
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        if (remoteMessage.getNotification() != null) {
            // Display notification if the message contains a notification payload.
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            // Handle the data payload here if needed.
        }
    }


    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class); // The activity to open when the notification is clicked
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "default_notification_channel_id";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.img)  // App's icon
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // For Android 7.1 and lower
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Since Android 8.0 (Oreo) requires a notification channel, create one if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel Human Readable Title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Notify with unique ID to allow multiple notifications
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

}
