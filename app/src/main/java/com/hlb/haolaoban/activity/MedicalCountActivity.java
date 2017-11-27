package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.MedicalAdapter;
import com.hlb.haolaoban.bean.MedicalBean;
import com.hlb.haolaoban.databinding.ActivityMedicalCountBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/6.
 */

public class MedicalCountActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityMedicalCountBinding binding;
    private MedicalAdapter mAdapter;
    private int pageNo = 1;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    List<MedicalBean.ItemsBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical_count);
        getMedical(pageNo);
        initView();
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("药品存量");
        binding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MedicalCountActivity.this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MedicalAdapter(list, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getMedical(final int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        api.getBaseUrl(HttpUrls.getMedical(Settings.getUserProfile().getMid() + "", pageNo)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                MedicalBean data = gson.fromJson(response, MedicalBean.class);
                if (!data.getItems().isEmpty()) {
                    list.addAll(data.getItems());
                } else {
                    if (pageNo > 1) {
                        showToast("暂时没有更多数据了");
                    }
                }
                mAdapter.update(list);
            }
        });
    }

    @Override
    public void onRefresh() {
        list  = new ArrayList<>();
        getMedical(1);
    }
}
