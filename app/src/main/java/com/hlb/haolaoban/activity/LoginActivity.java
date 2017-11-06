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
import com.hlb.haolaoban.bean.UserData;
import com.hlb.haolaoban.databinding.ActivityLoginBinding;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class LoginActivity extends BaseActivity {
    Gson gson;
    ActivityLoginBinding binding;

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
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[username]", binding.etPhone.getText().toString().trim());
        params.put("param[password]", binding.etPassword.getText().toString().trim());
        params.put("param[type]", BuildConfig.USER_TYPE + "");
        params.put("param[device]", "mobile");
        params.put("method", "member.login");
        OkHttpUtils.get().url(HttpUrls.BASE_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                DialogUtils.hideLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                DialogUtils.hideLoading();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    if (code == 1) {
                        Hawk.put(Constants.PHONE, binding.etPhone.getText().toString().trim());
                        Hawk.put(Constants.PASSWORD, binding.etPassword.getText().toString().trim());
                        UserData data = gson.fromJson(response, UserData.class);
                        Settings.setUesrProfile(data.getData());
                        startActivity(MainActivity.class);
                    } else if (code == -99) {
                        MainActivity.getToken();
                    } else {
                        showToast(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
