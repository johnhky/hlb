package com.hlb.haolaoban.fragment.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.ClubAdapter;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.databinding.ActivityClubBinding;
import com.hlb.haolaoban.base.BaseFragment;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;

import retrofit2.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class MainClubFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    ActivityClubBinding binding;
    private ClubAdapter mAdapter;
    private int pageNo = 1;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_club, container, false);
        binding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        getClub(pageNo);
        return binding.getRoot();
    }

    private void getClub(int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        api.getArticle(HttpUrls.getArticle(pageNo)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                ArticleBean data = gson.fromJson(response, ArticleBean.class);
                if (!data.getItems().isEmpty()) {
                    mAdapter = new ClubAdapter(data.getItems(), mActivity);
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
        getClub(pageNo);
    }

}
