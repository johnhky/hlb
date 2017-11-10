package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.PrescriptionAdapter;
import com.hlb.haolaoban.databinding.ActivityPrescriptionBinding;
import com.hlb.haolaoban.utils.Constants;

import java.lang.reflect.Field;

/**
 * Created by heky on 2017/11/10.
 */

public class PrescriptionActivity extends BaseActivity {
    ActivityPrescriptionBinding binding;
    private FragmentPagerAdapter pagerAdapter;

    public static Intent intentFor(Context context, int position) {
        Intent i = new Intent(context, PrescriptionActivity.class);
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescription);
        binding.titlebar.tbTitle.setText("处方信息");
        initPagerAdapter();
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initPagerAdapter() {
        pagerAdapter = createPagerAdapter();
        binding.vp.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        binding.pagerTabs.setupWithViewPager(binding.vp, true);
        binding.pagerTabs.setTabMode(TabLayout.MODE_FIXED);
        binding.pagerTabs.postDelayed(new Runnable() {
            @Override
            public void run() {
                setIndicator(binding.pagerTabs,40,40);
            }
        }, 500);
    }

    protected FragmentPagerAdapter createPagerAdapter() {
        return new PrescriptionAdapter(getSupportFragmentManager());
    }

    public void setIndicator (TabLayout tabs,int leftDip,int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    }
