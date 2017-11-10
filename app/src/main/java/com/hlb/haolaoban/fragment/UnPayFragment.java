package com.hlb.haolaoban.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.UnpayAdapter;
import com.hlb.haolaoban.base.BaseFragment2;
import com.hlb.haolaoban.databinding.FragmentUnpayBinding;
import com.hlb.haolaoban.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/10.
 */

public class UnPayFragment extends BaseFragment2 implements SwipeRefreshLayout.OnRefreshListener {

    FragmentUnpayBinding binding;
    private UnpayAdapter mAdapter;

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
        return binding.getRoot();
    }

    private void getData() {
        List<String> list = new ArrayList<>();
        if (getType().equals("1")) {
            list.add("诊断:MDZZ");
            list.add("诊断:智障");
        } else {
            list.add("诊断:MDZZ");
            list.add("诊断:智障");
            list.add("诊断:MDZZ");
            list.add("诊断:智障");
        }
        mAdapter = new UnpayAdapter(list, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
        binding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    private String getType() {
        return getArguments().getString(Constants.TYPE);
    }
}
