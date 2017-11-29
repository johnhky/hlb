package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.MessageAdapter;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.MessageBean;
import com.hlb.haolaoban.databinding.ActivityMsgBinding;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.TokenOutEvent;
import com.hlb.haolaoban.utils.Settings;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by heky on 2017/11/20.
 *
 */

public class MessageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityMsgBinding binding;
    MessageAdapter mAdapter;
    Gson gson = new GsonBuilder().create();
    private int pageNo = 1;
    List<MessageBean.MsgBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_msg);
        binding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.titlebar.tbTitle.setText("我的消息");
        mAdapter = new MessageAdapter(list, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
  /*      binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == recyclerView.SCROLL_STATE_IDLE && !ViewCompat.canScrollVertically(recyclerView, 1)) {
                    pageNo++;
                    initData(pageNo);
                }
            }
        });*/
        onRefresh();
    }

    private void initData(final int pageNo) {
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "platform/index").params(HttpUrls.getMessage(pageNo + "", Settings.getUserProfile().getMid() + "")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(String response, int id) {
                binding.swipeRefresh.setRefreshing(false);
                MessageBean data = gson.fromJson(response, MessageBean.class);
                if (data.getCode() == 1) {
                    if (!data.getMsg().equals("null") && null!=data.getMsg()) {
                        list.addAll(data.getMsg());
                    } else {
                        if (pageNo > 1) {
                            showToast("暂时没有更多数据了");
                        }
                    }
                    mAdapter.update(list);
                } else if (data.getCode() == -99) {
                    BusProvider.getInstance().postEvent(new TokenOutEvent(data.getCode()));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        binding.swipeRefresh.setRefreshing(true);
        list = new ArrayList<>();
        initData(1);
    }
}
