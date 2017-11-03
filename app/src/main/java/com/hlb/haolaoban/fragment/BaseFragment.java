package com.hlb.haolaoban.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

/**
 * Created by heky on 2017/10/31.
 */

public class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity=null;
    }

    public void startActivity(Class c){
        Intent toStart = new Intent();
        toStart.setClass(mContext,c);
        startActivity(toStart);
    }
}
