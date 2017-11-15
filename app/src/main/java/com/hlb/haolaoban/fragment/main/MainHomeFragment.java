package com.hlb.haolaoban.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.ChatActivity;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.databinding.ActivityHomeBinding;
import com.hlb.haolaoban.base.BaseFragment;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.AudioRecordUtils;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


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
    AudioRecordUtils recordUtils;
    PopupWindow popupWindow;
    ImageView iv_state;
    TextView tv_voice;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_home, container, false);
        mActivity = getActivity();
        recordUtils = new AudioRecordUtils(mActivity);
        popupWindow = new PopupWindow();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_voice, null);
        iv_state = (ImageView) view.findViewById(R.id.iv_state);
        tv_voice = (TextView) view.findViewById(R.id.tv_voice);
        WindowManager windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        popupWindow.setContentView(view);
        popupWindow.setWidth(width / 2);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        player = new MediaPlayer();
        getArticle();
        initView();
        return binding.getRoot();
    }

    private void initView() {
        recordUtils.setOnAudioUpdateListener(new AudioRecordUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                /*int dp = (int) (3000 + 6000 * db / 100);*/
                int dp = (int) db;
                if (dp <= 30) {
                    iv_state.getDrawable().setLevel(0);
                } else if (dp <= 45 && dp > 30) {
                    iv_state.getDrawable().setLevel(1);
                } else if (dp <= 60 && dp > 45) {
                    iv_state.getDrawable().setLevel(2);
                } else if (dp <= 75 && dp > 60) {
                    iv_state.getDrawable().setLevel(3);
                } else if (dp <= 90 && dp > 75) {
                    iv_state.getDrawable().setLevel(4);
                } else if (dp > 90) {
                    iv_state.getDrawable().setLevel(5);
                }
                tv_voice.setText(Utils.getTime(time));
            }

            @Override
            public void onStop(String filePath) {

                uploadAudio(filePath);
                tv_voice.setText(Utils.getTime(0));
            }
        });

        binding.listItem.mainLlContactClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != Settings.getUserProfile()) {
                    contactClub();
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        });
        binding.listItem.mainLlContactTeam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        popupWindow.showAtLocation(binding.llRemind, Gravity.CENTER, 0, 0);
                        recordUtils.startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        recordUtils.stopRecord();
                        recordUtils.cancelRecord();
                        popupWindow.dismiss();
                        break;
                }
                return true;
            }
        });

    }

    public void uploadAudio(String fileName) {
        DialogUtils.showLoading(mActivity, "语音上传中...");
        File file = new File(fileName);
        String newFileName = System.currentTimeMillis() / 1000 + ".amr";
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "platform/index")
                .params(HttpUrls.uploadPicture(Settings.getUserProfile().getMid()))
                .addFile("file", newFileName, file).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                DialogUtils.hideLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                DialogUtils.hideLoading();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    if (code == 1) {
                        Toast.makeText(mActivity, "语音上传成功!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void getArticle() {
        api.getBaseUrl(HttpUrls.getArticle(pageNo)).enqueue(new SimpleCallback() {
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

    private void contactClub() {
        DialogUtils.showConsactClub(mActivity, Settings.getUserProfile().getUsername(), new DialogUtils.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int which) {
                if (which == 1) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + Settings.getUserProfile().getUsername()));
                    startActivity(i);
                }
            }
        });
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
