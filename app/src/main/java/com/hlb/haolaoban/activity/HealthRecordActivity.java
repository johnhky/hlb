package com.hlb.haolaoban.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.adapter.HealthRecordAdapter;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.HealthBean;
import com.hlb.haolaoban.bean.MessageBean;
import com.hlb.haolaoban.databinding.ActivityHealthRecordBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/2.
 */

public class HealthRecordActivity extends BaseActivity {
    ActivityHealthRecordBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    HealthRecordAdapter mAdapter;
    List<HealthBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_record);
        binding.titlebar.tbTitle.setText("健康档案");
        initView();
        initData();
    }

    private void initData() {
        api.getBaseUrl(HttpUrls.getRealtimetypeList()).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                List<HealthBean> lists = gson.fromJson(response, new TypeToken<ArrayList<HealthBean>>() {
                }.getType());
                if (!lists.isEmpty()) {
                    list.addAll(lists);
                }
                mAdapter.update(list);
            }
        });
    }


    protected void initView() {
        mAdapter = new HealthRecordAdapter(list, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MedicalRecordActivity.class);
            }
        });
        binding.tvMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MedicalCountActivity.class);
            }
        });
        binding.tvMedicalRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = PrescriptionActivity.intentFor(HealthRecordActivity.this, 0);
                startActivity(i);
            }
        });
        binding.tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HealthReportActivity.class);
            }
        });
    }
}
