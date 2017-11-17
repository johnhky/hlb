package com.hlb.haolaoban.activity.device;

import com.hlb.haolaoban.MyApplication;
import com.inuker.bluetooth.library.BluetoothClient;

/**
 * Created by heky on 2017/11/17.
 */

public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(MyApplication.getInstance());
                }
            }
        }
        return mClient;
    }

}
