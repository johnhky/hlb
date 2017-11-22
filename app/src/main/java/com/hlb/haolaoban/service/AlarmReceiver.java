package com.hlb.haolaoban.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hlb.haolaoban.activity.TotalRemindActivity;
import com.hlb.haolaoban.utils.Constants;

/**
 * Created by heky on 2017/11/15.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        switch (intent.getAction()) {
            case Constants.CHANNEL:
                Intent i = new Intent(context, TotalRemindActivity.class);
                i.putExtra(Constants.DATA, intent.getStringExtra(Constants.DATA));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }

    }

}
