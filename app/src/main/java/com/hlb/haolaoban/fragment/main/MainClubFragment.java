package com.hlb.haolaoban.fragment.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.adapter.ClubAdapter;
import com.hlb.haolaoban.bean.ArticleBean;
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
    private ClubAdapter mAdapter;
    private int pageNo = 1;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    AudioRecordUtils recordUtils;
    PopupWindow popupWindow;
    ImageView iv_state;
    TextView tv_voice;
    LinearLayoutManager linearLayoutManager;
    private float startY, endY;
    List<ArticleBean.ItemsBean> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_club, container, false);
        binding.swipeRefresh.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
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
        onRefresh();
        return binding.getRoot();
    }

    private void initView() {
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
                }
                return true;
            }
        });
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !ViewCompat.canScrollVertically(recyclerView, 1)) {
                    pageNo++;
                    getClub(pageNo);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mAdapter = new ClubAdapter(datas, mActivity);
        binding.recyclerView.setAdapter(mAdapter);
    }

    /*上传语音*/
    public void uploadAudio(String fileName) {
        DialogUtils.showLoading(mActivity, "语音上传中...");
        /*String fileName = Environment.getExternalStorageDirectory()+"/Recordings/REC20171113163916.mp3";*/
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


    private void getClub(final int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        api.getBaseUrl(HttpUrls.getArticle(pageNo)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                ArticleBean data = gson.fromJson(response, ArticleBean.class);
                if (!data.getItems().isEmpty()) {
                    datas.addAll(data.getItems());
                } else {
                    if (pageNo > 1) {
                        Utils.showToast("暂时没有更多数据了");
                    }
                }
                mAdapter.update(datas);
                mAdapter.notifyDataSetChanged();
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
        datas = new ArrayList<>();
        getClub(1);
    }

    private void contactClub() {
        DialogUtils.showConsactClub(mActivity, Settings.getUserProfile().getClub_name(), new DialogUtils.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int which) {
                if (which == 1) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + Settings.getUserProfile().getClub_username()));
                    startActivity(i);
                }
            }
        });
    }

}
