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
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.TokenOutEvent;
import com.hlb.haolaoban.utils.Settings;
import com.squareup.otto.Bus;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import okhttp3.Call;

/**
 * Created by heky on 2017/11/3.
 */

public class ChatActivity extends BaseActivity {
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    private String TAG = "eeee";
    private RtcEngine mRtcEngine;
    ActivityChatBinding binding;
    private String channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {
            initAgoraEngineAndJoinChannel();
        }
        initView();
    }

    private void initView() {
        binding.tvName.setText(Settings.getUserProfile().getClub_name());
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
                disconnectVideo(2);
            }
        });
        binding.tvFinish.setKeepScreenOn(true);
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
                .setContentText("与" + Settings.getUserProfile().getClub_name() + "通话中");
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
        calling();
        // Tutorial Step 4
    }

    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(mActivity, BuildConfig.APPKEY, mRtcEventHandler);
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
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        binding.flMineVideo.addView(surfaceView);
        /*RENDER_MODE_ADAPTIVE*/
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    // Tutorial Step 4
    private void joinChannel(String channel) {
        mRtcEngine.joinChannel(null, channel, null, 0); // if you do not specify the uid, we will generate the uid for you
    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {
        if (binding.flYourVideo.getChildCount() >= 1) {
            return;
        }
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        binding.flYourVideo.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        surfaceView.setTag(uid); // for mark purpose
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
        binding.flYourVideo.removeAllViews();
        binding.tvStatus.setVisibility(View.VISIBLE);
    }

    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        SurfaceView surfaceView = (SurfaceView) binding.flYourVideo.getChildAt(0);
        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) { // Tutorial Step 5
            Log.e(TAG, uid + " , " + elapsed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            Log.e(TAG, err + "error");
        }

        @Override
        public void onUserOffline(int uid, final int reason) { // Tutorial Step 7
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, reason + " 对方断开连接");
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
            super.onFirstRemoteVideoFrame(uid, width, height, elapsed);

        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            Log.e(TAG, "对方加入:" + uid + "  ,  " + elapsed);
        }

        @Override
        public void onUserEnableVideo(int uid, boolean enabled) {
            super.onUserEnableVideo(uid, enabled);
            onRemoteUserVideoMuted(uid, enabled);
        }

        @Override
        public void onUserMuteVideo(int uid, boolean muted) {
            super.onUserMuteVideo(uid, muted);
            onRemoteUserVideoMuted(uid, muted);
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
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL).params(HttpUrls.startVideo()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("nFlag");
                    if (code == 1) {
                        channel = jsonObject.getString("channel");
                        initializeAgoraEngine();     // Tutorial Step 1
                        setupVideoProfile();         // Tutorial Step 2
                        setupLocalVideo();           // Tutorial Step 3
                        joinChannel(channel);
                    } else if (code == -99) {
                        BusProvider.getInstance().postEvent(new TokenOutEvent(code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void disconnectVideo(int mode) {
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL).params(HttpUrls.rejectVideo(Settings.getUserProfile().getMid() + "", mode, channel)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("nFlag");
                    if (code == 1) {
                        finish();
                    } else if (code == -99) {
                        BusProvider.getInstance().postEvent(new TokenOutEvent(code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
