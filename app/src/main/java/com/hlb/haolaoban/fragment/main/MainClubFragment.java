package com.hlb.haolaoban.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
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
import com.hlb.haolaoban.activity.ChatActivity;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.adapter.ClubAdapter;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.databinding.ActivityClubBinding;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_club, container, false);
        binding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
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

    }

    /*上传语音*/
    public void uploadAudio(String fileName) {
        DialogUtils.showLoading(mActivity, "语音上传中...");
        /*String fileName = Environment.getExternalStorageDirectory()+"/Recordings/REC20171113163916.mp3";*/
        File file = new File(fileName);
        String newFileName = System.currentTimeMillis() / 1000 + ".amr";
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "platform/index")
                .params(HttpUrls.uploadPicture(Settings.getUserProfile().getMid()))
                .addFile("file", newFileName, file).build().execute(new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
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


    private void getClub(int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        api.getBaseUrl(HttpUrls.getArticle(pageNo)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                binding.swipeRefresh.setRefreshing(false);
                ArticleBean data = gson.fromJson(response, ArticleBean.class);
                if (!data.getItems().isEmpty()) {
                    mAdapter = new ClubAdapter(data.getItems(), mActivity);
                    binding.recyclerView.setAdapter(mAdapter);
                }
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
        getClub(pageNo);
    }

    private void contactClub() {
        DialogUtils.showConsactClub(mActivity, Settings.getUserProfile().getUsername(), new DialogUtils.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int which) {
                if (which == 1) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+Settings.getUserProfile().getUsername()));
                    startActivity(i);
                }
            }
        });
    }

}
