package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.MainActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.TokenBean;
import com.hlb.haolaoban.bean.Userbean;
import com.hlb.haolaoban.databinding.ActivityLoginBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.ApiDTO;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 2017/10/31.
 */

public class LoginActivity extends BaseActivity {
    Gson gson;
    ActivityLoginBinding binding;
    ApiModule api = Api.of(ApiModule.class);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login, null);
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
        DialogUtils.showLoading(LoginActivity.this, "正在登录...");
        if (null == binding.etPhone.getText().toString().trim() && binding.etPhone.getText().toString().trim().length() < 1) {
            showToast("手机号码不能为空!");
            return;
        }
        if (!Utils.isMobile(binding.etPhone.getText().toString().trim())) {
            showToast("请输入正确的手机号码!");
            return;
        }
        if (null == binding.etPassword.getText().toString().trim() && binding.etPassword.getText().toString().trim().length() < 1) {
            showToast("密码不能为空!");
            return;
        }
        api.getToken(HttpUrls.getToken()).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                DialogUtils.hideLoading();
                if (null != Hawk.get(Constants.TOKEN)) {
                    Hawk.delete(Constants.TOKEN);
                }
                TokenBean tokenBean = gson.fromJson(response, TokenBean.class);
                Hawk.put(Constants.TOKEN, tokenBean.getToken());
                Hawk.put(Constants.TOKENOUT, tokenBean.getTokenout());
                api.login(HttpUrls.login(binding.etPhone.getText().toString().trim(), binding.etPassword.getText().toString().trim())).enqueue(new SimpleCallback() {
                    @Override
                    protected void handleResponse(String response) {
                        Userbean data = gson.fromJson(response, Userbean.class);
                        DialogUtils.hideLoading();
                        Hawk.put(Constants.PHONE, binding.etPhone.getText().toString().trim());
                        Hawk.put(Constants.PASSWORD, binding.etPassword.getText().toString().trim());
                        loginWebSocket(data.getMid() + "", data.getClub_id());
                        Settings.setUesrProfile(data);
                        startActivity(MainActivity.class);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        super.onFailure(call, t);
                        DialogUtils.hideLoading();
                    }
                });
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                DialogUtils.hideLoading();
            }
        });

    }

    private void loginWebSocket(String id, int club_id) {
        String url = BuildConfig.BASE_WEBSOCKET_URL + "mid=" + id + "&club_id=" + club_id;
        final WebSocketClient myWebSocket = new WebSocketClient(URI.create(url), new Draft_17(), null, 3000) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("eeee", "链接成功!");
            }

            @Override
            public void onMessage(String s) {
                Log.e("eeee", s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {

            }
        };
        myWebSocket.connect();

    }

}
