package com.hlb.haolaoban.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.databinding.ActivityChattingBinding;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.UpdateHomeEvent;
import com.hlb.haolaoban.utils.AudioRecordUtils;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2017/12/13.
 */

public class ChattingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    InputMethodManager imm;
    ActivityChattingBinding binding;
    AudioRecordUtils recordUtils;
    PopupWindow popupWindow;
    ImageView iv_state;
    TextView tv_voice;
    private float startY, endY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_chatting);
        initView();
    }

    private void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

        binding.titlebar.tbTitle.setText(Settings.getUserProfile().getClub_name());
        binding.swipeRefresh.setOnRefreshListener(this);
        binding.ivAudio.setOnClickListener(this);
        binding.btSend.setOnClickListener(this);
        binding.ivKeyword.setOnClickListener(this);
        binding.btSend.setOnClickListener(this);
        binding.ivMore.setOnClickListener(this);
        binding.ivClose.setOnClickListener(this);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    /*uploadAudio(filePath);*/
                } else {
                    Utils.showToast("录音已取消!");
                }
                tv_voice.setText(Utils.getTime(0));
            }
        });
        binding.etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.ivMore.setVisibility(View.GONE);
                    binding.btSend.setVisibility(View.VISIBLE);
                    binding.ivClose.setVisibility(View.GONE);
                    binding.llMore.setVisibility(View.GONE);
                } else {
                    binding.ivMore.setVisibility(View.VISIBLE);
                    binding.btSend.setVisibility(View.GONE);
                }
            }
        });
        binding.btAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        popupWindow.showAtLocation(binding.recyclerView, Gravity.CENTER, 0, 0);
                        recordUtils.startRecord();
                        binding.btAudio.setText("松开 结束");
                        break;
                    case MotionEvent.ACTION_UP:
                        endY = event.getY();
                        recordUtils.stopRecord();
                        recordUtils.cancelRecord();
                        popupWindow.dismiss();
                        binding.btAudio.setText("按住 说话");
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_audio:
                hideAllWhileAudioClose();
                break;
            case R.id.iv_more:
                hideAllWhileNoMore();
                break;
            case R.id.iv_close:
                hideAllWhileMore();
                break;
            case R.id.bt_send:
                binding.etMsg.setText("");
                break;
            case R.id.iv_keyword:
                hideAllWhileAudioOpen();
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    /*点击打开语音发送*/
    private void hideAllWhileAudioOpen() {
        binding.ivAudio.setVisibility(View.VISIBLE);
        binding.llMore.setVisibility(View.GONE);
        binding.ivKeyword.setVisibility(View.GONE);
        binding.etMsg.setVisibility(View.VISIBLE);
        binding.btAudio.setVisibility(View.GONE);
        binding.ivMore.setVisibility(View.VISIBLE);
        binding.ivClose.setVisibility(View.GONE);
        binding.etMsg.setText("");
        hideKeyword();
    }

    /*点击关闭语音发送,回到输入框*/
    private void hideAllWhileAudioClose() {
        binding.ivAudio.setVisibility(View.GONE);
        binding.ivKeyword.setVisibility(View.VISIBLE);
        binding.etMsg.setVisibility(View.GONE);
        binding.btAudio.setVisibility(View.VISIBLE);
        binding.ivMore.setVisibility(View.VISIBLE);
        binding.ivClose.setVisibility(View.GONE);
        binding.llMore.setVisibility(View.GONE);
        binding.etMsg.setText("");
        hideKeyword();
    }

    /*点击打开视频，图片页面*/
    private void hideAllWhileNoMore() {
        binding.etMsg.setText("");
        binding.ivKeyword.setVisibility(View.GONE);
        binding.ivAudio.setVisibility(View.VISIBLE);
        binding.etMsg.setVisibility(View.VISIBLE);
        binding.btAudio.setVisibility(View.GONE);
        binding.llMore.setVisibility(View.VISIBLE);
        binding.ivMore.setVisibility(View.GONE);
        binding.ivClose.setVisibility(View.VISIBLE);
        hideKeyword();
    }

    /*点击关闭视频，图片页面,回到输入框*/
    private void hideAllWhileMore() {
        binding.etMsg.setText("");
        binding.ivClose.setVisibility(View.GONE);
        binding.llMore.setVisibility(View.GONE);
        binding.ivMore.setVisibility(View.VISIBLE);
        binding.ivKeyword.setVisibility(View.GONE);
        binding.ivAudio.setVisibility(View.VISIBLE);
        binding.etMsg.setVisibility(View.VISIBLE);
        binding.btAudio.setVisibility(View.GONE);
        hideKeyword();
    }

    private void hideKeyword() {
        if (null != imm) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().postEvent(new UpdateHomeEvent());
    }
}

