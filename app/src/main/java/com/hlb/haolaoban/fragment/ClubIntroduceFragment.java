package com.hlb.haolaoban.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseFragment2;
import com.hlb.haolaoban.bean.ClubBean;
import com.hlb.haolaoban.databinding.FragmentIntroduceBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;

import java.util.List;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubIntroduceFragment extends BaseFragment2 {

    FragmentIntroduceBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();

    public static ClubIntroduceFragment intentFor(String id) {
        ClubIntroduceFragment clubIntroduceFragment = new ClubIntroduceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA, id);
        clubIntroduceFragment.setArguments(bundle);
        return clubIntroduceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_introduce, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        api.getBaseUrl(HttpUrls.clubDetail(id())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                ClubBean data = gson.fromJson(response, ClubBean.class);
                initData(data);
            }
        });
    }

    private void initData(ClubBean data) {
        if (null==data){
            return;
        }
        binding.tvDoctor.setText(data.getDoctor_name());
        binding.tvIntroduce.setText(data.getDoctor_introduce());
            binding.tvAbout.setText(Html.fromHtml(data.getContent()));
        Glide.with(mActivity).load(data.getPhoto()).into(binding.ivMain);
        Glide.with(mActivity).load(data.getDoctor_photo()).into(binding.ivAvatar);
        MyAdapter mAdapter = new MyAdapter(data.getEnvironment_image());
        binding.gridView.setAdapter(mAdapter);
    }


    class MyAdapter extends BaseAdapter {

        List<String> list;

        public MyAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_club_image, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mActivity).load(list.get(position)).into(holder.iv);
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
        }
    }

    private String id() {
        return getArguments().getString(Constants.DATA);
    }
}
