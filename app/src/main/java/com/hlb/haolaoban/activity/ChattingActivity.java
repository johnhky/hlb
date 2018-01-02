package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.adapter.ConsultAdapter;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.ConsultBean;
import com.hlb.haolaoban.bean.UnReadMsgEvent;
import com.hlb.haolaoban.databinding.ActivityChattingBinding;
import com.hlb.haolaoban.handler.MsgHandler;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.RefreshMsgList;
import com.hlb.haolaoban.otto.UpdateHomeEvent;
import com.hlb.haolaoban.utils.AudioRecordUtils;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.NetworkUtils;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

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
    private final int CAMERA_REQUEST = 2;
    private final int PHOTO_REQUEST = 3;
    Gson gson = new GsonBuilder().create();
    private int pageNo = 1;
    ConsultAdapter mAdapter;
    List<ConsultBean> consultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.isRead = true;
        BusProvider.getInstance().postEvent(new UnReadMsgEvent());
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_chatting);
        initView();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ConsultAdapter(ChattingActivity.this, consultList);
        binding.recyclerView.setAdapter(mAdapter);
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
        binding.ivPhoto.setOnClickListener(this);
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
                    MsgHandler.sendAudio(filePath);
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
                        binding.btAudio.setText("按住 说话");
                        popupWindow.dismiss();
                        break;
                }
                return true;
            }
        });
        binding.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyword();
                return false;
            }
        });
        pageNo = 1;
        messageList(pageNo);
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
                if (!NetworkUtils.isAvailable(mActivity)) {
                    Utils.showToast("网络连接已断开,请检查您的网络情况!");
                    return;
                }
                MsgHandler.sendText(binding.etMsg.getText().toString());
                binding.etMsg.setText("");
                break;
            case R.id.iv_keyword:
                hideAllWhileAudioOpen();
                break;
            case R.id.iv_photo:
                DialogUtils.showPickPhotoDialog(binding.recyclerView, mActivity, new DialogUtils.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int which) {
                        switch (which) {
                            case 1:
                                takePicture();
                                break;
                            case 2:
                                choosePicture();
                                break;
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageNo++;
        messageList(pageNo);
    }

    private void messageList(final int pageNo) {
        binding.swipeRefresh.setRefreshing(true);
        OkHttpUtils.post().url(BuildConfig.BASE_VIDEO_URL + "platform/index").params(HttpUrls.msgList(pageNo)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(String response, int id) {
                binding.swipeRefresh.setRefreshing(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    if (code == 1) {
                        List<ConsultBean> list = gson.fromJson(jsonObject.getString("data"), new TypeToken<ArrayList<ConsultBean>>() {
                        }.getType());
                        if (!list.isEmpty()) {
                            if (pageNo == 1) {
                                consultList.addAll(list);
                                binding.recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                            } else {
                                for (int i = 0; i < list.size(); i++) {
                                    consultList.add(0, list.get(i));
                                }
                                binding.recyclerView.scrollToPosition(0);
                            }
                        }
                        mAdapter.update(consultList);
                    } else if (code == 0) {
                        String reason = jsonObject.getString("data");
                        Utils.showToast(reason);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendPicture(Bitmap bitmap) {
        MsgHandler.sendImage(bitmap);
    }

    public void takePicture() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, CAMERA_REQUEST);
    }

    /**
     * 从相册获取
     */
    public void choosePicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    if (data.getExtras() != null) {
                        if (null != data.getExtras().get("data")) {
                            Bitmap bm = (Bitmap) data.getExtras().get("data");
                            sendPicture(bm);
                        }
                    }
                    break;
                case PHOTO_REQUEST:
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        if (null != uri) {
                            try {
                                Bitmap bm = Utils.getBitmapFormUri(mActivity, uri);
                                sendPicture(bm);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    }

    /*点击关闭语音发送,回到输入框*/
    private void hideAllWhileAudioOpen() {
        binding.etMsg.setText("");
        binding.btAudio.setVisibility(View.GONE);
        binding.etMsg.setVisibility(View.VISIBLE);
        binding.ivAudio.setVisibility(View.VISIBLE);
        binding.ivClose.setVisibility(View.GONE);
        binding.ivKeyword.setVisibility(View.GONE);
        binding.ivMore.setVisibility(View.VISIBLE);
        binding.llMore.setVisibility(View.GONE);
        hideKeyword();
    }

    /*点击打开语音发送*/
    private void hideAllWhileAudioClose() {
        binding.etMsg.setText("");
        binding.btAudio.setVisibility(View.VISIBLE);
        binding.etMsg.setVisibility(View.GONE);
        binding.ivAudio.setVisibility(View.GONE);
        binding.ivClose.setVisibility(View.GONE);
        binding.ivKeyword.setVisibility(View.VISIBLE);
        binding.ivMore.setVisibility(View.VISIBLE);
        binding.llMore.setVisibility(View.GONE);
        hideKeyword();
    }

    /*点击打开视频，图片页面*/
    private void hideAllWhileNoMore() {
        binding.etMsg.setText("");
        binding.btAudio.setVisibility(View.GONE);
        binding.ivClose.setVisibility(View.VISIBLE);
        binding.etMsg.setVisibility(View.VISIBLE);
        binding.ivAudio.setVisibility(View.VISIBLE);
        binding.ivKeyword.setVisibility(View.GONE);
        binding.ivMore.setVisibility(View.GONE);
        binding.llMore.setVisibility(View.VISIBLE);
        hideKeyword();
    }

    /*点击关闭视频，图片页面,回到输入框*/
    private void hideAllWhileMore() {
        binding.etMsg.setText("");
        binding.btAudio.setVisibility(View.GONE);
        binding.etMsg.setVisibility(View.VISIBLE);
        binding.ivAudio.setVisibility(View.VISIBLE);
        binding.ivClose.setVisibility(View.GONE);
        binding.ivMore.setVisibility(View.VISIBLE);
        binding.ivKeyword.setVisibility(View.GONE);
        binding.llMore.setVisibility(View.GONE);
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
        Constants.isRead = false;
        BusProvider.getInstance().postEvent(new UpdateHomeEvent());
    }

    @Subscribe
    public void onReceiveEvent(RefreshMsgList event) {
        pageNo = 1;
        messageList(pageNo);
    }
}

