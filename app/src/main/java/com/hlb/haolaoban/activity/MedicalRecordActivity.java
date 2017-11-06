package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.MedicalRecordAdapter;
import com.hlb.haolaoban.databinding.ActivityMedicalRecordBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/6.
 */

public class MedicalRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityMedicalRecordBinding binding;
    private MedicalRecordAdapter mAdapter;

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
        List<String> datas = new ArrayList<>();
        datas.add("2017-10-25");
        datas.add("2017-10-26");
        datas.add("2017-10-27");
        datas.add("2017-10-28");
        mAdapter = new MedicalRecordAdapter(datas);
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
