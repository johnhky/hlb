package com.hlb.haolaoban.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.ChatActivity;
import com.hlb.haolaoban.activity.LoginActivity;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.databinding.ActivityHomeBinding;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class MainHomeFragment extends BaseFragment {

    ActivityHomeBinding binding;
    Gson gson = new GsonBuilder().create();
    private List<View> mViewList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_home, container, false);
        getAriticle();
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.listItem.mainLlContactClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ChatActivity.class);
            }
        });
    }

/*
    private void getRemind(){
        Map<String,String>params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", Settings.getUserProfile().getMid()+"");
        params.put("param[type]",S)
        OkHttpUtils.get().params()
    }
*/

    private void getAriticle() {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[catid]", 9 + "");
        params.put("param[pageno]", 1 + "");
        params.put("method", "article.get.list");
        OkHttpUtils.get().url(HttpUrls.BASE_URL).params(params).build().execute(new StringCallback() {
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
                        ArticleBean data = gson.fromJson(response, ArticleBean.class);
                        initViewPager(data.getData().getItems().size(), data.getData().getItems());
                    } else if (code == -99) {
                        startActivity(LoginActivity.class);
                        mActivity.finish();
                    } else {
                        Utils.showToast(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initViewPager(int size, List<ArticleBean.DataBean.ItemsBean> datas) {
        WindowManager manager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        int screenWinth = manager.getDefaultDisplay().getWidth();
        int screenHeight = manager.getDefaultDisplay().getHeight();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxWidth(screenWinth);
            int result = (int) (screenHeight * 0.33);
            imageView.setMaxHeight(result);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mActivity).load(datas.get(i).getImage()).placeholder(R.drawable.article_bg).into(imageView);
            mViewList.add(imageView);
        }
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mViewList.size() > 0) {
                mHandler.sendEmptyMessage(0);
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            binding.vp.start(mActivity, mViewList, 5000, null, 0, 0, 0, 0);
        }
    };

}
