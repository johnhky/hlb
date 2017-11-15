package com.hlb.haolaoban.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.hlb.haolaoban.MyApplication;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.RemindListActivity;

/**
 * Created by heky on 2017/11/15.
 */

public class NotificationUtil {

    public static final int NEW_MSG = 100;

    public static void showNotification(String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.mContext);
        builder.setContentTitle("好老伴新消息");
        builder.setContentText(msg);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setLights(Color.GREEN, 1000, 3000);
        Intent i = new Intent(MyApplication.mContext, RemindListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.mContext, NEW_MSG, i, 0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        long[] longs = new long[]{0, 300, 150, 300};
        builder.setVibrate(longs);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MyApplication.mContext);
        managerCompat.notify(NEW_MSG, notification);
    }

}
