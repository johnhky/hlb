package com.hlb.haolaoban.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hlb.haolaoban.activity.TotalRemindActivity;
import com.hlb.haolaoban.utils.Constants;

/**
 * Created by heky on 2017/11/10.
 */

public class RemindService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + 1000;
        Intent i = new Intent(this, TotalRemindActivity.class);
        i.putExtra(Constants.DATA, intent.getStringExtra(Constants.DATA));
        PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, p);//1min后返回执行
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
