package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.hlb.haolaoban.base.BaseActivity;
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
    }

    protected FragmentPagerAdapter createPagerAdapter() {
        return new PrescriptionAdapter(getSupportFragmentManager());
    }

}
