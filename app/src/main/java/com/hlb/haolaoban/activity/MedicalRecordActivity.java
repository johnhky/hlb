package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.MedicalRecordAdapter;
import com.hlb.haolaoban.adapter.SpaceItemDecoration;
import com.hlb.haolaoban.bean.HealthRecordBean;
import com.hlb.haolaoban.databinding.ActivityMedicalRecordBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Settings;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by heky on 2017/11/6.
 */

public class MedicalRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityMedicalRecordBinding binding;
    private MedicalRecordAdapter mAdapter;
    private int pageNo = 1;
    private ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical_record);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("病历档案");
        binding.swipeRefresh.setOnRefreshListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        getMedicalRecord(pageNo);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getMedicalRecord(int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        api.getArticle(HttpUrls.getHealthRecord(Settings.getUserProfile().getMid(), pageNo)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                HealthRecordBean data = gson.fromJson(response, HealthRecordBean.class);
                if (!data.getItems().isEmpty()) {
                    mAdapter = new MedicalRecordAdapter(data.getItems(), MedicalRecordActivity.this);
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
        getMedicalRecord(1);
    }
}
