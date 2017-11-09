package com.hlb.haolaoban.fragment;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by heky on 2017/10/31.
 */

public class MainHomeFragment extends BaseFragment {

    ActivityHomeBinding binding;
    private List<View> mViewList = new ArrayList<>();
    ApiModule api = Api.of(ApiModule.class);
    private MyAdapter myAdapter;
    MediaPlayer player;
    private int pageNo = 1;
    Gson gson = new GsonBuilder().create();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_home, container, false);
        mActivity = getActivity();
        player = new MediaPlayer();
        getArticle();
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.listItem.mainLlContactClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != Settings.getUserProfile()) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(ChatActivity.class);
                }
            }
        });
    }

    private void getArticle() {
        api.getArticle(HttpUrls.getArticle(pageNo)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                ArticleBean data = gson.fromJson(response, ArticleBean.class);
                initData(data.getItems());
            }

        });
    }

    private void initData(final List<ArticleBean.ItemsBean> list) {
        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            View view = inflater.inflate(R.layout.item_home_advert, null);
            TextView tv_read = (TextView) view.findViewById(R.id.tv_read);
            final TextView tv_voice = (TextView) view.findViewById(R.id.tv_voice);
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
                    boolean isPlaying;
                    try {
                        isPlaying = player.isPlaying();
                        if (!isPlaying) {
                            tv_voice.setText("停止播放");
                            DialogUtils.showLoading(mActivity, "正在加载语音...");
                            Uri uri = Uri.parse(list.get(position).getFiles());
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

                        } else {
                            tv_voice.setText("播放语音");
                            player.stop();
                            player.release();
                        }

                    } catch (IllegalStateException e) {
                        player = null;
                        player = new MediaPlayer();
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
