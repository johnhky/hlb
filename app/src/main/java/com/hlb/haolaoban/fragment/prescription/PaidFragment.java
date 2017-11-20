package com.hlb.haolaoban.fragment.prescription;

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
import com.hlb.haolaoban.adapter.UnpayAdapter;
import com.hlb.haolaoban.base.BaseFragment2;
import com.hlb.haolaoban.bean.OrderBean;
import com.hlb.haolaoban.databinding.FragmentUnpayBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;

import retrofit2.Call;

/**
 * Created by heky on 2017/11/10.
 */

public class PaidFragment extends BaseFragment2 implements SwipeRefreshLayout.OnRefreshListener {

    FragmentUnpayBinding binding;
    private UnpayAdapter mAdapter;
    private int pageNo = 1;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();

    public static PaidFragment getInstance(String type) {
        PaidFragment paidFragment = new PaidFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        paidFragment.setArguments(bundle);
        return paidFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unpay, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.swipeRefresh.setOnRefreshListener(this);
        onRefresh();
        return binding.getRoot();
    }

    private void getData() {
        api.getBaseUrl(HttpUrls.getOrderList(Settings.getUserProfile().getMid() + "", pageNo, getType())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                OrderBean data = gson.fromJson(response, OrderBean.class);
                if (!data.getItems().isEmpty()) {
                    mAdapter = new UnpayAdapter(data.getItems(), mActivity);
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
        getData();
    }

    private String getType() {
        return getArguments().getString(Constants.TYPE);
    }
}