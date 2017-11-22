package com.hlb.haolaoban.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.hlb.haolaoban.handler.MsgHandler;
import com.hlb.haolaoban.utils.Constants;
import com.orhanobut.hawk.Hawk;

/**
 * Created by heky on 2017/11/22.
 */

public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent i = new Intent(this, AlarmReceiver.class);
        i.setAction(Constants.CHANNEL);
        i.putExtra(Constants.DATA, intent.getStringExtra(Constants.DATA));
        sendBroadcast(i);
        Log.e("eeee","service start");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(Hawk.get(Constants.MID)+"")){
            MsgHandler.queryMsg(Hawk.get(Constants.MID)+"",this);
        }

    }*/
}
