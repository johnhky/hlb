package com.hlb.haolaoban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hlb.haolaoban.fragment.PaidFragment;
import com.hlb.haolaoban.fragment.UnPayFragment;

/**
 * Created by heky on 2017/11/10.
 */

public class PrescriptionAdapter extends FragmentPagerAdapter {

    public PrescriptionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return UnPayFragment.getInstance("1");
        } else {
            return PaidFragment.getInstance("2");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "待支付";
        } else {
            return "已完成";
        }

    }
}
