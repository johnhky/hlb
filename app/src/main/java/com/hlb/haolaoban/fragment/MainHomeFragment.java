package com.hlb.haolaoban.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static android.R.attr.path;

/**
 * Created by heky on 2017/10/31.
 */

public class MainHomeFragment extends BaseFragment {

    ActivityHomeBinding binding;
    Gson gson = new GsonBuilder().create();
    private List<View> mViewList = new ArrayList<>();
    private MyAdapter myAdapter;

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
                        initData(data.getData().getItems());
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

    private void initData(final List<ArticleBean.DataBean.ItemsBean> list) {
        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            View view = inflater.inflate(R.layout.item_home_advert, null);
            TextView tv_read = (TextView) view.findViewById(R.id.tv_read);
            TextView tv_voice = (TextView) view.findViewById(R.id.tv_voice);
            ImageView iv_title = (ImageView) view.findViewById(R.id.iv_title);
            Glide.with(mActivity).load(list.get(i).getImage()).centerCrop().into(iv_title);
            mViewList.add(view);
            final int position = i;
            tv_read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            tv_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showLoading(mActivity, "正在加载语音...");
                    Uri uri = Uri.parse(list.get(position).getFiles());
                    MediaPlayer player = new MediaPlayer();
                    try {
                        player.setDataSource(mActivity, uri);
                        player.prepareAsync();
                        if (player.isPlaying()) {
                            player.stop();
                            player.release();
                            player = new MediaPlayer();
                            uri = Uri.parse(list.get(position).getFiles());
                            player.setDataSource(mActivity, uri);
                            player.prepareAsync();
                        }
                        player.setLooping(false);
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                DialogUtils.hideLoading();
                                mp.start();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                    }

                }
            });
        }
        myAdapter = new MyAdapter(mViewList);
        binding.mViewpager.setAdapter(myAdapter);
    }

    class MyAdapter extends PagerAdapter {

        public List<View> mViewList;

        public MyAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

}
