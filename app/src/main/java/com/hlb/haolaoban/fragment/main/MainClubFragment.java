package com.hlb.haolaoban.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.hlb.haolaoban.activity.ClubDetailActivity;
import com.hlb.haolaoban.activity.VideoActivity;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.adapter.ClubAdapter;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.bean.ClubBean;
import com.hlb.haolaoban.bean.ClubListBean;
import com.hlb.haolaoban.databinding.ActivityClubBinding;
import com.hlb.haolaoban.base.BaseFragment;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.TokenOutEvent;
import com.hlb.haolaoban.utils.AudioRecordUtils;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.hlb.haolaoban.widget.RecyclerViewScroller;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class MainClubFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    ActivityClubBinding binding;
    private int pageNo = 1;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    AudioRecordUtils recordUtils;
    PopupWindow popupWindow;
    ImageView iv_state;
    TextView tv_voice;
    LinearLayoutManager linearLayoutManager;
    private float startY, endY;
    ClubBean data = new ClubBean();
    List<ClubListBean.ItemsBean> list = new ArrayList<>();
    ClubAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_club, container, false);
        clubDetail();
        pageNo =1;
        clubList(pageNo);
        binding.swipeRefresh.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
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
        initView();
        return binding.getRoot();
    }

    private void initView() {
        mAdapter = new ClubAdapter(list, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerViewScroller(linearLayoutManager, mAdapter) {
            @Override
            public void onLoadMore() {
                pageNo++;
                clubList(pageNo);
            }
        });
        recordUtils.setOnAudioUpdateListener(new AudioRecordUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                int dp = (int) db;
                if (dp <= 40) {
                    iv_state.getDrawable().setLevel(0);
                } else if (dp <= 55 && dp > 40) {
                    iv_state.getDrawable().setLevel(1);
                } else if (dp <= 70 && dp > 55) {
                    iv_state.getDrawable().setLevel(2);
                } else if (dp <= 85 && dp > 70) {
                    iv_state.getDrawable().setLevel(3);
                } else if (dp <= 100 && dp > 85) {
                    iv_state.getDrawable().setLevel(4);
                } else if (dp > 100) {
                    iv_state.getDrawable().setLevel(5);
                }
                tv_voice.setText(Utils.getTime(time));
            }

            @Override
            public void onStop(String filePath) {
                float length = startY - endY;
                if (length < 80) {
                    uploadAudio(filePath);
                } else {
                    Utils.showToast("录音已取消!");
                }
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
                        startY = event.getY();
                        popupWindow.showAtLocation(binding.recyclerView, Gravity.CENTER, 0, 0);
                        recordUtils.startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        endY = event.getY();
                        recordUtils.stopRecord();
                        recordUtils.cancelRecord();
                        popupWindow.dismiss();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        endY = 0;
                        recordUtils.stopRecord();
                        recordUtils.cancelRecord();
                        popupWindow.dismiss();
                        break;
                }
                return true;
            }
        });
        binding.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == data) {
                    return;
                }
                Intent i = ClubDetailActivity.intentFor(mActivity, data.getMid() + "");
                startActivity(i);
            }
        });

    }

    /*上传语音*/
    public void uploadAudio(String fileName) {
        DialogUtils.showLoading(mActivity, "语音上传中...");
        File file = new File(fileName);
        String newFileName = System.currentTimeMillis() / 1000 + ".amr";
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "platform/index")
                .params(HttpUrls.uploadAudio(Settings.getUserProfile().getMid(), Settings.getUserProfile().getDoctor_team_id() + ""))
                .addFile("file", newFileName, file).build().execute(new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                DialogUtils.hideLoading(mActivity);
            }

            @Override
            public void onResponse(String response, int id) {
                DialogUtils.hideLoading(mActivity);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    if (code == 1) {
                        Toast.makeText(mActivity, "语音上传成功!", Toast.LENGTH_LONG).show();
                    } else if (code == -99) {
                        BusProvider.getInstance().postEvent(new TokenOutEvent(code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initInfo(ClubBean data) {
        binding.tvAddress.setText(data.getAddress());
        binding.tvName.setText(data.getDoctor_name() + "医生");
        binding.tvClubName.setText(data.getName());
        binding.tvPhone.setText(data.getUsername());
        Glide.with(mActivity).load(data.getDoctor_photo()).fitCenter().into(binding.ivAvatar);
        Glide.with(mActivity).load(data.getPhoto()).fitCenter().into(binding.ivMain);
    }

    private void clubDetail() {
        api.getBaseUrl(HttpUrls.clubDetail(Settings.getUserProfile().getClub_id() + "")).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                data = gson.fromJson(response, ClubBean.class);
                initInfo(data);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
            }
        });

    }

    private void clubList(final int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        api.getBaseUrl(HttpUrls.club("", pageNo + "", Settings.getUserProfile().getClub_id() + "")).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                ClubListBean data = gson.fromJson(response, ClubListBean.class);
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
        list = new ArrayList<>();
        clubList(pageNo);
    }

    private void contactClub() {
        startActivity(VideoActivity.class);
    }

}
