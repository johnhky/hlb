package com.hlb.haolaoban.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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

    public static void showNotification(String type, String msg) {
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
/*

    public static void showNotificationMsg(String msg, Context context) {
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //设置小图标
                .setSmallIcon(R.mipmap.logo)
                //设置通知标题
                .setContentTitle("好老伴新消息")
                //设置通知内容
                .setContentText(msg);
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }
*/

}
