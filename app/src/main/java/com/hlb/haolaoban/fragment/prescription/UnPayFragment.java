package com.hlb.haolaoban.fragment.prescription;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.hlb.haolaoban.otto.RefreshOrderEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.hlb.haolaoban.widget.RecyclerViewScroller;
import com.orhanobut.hawk.Hawk;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by heky on 2017/11/10.
 */

public class UnPayFragment extends BaseFragment2 implements SwipeRefreshLayout.OnRefreshListener {

    FragmentUnpayBinding binding;
    private UnpayAdapter mAdapter;
    private int pageNo = 1;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    List<OrderBean.ItemsBean> list = new ArrayList<>();

    public static UnPayFragment getInstance(String type) {
        UnPayFragment unPayFragment = new UnPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        unPayFragment.setArguments(bundle);
        return unPayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unpay, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.swipeRefresh.setOnRefreshListener(this);
        onRefresh();
        mAdapter = new UnpayAdapter(list, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerViewScroller(linearLayoutManager,mAdapter) {
            @Override
            public void onLoadMore() {
                pageNo++;
                getData(pageNo);
            }
        });
        return binding.getRoot();
    }

    private void getData(final int pageNo) {
        api.getBaseUrl(HttpUrls.getOrderList(Hawk.get(Constants.MID) + "", pageNo, getType())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                OrderBean data = gson.fromJson(response, OrderBean.class);
                if (!data.getItems().isEmpty()) {
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
        binding.swipeRefresh.setRefreshing(true);
        list = new ArrayList<>();
        getData(1);
    }

    @Subscribe
    public void receiveEvent(RefreshOrderEvent event) {
        onRefresh();
    }

    private String getType() {
        return getArguments().getString(Constants.TYPE);
    }
}
