package com.hlb.haolaoban.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.ClubAdapter;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.databinding.ActivityClubBinding;
import com.hlb.haolaoban.module.LoginModule;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class MainClubFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    Gson gson = new GsonBuilder().create();
    ActivityClubBinding binding;
    private ClubAdapter mAdapter;
    ArticleBean data;
    private int pageNo = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_club, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.mListView.setLayoutManager(linearLayoutManager);
        binding.swipeRefresh.setOnRefreshListener(this);
        getClub();
        return binding.getRoot();
    }

    private void getClub() {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[catid]", 9 + "");
        params.put("param[pageno]", pageNo + "");
        params.put("method", "article.get.list");
        OkHttpUtils.get().url(LoginModule.BASE_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int code = jobj.optInt("code");
                    if (code == 1) {
                        data = gson.fromJson(response, ArticleBean.class);
                        mAdapter = new ClubAdapter(data.getData().getItems());
                        binding.mListView.setAdapter(mAdapter);
                        binding.swipeRefresh.setRefreshing(false);
                    } else if (code == -99) {
                        Log.e("eeee",response);
                     /*   startActivity(LoginActivity.class);
                        mActivity.finish();*/
                    } else {
                        Utils.showToast(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        getClub();
    }

}
