package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.MessageAdapter;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.DrugRemind;
import com.hlb.haolaoban.bean.MessageBean;
import com.hlb.haolaoban.databinding.ActivityMsgBinding;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Settings;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by heky on 2017/11/20.
 */

public class MessageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityMsgBinding binding;
    MessageAdapter mAdapter;
    Gson gson = new GsonBuilder().create();
    private int pageNo = 1;
    List<MessageBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_msg);
        binding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.titlebar.tbTitle.setText("我的消息");
        list = new ArrayList<>();
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        onRefresh();
    }

    private void initData(int pageNo) {
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "platform/index").params(HttpUrls.getMessage(pageNo + "", Settings.getUserProfile().getMid()+"")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                binding.swipeRefresh.setRefreshing(false);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        String msg = jsonObject.getString("msg");
                        if (null != msg&&!TextUtils.isEmpty(msg)&&!msg.equals("null")) {
                            list = gson.fromJson(msg, new TypeToken<ArrayList<MessageBean>>() {
                            }.getType());
                            if (!list.isEmpty()) {
                                mAdapter = new MessageAdapter(list, mActivity);
                                binding.recyclerView.setAdapter(mAdapter);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
  /*      mAdapter = new MessageAdapter(lists, mActivity);
        binding.recyclerView.setAdapter(mAdapter);*/
    }

    @Override
    public void onRefresh() {
        binding.swipeRefresh.setRefreshing(true);
        pageNo = 1;
        initData(pageNo);
    }
}
