package com.hlb.haolaoban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hlb.haolaoban.fragment.ClubActivityFragment;
import com.hlb.haolaoban.fragment.ClubIntroduceFragment;
import com.hlb.haolaoban.fragment.ClubStoreFragment;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubDetailAdapter extends FragmentStatePagerAdapter {

    String id;

    public ClubDetailAdapter(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ClubActivityFragment.intentFor(id);
            case 1:
                return ClubIntroduceFragment.intentFor(id);
            case 2:
                return ClubStoreFragment.intentFor();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "最新活动";
            case 1:
                return "俱乐部介绍";
            case 2:
                return "俱乐部商城";
        }
        return null;
    }

}
