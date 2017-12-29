package com.hlb.haolaoban.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.hlb.haolaoban.MainActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.ChattingActivity;
import com.hlb.haolaoban.activity.PrescriptionActivity;

/**
 * Created by heky on 2017/11/15.
 */

public class NotificationUtil {

    public static final int NEW_MSG = 100;

    public static void showNotification(Context context, String type, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("好老伴新消息");
        builder.setContentText(msg);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setLights(Color.GREEN, 1000, 3000);
        Intent i;
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("message")){
                i = new Intent(context, ChattingActivity.class);
            }else {
                i = new Intent(context, PrescriptionActivity.class);
            }
        } else {
            i = new Intent(context, MainActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NEW_MSG, i, 0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        long[] longs = new long[]{0, 300, 150, 300};
        builder.setVibrate(longs);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NEW_MSG, notification);
    }

}
