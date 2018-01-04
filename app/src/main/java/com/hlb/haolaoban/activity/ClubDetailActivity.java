package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.ClubDetailAdapter;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.databinding.ActivityClubDetailBinding;
import com.hlb.haolaoban.utils.Constants;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubDetailActivity extends BaseActivity {

    ActivityClubDetailBinding binding;

    public static Intent intentFor(Context context, String id) {
        Intent i = new Intent(context, ClubDetailActivity.class);
        i.putExtra(Constants.DATA, id);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_club_detail);
        binding.titlebar.tbTitle.setText("俱乐部");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initAdapter();
    }


    private void initAdapter() {
        PagerAdapter pagerAdapter = createAdapter();
        binding.vp.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        binding.pagerTabs.setupWithViewPager(binding.vp, true);
        binding.pagerTabs.setTabMode(TabLayout.MODE_FIXED);
    }

    private PagerAdapter createAdapter() {
        return new ClubDetailAdapter(getSupportFragmentManager(), id());
    }

    private String id() {
        return getIntent().getStringExtra(Constants.DATA);
    }

}
