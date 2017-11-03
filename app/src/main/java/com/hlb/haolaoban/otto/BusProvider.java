package com.hlb.haolaoban.otto;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by heky on 2017/10/31.
 */

public class BusProvider extends Bus {

    private static final BusProvider bus = new BusProvider();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public static BusProvider getInstance() {
        return bus;
    }

    public void postEvent(final Event event) {
        if (event == null)
            return;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            bus.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    bus.post(event);
                }
            });
        }

    }


}
