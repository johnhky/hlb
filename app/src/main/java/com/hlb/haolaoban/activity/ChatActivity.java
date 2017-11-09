package com.hlb.haolaoban.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityChatBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Settings;


import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 2017/11/3.
 */

public class ChatActivity extends BaseActivity {
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;

    private RtcEngine mRtcEngine;
    ActivityChatBinding binding;
    ApiModule api = Api.of(ApiModule.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {
            /*calling();*/
            initAgoraEngineAndJoinChannel();
        }
        initView();
    }

    private void initView() {
        binding.tvName.setText(Settings.getUserProfile().getUsername());
        binding.tvStatus.setText("通话连接中...");
        binding.tvHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        binding.tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.chxVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.chxVoice.setTextColor(getResources().getColor(R.color.main_title_bg));
                } else {
                    binding.chxVoice.setTextColor(getResources().getColor(R.color.main_bg));
                }
                mRtcEngine.muteLocalAudioStream(binding.chxVoice.isSelected());
            }
        });
    }

    private void sendNotification() {
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.logo)
                //设置通知标题
                .setContentTitle("视频通话中")
                //设置通知内容
                .setContentText("与" + Settings.getUserProfile().getUsername() + "通话中");
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA);
                } else {
                    showToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    finish();
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showToast("No permission for " + Manifest.permission.CAMERA);
                    finish();
                }
                break;
            }
        }
    }


    private void initAgoraEngineAndJoinChannel() {
        //在视频连接之前  请求服务器接口,判断对方是否未接听  挂断
        initializeAgoraEngine();     // Tutorial Step 1
        setupVideoProfile();         // Tutorial Step 2
        setupLocalVideo();           // Tutorial Step 3
        joinChannel();               // Tutorial Step 4
        calling();
    }

    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), BuildConfig.APPKEY, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    // Tutorial Step 2
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }

    // Tutorial Step 3
    private void setupLocalVideo() {
        /*FrameLayout container = (FrameLayout) findViewById(R.id.fl_video);*/
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
/*        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.NO_GRAVITY);
        params.setMargins(5, 5, 5, 5);
        surfaceView.setLayoutParams(params);*/
        surfaceView.setZOrderMediaOverlay(true);
        binding.flMineVideo.addView(surfaceView);
        /*RENDER_MODE_ADAPTIVE*/
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    // Tutorial Step 4
    private void joinChannel() {
        mRtcEngine.joinChannel(null, "simpleDemo", "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {
        if (binding.flYourVideo.getChildCount() >= 1) {
            return;
        }
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        binding.flYourVideo.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

        surfaceView.setTag(uid); // for mark purpose
       /* View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk); // optional UI*/
        binding.tvStatus.setVisibility(View.GONE);
    }

    // Tutorial Step 6
    private void leaveChannel() {
        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
    }

    // Tutorial Step 7
    private void onRemoteUserLeft() {
        /*FrameLayout container = (FrameLayout) findViewById(R.id.fl_video);*/
        binding.flYourVideo.removeAllViews();

      /*  View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk); // optional UI*/
        binding.tvStatus.setVisibility(View.VISIBLE);
    }

    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
 /*       FrameLayout container = (FrameLayout) findViewById(R.id.fl_video);
*/
        SurfaceView surfaceView = (SurfaceView) binding.flYourVideo.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) { // Tutorial Step 5
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) { // Tutorial Step 7
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) { // Tutorial Step 10
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoMuted(uid, muted);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    /*发起视频通话请求*/
    private void calling() {
        api.video(HttpUrls.startVideo()).enqueue(new Callback() {
            @Override
            public void onResponse(retrofit2.Call call, Response response) {

            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {

            }
        });
/*        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "video.calling");
        params.put("call", Settings.getUserProfile().getMid() + "");
        params.put("token", Hawk.get(com.hlb.haolaoban.utils.Constants.TOKEN) + "");
        params.put("answers[]", Settings.getUserProfile().getClub_id() + "");
        OkHttpUtils.post().url(HttpUrls.BASE_VIDEO_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("nFlag");
                    if (code == 1) {
                        String channel = jsonObject.getString("channel");
                    } else {
                        showToast(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });*/
    }


}
