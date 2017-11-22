package com.hlb.haolaoban.activity.account;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.MainActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.DrugRemind;
import com.hlb.haolaoban.bean.Userbean;
import com.hlb.haolaoban.databinding.ActivityLoginBinding;
import com.hlb.haolaoban.handler.MsgHandler;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.FinishChatEvent;
import com.hlb.haolaoban.otto.JoinVideoEvent;
import com.hlb.haolaoban.otto.ShowNotificationEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.NotificationUtil;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class LoginActivity extends BaseActivity {
    Gson gson;
    ActivityLoginBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    private final String TAG = this.getClass().getSimpleName();
    WebSocketClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        gson = new GsonBuilder().create();
        initView();
    }

    private void initView() {
        if (null != Hawk.get(Constants.PHONE)) {
            binding.etPhone.setText(Hawk.get(Constants.PHONE) + "");
        }
        if (null != Hawk.get(Constants.PASSWORD)) {
            binding.etPassword.setText(Hawk.get(Constants.PASSWORD) + "");
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        binding.tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetPasswordActivity1.class);
            }
        });
    }

    private void login() {
        DialogUtils.showLoading(mActivity, "正在登录...");
        if (TextUtils.isEmpty(binding.etPhone.getText().toString().trim()) || binding.etPhone.getText().toString().trim().length() < 1) {
            showToast("手机号码不能为空!");
            return;
        }

        if (!Utils.isMobile(binding.etPhone.getText().toString().trim())) {
            showToast("请输入正确的手机号码!");
            return;
        }
        if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim()) || binding.etPassword.getText().toString().trim().length() < 1) {
            showToast("密码不能为空!");
            return;
        }
        api.login(HttpUrls.login(binding.etPhone.getText().toString().trim(), binding.etPassword.getText().toString().trim())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                DialogUtils.hideLoading(mActivity);
                Userbean data = gson.fromJson(response, Userbean.class);
                Hawk.put(Constants.PHONE, binding.etPhone.getText().toString().trim());
                Hawk.put(Constants.PASSWORD, binding.etPassword.getText().toString().trim());
                Hawk.put(Constants.MID, data.getMid());
                Hawk.put(Constants.CLUB_ID, data.getClub_id());
                loginWebClient(data.getMid() + "", data.getClub_id() + "");
                startActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                DialogUtils.hideLoading(mActivity);
            }
        });
    }

    private void saveMsg(String s,String id) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            String data = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data)) {
                jsonObject = new JSONObject(data);
                String drugs = jsonObject.getString("data");
                List<DrugRemind> list = gson.fromJson(drugs, new TypeToken<ArrayList<DrugRemind>>() {
                }.getType());
                MsgHandler.saveMsg(list, id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginWebClient(final String id, String club_id) {
        String url = BuildConfig.BASE_WEBSOCKET_URL + "?mid=" + id + "&club_id=" + club_id;
        client = new WebSocketClient(URI.create(url), new Draft_17(), null, 3000) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("eeee", "连接成功!");
            }

            @Override
            public void onMessage(String s) {
                Log.e(TAG, "onMessage:" + s);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.optInt("nFlag");
                    String type = jsonObject.getString("type");
                    String channel = "";
                    if (code == 1) {
                        if (!TextUtils.isEmpty(type)) {
                            if (type.equals("calling")) {
                                channel = jsonObject.getString("channel");
                            }
                            switch (type) {
                                case "meet":
                                    BusProvider.getInstance().postEvent(new JoinVideoEvent(type));
                                    break;
                                case "refuse":
                                    BusProvider.getInstance().postEvent(new FinishChatEvent("finish"));
                                    break;
                                case "calling":
                                    BusProvider.getInstance().postEvent(new JoinVideoEvent(type, channel));
                                    break;
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        switch (type) {
                            case "1":
                            case "4":
                                NotificationUtil.showNotification("", msg);
                                break;
                            case "2":
                                    if (!TextUtils.isEmpty(jsonObject.getString("data"))){
                                        saveMsg(s,id);
                                    }
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {

            }
        };
        client.connect();
/*        Timer timer = new Timer();
        timer.schedule(timerTask, 10000);*/
    }

  /*  TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            msg.arg1 = 1;
            msg.sendToTarget();
        }
    };*/
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.arg1) {
//                case 1:
//                    if (client != null) {
//                        try {
//                            client.send("");
//                        } catch (WebsocketNotConnectedException e) {
//                        }
//                    }
//                    break;
//
//            }
//        }
//    };
}
