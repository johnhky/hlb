package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.MessageAdapter;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.databinding.ActivityMsgBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/20.
 */

public class MessageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityMsgBinding binding;
    MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_msg);
        binding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.titlebar.tbTitle.setText("我的消息");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        onRefresh();
    }

    private void initData() {
        List<String> lists = new ArrayList<>();
        lists.add("咨询消息");
        lists.add("系统消息");
        lists.add("通知消息");
        mAdapter = new MessageAdapter(lists, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
