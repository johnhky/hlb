package com.hlb.haolaoban.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.ClubActivityAdapter;
import com.hlb.haolaoban.adapter.ClubAdapter;
import com.hlb.haolaoban.base.BaseFragment2;
import com.hlb.haolaoban.bean.ClubActivityBean;
import com.hlb.haolaoban.databinding.FragmentClubBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubActivityFragment extends BaseFragment2 implements SwipeRefreshLayout.OnRefreshListener {

    FragmentClubBinding binding;
    private int pageNo;
    private Gson gson = new GsonBuilder().create();
    ApiModule api = Api.of(ApiModule.class);
    List<ClubActivityBean.ItemsBean> list = new ArrayList<>();
    ClubActivityAdapter mAdapter;

    public static ClubActivityFragment intentFor(String id) {
        ClubActivityFragment clubActivityFragment = new ClubActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA, id);
        clubActivityFragment.setArguments(bundle);
        return clubActivityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_club, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        binding.swipeRefresh.setOnRefreshListener(this);
        mAdapter = new ClubActivityAdapter(mActivity, list);
        binding.recyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    private void activityList(final int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        api.getBaseUrl(HttpUrls.clubActivity("", pageNo + "", id())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                ClubActivityBean data = gson.fromJson(response, ClubActivityBean.class);
                if (null != data.getItems() || !data.getItems().isEmpty()) {
                    list.addAll(data.getItems());
                } else {
                    if (pageNo > 1) {
                        Utils.showToast("暂时没有更多数据了");
                    }
                }
                mAdapter.update(list);
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
        pageNo = 1;
        activityList(pageNo);
    }

    private String id() {
        return getArguments().getString(Constants.DATA);
    }

}
