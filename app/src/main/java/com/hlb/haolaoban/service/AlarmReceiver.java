package com.hlb.haolaoban.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hlb.haolaoban.activity.TotalRemindActivity;

/**
 * Created by heky on 2017/11/15.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, TotalRemindActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
