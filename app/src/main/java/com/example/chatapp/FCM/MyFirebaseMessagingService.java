package com.example.chatapp.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.example.chatapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_ID = 1;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // if(remoteMessage.getFrom().equals("Admin")) {
            generateNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());

       // }
    }
//
//    private void generateNotification(String body, String title) {
//
//        Intent intent = new Intent(this,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
//
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.android)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setAutoCancel(true)
//                .setSound(soundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if(NOTIFICATION_ID > 1073741824)
//        {
//            NOTIFICATION_ID = 0;
//        }
//        notificationManager.notify(NOTIFICATION_ID++,notificationBuilder.build());
//    }
//
//
//
//
//
//
    private void generateNotification(String body, String title) {



        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(          String.valueOf(NOTIFICATION_ID) ,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(body);
            mNotificationManager.createNotificationChannel(channel);
        }
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), String.valueOf(NOTIFICATION_ID))
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(body)// message for notification
                .setSound(soundUri)
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        if(NOTIFICATION_ID > 1073741824)
        {
            NOTIFICATION_ID = 0;
        }
        mNotificationManager.notify(NOTIFICATION_ID++,mBuilder.build());

    }


//    private void generatelolipopNotification(String body, String title) {
//
//        Intent intent = new Intent(this,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
//
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setAutoCancel(true)
//                .setSound(soundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if(NOTIFICATION_ID > 1073741824)
//        {
//            NOTIFICATION_ID = 0;
//        }
//        notificationManager.notify(NOTIFICATION_ID++,notificationBuilder.build());
//    }
}
