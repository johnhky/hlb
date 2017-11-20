package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.HealthReportAdapter;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.ReportBean;
import com.hlb.haolaoban.databinding.ActivityHealthReportBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Settings;

import retrofit2.Call;

/**
 * Created by heky on 2017/11/15.
 */

public class HealthReportActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityHealthReportBinding binding;
    private ApiModule api = Api.of(ApiModule.class);
    private int pageNo = 1;
    Gson gson = new GsonBuilder().create();
    HealthReportAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_report);
        initView();
        onRefresh();
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("健康报告");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.swipeRefresh.setOnRefreshListener(this);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        api.getBaseUrl(HttpUrls.getHealthReport(Settings.getUserProfile().getMid(), pageNo)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                ReportBean data = gson.fromJson(response, ReportBean.class);
                if (!data.getItems().isEmpty()) {
                    mAdapter = new HealthReportAdapter(mActivity, data.getItems());
                    binding.recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
