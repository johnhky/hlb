package com.hlb.haolaoban.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityChatBinding;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.FinishChatEvent;
import com.hlb.haolaoban.otto.JoinVideoEvent;
import com.hlb.haolaoban.otto.TokenOutEvent;
import com.hlb.haolaoban.utils.Settings;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import okhttp3.Call;

/**
 * Created by heky on 2017/11/3.
 */

public class ChatActivity extends BaseActivity {
    private String TAG = "eeee";
    private RtcEngine mRtcEngine;
    ActivityChatBinding binding;
    private String channel;
    private Timer timer = null;//计时器
    private TimerTask timerTask;
    private int callNoResponse = 40000;

    public static Intent intentFor(Context context, String channel) {
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra(com.hlb.haolaoban.utils.Constants.CHANNEL, channel);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        if (!TextUtils.isEmpty(getChannel())) {
            acceptVideo(getChannel());
        } else {
            calling();
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
                disconnectVideo(channel, 2);
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
                if (null != mRtcEngine) {
                    mRtcEngine.muteLocalAudioStream(binding.chxVoice.isSelected());
                }
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

    /*初始化远程视频窗口*/
    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(mActivity, BuildConfig.APPKEY, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    /*检测是否拥有语音 视频权限*/
    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    /*设置视频属性*/
    // Tutorial Step 2
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }

    /*初始化本地视频视图*/
    // Tutorial Step 3
    private void setupLocalVideo() {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        binding.flMineVideo.addView(surfaceView);
        /*RENDER_MODE_ADAPTIVE*/
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    /*加入频道*/
    // Tutorial Step 4
    private void joinChannel(String channel) {
        mRtcEngine.joinChannel(null, channel, null, 0); // if you do not specify the uid, we will generate the uid for you
    }

    /*初始化远程视频视图*/
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

    /*离开该频道*/
    // Tutorial Step 6
    private void leaveChannel() {
        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
    }

    /*监听到远程用户离开房间时的操作*/
    // Tutorial Step 7
    private void onRemoteUserLeft() {
        binding.flYourVideo.removeAllViews();
        binding.tvStatus.setVisibility(View.VISIBLE);
        BusProvider.getInstance().postEvent(new FinishChatEvent("finish"));
    }

    /*判断是否显示远程视频*/
    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        SurfaceView surfaceView = (SurfaceView) binding.flYourVideo.getChildAt(0);
        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }

    /*视频监听  */
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
        public void onLastmileQuality(int quality) {
            super.onLastmileQuality(quality);
            if (quality > 2) {
                showToast("当前网络连接不稳定!");
            }
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
        startTime();
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "videochat/index").params(HttpUrls.startVideo()).build().execute(new StringCallback() {
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
                    } else if (code == -99) {
                        BusProvider.getInstance().postEvent(new TokenOutEvent(code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /*断开连接时通知服务器
     拒绝视频通话
    * @params mode 参数
    *  1  应答者在呼叫时直接挂断
    *  2  进⼊视频会话后主动退出
    *  3  呼叫超时
    *  4  异常
    *  5  发起者在呼叫阶段主动取消
     *  */
    public void disconnectVideo(String channel, int mode) {
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "videochat/index").params(HttpUrls.rejectVideo(Settings.getUserProfile().getMid() + "", mode, channel)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                BusProvider.getInstance().postEvent(new FinishChatEvent("finish"));
                Log.e(TAG, response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("nFlag");
                    if (code == 1) {
                        stopTime();
                    } else if (code == -99) {
                        BusProvider.getInstance().postEvent(new TokenOutEvent(code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /*接受视频通话*/
    private void acceptVideo(final String channel) {
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "videochat/index").params(HttpUrls.acceptVideo(Settings.getUserProfile().getMid() + "", channel)).build().execute(new StringCallback() {
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
                        startChat(channel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*开始视频通话*/
    private void startChat(String channel) {
        binding.tvStatus.setText("正在通话中...");
        initializeAgoraEngine();     // Tutorial Step 1
        setupVideoProfile();         // Tutorial Step 2
        setupLocalVideo();           // Tutorial Step 3
        joinChannel(channel);
    }

    @Subscribe
    public void onReciveEvent(JoinVideoEvent event) {
        if (event.getType().equals("meet")) {
            stopTime();
            startChat(channel);

        }
    }

    @Subscribe
    public void onReciveEvent(FinishChatEvent event) {
        switch (event.getType()) {
            case "finish":
                finish();
                break;
        }


    }

    /*开始计时*/
    private void startTime() {
        if (timer == null) {
            timer = new Timer();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                int count = callNoResponse - 1000;
                Message msg = handler.obtainMessage();
                msg.what = count;
            }
        };
        timerTask.run();
    }

    /*停止计时*/
    private void stopTime() {
        if (timer != null) {
            timer.cancel();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startTime();
            if (msg.what == 0) {
                disconnectVideo(channel, 5);
            }
        }
    };


    private String getChannel() {
        return getIntent().getStringExtra(com.hlb.haolaoban.utils.Constants.CHANNEL);
    }

}
