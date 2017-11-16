package com.hlb.haolaoban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hlb.haolaoban.fragment.prescription.DoneFragment;
import com.hlb.haolaoban.fragment.prescription.ExpiredFragment;
import com.hlb.haolaoban.fragment.prescription.PaidFragment;
import com.hlb.haolaoban.fragment.prescription.SendedFragment;
import com.hlb.haolaoban.fragment.prescription.UnPayFragment;

/**
 * Created by heky on 2017/11/10.
 */

public class PrescriptionAdapter extends FragmentPagerAdapter {

    public PrescriptionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return UnPayFragment.getInstance("2");
            }
            case 1: {
                return PaidFragment.getInstance("1");
            }
            case 2: {
                return SendedFragment.getInstance("3");
            }
            case 3: {
                return DoneFragment.getInstance("4");
            }
            case 4: {
                return ExpiredFragment.getInstance("5");
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "待支付";
            case 1:
                return "已支付";
            case 2:
                return "已配送";
            case 3:
                return "已完成";
            case 4:
                return "已过期";
        }
        return "";
    }
}
