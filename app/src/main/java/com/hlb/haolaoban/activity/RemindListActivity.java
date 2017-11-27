package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.RemindListAdapter;
import com.hlb.haolaoban.bean.RemindBean;
import com.hlb.haolaoban.databinding.ActivityRemindlistBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Settings;

/**
 * Created by heky on 2017/11/9.
 */

public class RemindListActivity extends BaseActivity {

    ActivityRemindlistBinding binding;
    private RemindListAdapter mAdapter;
    private Gson gson = new GsonBuilder().create();
    private ApiModule api = Api.of(ApiModule.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remindlist);
        getRemind();
        initView();
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("提醒事项");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getRemind() {
        api.getBaseUrl(HttpUrls.getRemind(Settings.getUserProfile().getMid())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                RemindBean data = gson.fromJson(response,RemindBean.class);
                if (!data.getItems().isEmpty()){
                    mAdapter = new RemindListAdapter(mActivity,data.getItems());
                    binding.recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

}

