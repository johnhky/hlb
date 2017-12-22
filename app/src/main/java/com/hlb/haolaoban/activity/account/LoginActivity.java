package com.hlb.haolaoban.activity.account;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.MainActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.UserBean;
import com.hlb.haolaoban.databinding.ActivityLoginBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;

import retrofit2.Call;

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
            DialogUtils.hideLoading(mActivity);
            showToast("手机号码不能为空!");
            return;
        }

        if (!Utils.isMobile(binding.etPhone.getText().toString().trim())) {
            DialogUtils.hideLoading(mActivity);
            showToast("请输入正确的手机号码!");
            return;
        }
        if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim()) || binding.etPassword.getText().toString().trim().length() < 1) {
            DialogUtils.hideLoading(mActivity);
            showToast("密码不能为空!");
            return;
        }
        if (binding.etPassword.getText().toString().trim().length() < 8 || binding.etPassword.getText().toString().trim().length() > 12) {
            DialogUtils.hideLoading(mActivity);
            showToast("密码长度不能低于8位或者高于12位!");
            return;
        }
        api.login(HttpUrls.login(binding.etPhone.getText().toString().trim(), binding.etPassword.getText().toString().trim())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                DialogUtils.hideLoading(mActivity);
                UserBean data = gson.fromJson(response, UserBean.class);
                Hawk.put(Constants.PHONE, binding.etPhone.getText().toString().trim());
                Hawk.put(Constants.PASSWORD, binding.etPassword.getText().toString().trim());
                Hawk.put(Constants.TOKEN, data.getToken());
                Hawk.put(Constants.MID, data.getMid());
                Hawk.put(Constants.CLUB_ID, data.getClub_id());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }
}
