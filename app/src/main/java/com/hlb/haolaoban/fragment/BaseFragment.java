package com.hlb.haolaoban.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;

/**
 * Created by heky on 2017/10/31.
 */

public class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void startActivity(Class c) {
        Intent toStart = new Intent();
        toStart.setClass(mActivity, c);
        startActivity(toStart);
    }
}
