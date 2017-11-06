package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.MedicalCountAdapter;
import com.hlb.haolaoban.databinding.ActivityMedicalCountBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/6.
 */

public class MedicalCountActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityMedicalCountBinding binding;
    private MedicalCountAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical_count);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("我的药品");
        binding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        List<String> datas = new ArrayList<>();
        datas.add("阿莫西林");
        datas.add("阿莫西林");
        datas.add("阿莫西林");
        mAdapter = new MedicalCountAdapter(datas);
        binding.recyclerView.setAdapter(mAdapter);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onRefresh() {
        binding.swipeRefresh.setRefreshing(false);
    }
}
