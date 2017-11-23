package com.hlb.haolaoban.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hlb.haolaoban.MyApplication;
import com.hlb.haolaoban.service.websocket.WebSocketManager;

/**
 * Created by heky on 2017/11/23.
 */

public class NetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            // 获取网络连接管理器
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) MyApplication.mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取当前网络状态信息
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();

            if (info != null && info.isAvailable()) {
                Log.e("eeee", "监听到可用网络切换,调用重连方法");
                WebSocketManager.getInstance().reconnect();//wify 4g切换重连websocket
            }
        }
    }
}