package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.databinding.ActivityHealthReportDetailBinding;
import com.hlb.haolaoban.utils.Constants;

/**
 * Created by heky on 2017/11/15.
 */

public class HealthReportDetailActivity extends BaseActivity {
    ActivityHealthReportDetailBinding binding;

    public static Intent intentFor(Context context, String id, String name, String time, String content) {
        Intent i = new Intent(context, HealthReportDetailActivity.class);
        i.putExtra(Constants.MID, id);
        i.putExtra(Constants.DATA, name);
        i.putExtra(Constants.TYPE, time);
        i.putExtra(Constants.CHANNEL, content);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_report_detail);
        initView();
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText(getName());
        binding.tvContent.setText(getContent());
        binding.tvTime.setText(getTime());
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

/*
    private void initData() {
        api.getBaseUrl(HttpUrls.getHealthReportDetail(getId())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
            }
        });
    }
*/

    private String getId() {
        return getIntent().getStringExtra(Constants.MID);
    }

    private String getName() {
        return getIntent().getStringExtra(Constants.DATA);
    }

    private String getContent() {
        return getIntent().getStringExtra(Constants.CHANNEL);
    }

    private String getTime() {
        return getIntent().getStringExtra(Constants.TYPE);
    }

}
