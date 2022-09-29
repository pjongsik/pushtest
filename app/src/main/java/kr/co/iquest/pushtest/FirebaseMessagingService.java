package kr.co.iquest.pushtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    private String msg, title;


    @Override
    public void onNewToken(String token) {
        Log.d("Refreshed token:",token);
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"onMessageReceived");
        Log.d(TAG,"onMessageReceived");
        Log.d(TAG,"onMessageReceived");

        /**
         * 파라미터 값
         */
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG,"remoteMessage.getNotification() ");

            title = remoteMessage.getNotification().getTitle();
            msg = remoteMessage.getNotification().getBody();
        } else {
            Log.d(TAG,"remoteMessage.getData() ");
            title = remoteMessage.getData().get("title");
            msg = remoteMessage.getData().get("body");
        }
        Log.d(TAG,"title : " + title);
        Log.d(TAG,"msg : " + msg);

        String sqNotice = remoteMessage.getData().get("sq_notice");
        String sqTest = remoteMessage.getData().get("sq_test");

        Log.d(TAG,"sqNotice : " + sqNotice);
        Log.d(TAG,"sqTest : " + sqTest);

        /**
         * 팝업
         */
        Intent intent = new Intent(this, MainActivity.class);
        if("sub".equals(sqTest)) {
            intent = new Intent(this, SubActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int pendingFlags;
        if (Build.VERSION.SDK_INT >= 23) {
            pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else {
            pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, pendingFlags);
                //PendingIntent.FLAG_ONE_SHOT);

        String channelId = "one-channel";
        String channelName = "My Channel One1";
        String channelDescription = "My Channel One Description";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                //제목
                .setContentTitle(title)
                //내용
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                ;


        try {
            URL url = new URL(remoteMessage.getData().get("largeIcon"));
            //아이콘 처리
            Bitmap bigIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            notificationBuilder.setLargeIcon(bigIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(333 /* ID of notification */, notificationBuilder.build());
    }
}

