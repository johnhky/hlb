package com.hlb.haolaoban.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;

import com.hlb.haolaoban.otto.BusProvider;

/**
 * Created by heky on 2017/10/31.
 */

public class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        BusProvider.getInstance().register(this);

    }

    public void startActivity(Class c) {
        Intent toStart = new Intent();
        toStart.setClass(mActivity, c);
        startActivity(toStart);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    public static boolean isActivityDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && activity != null && activity.isDestroyed()) {
            return true;
        }
        return false;
    }

}
