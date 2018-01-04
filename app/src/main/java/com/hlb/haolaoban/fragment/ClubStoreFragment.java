package com.hlb.haolaoban.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlb.haolaoban.base.BaseFragment2;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubStoreFragment extends BaseFragment2 {

    public static ClubStoreFragment intentFor() {
        ClubStoreFragment clubStoreFragment = new ClubStoreFragment();
        Bundle bundle = new Bundle();
        clubStoreFragment.setArguments(bundle);
        return clubStoreFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
