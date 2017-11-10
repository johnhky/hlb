package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityForgetPassword2Binding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.ApiDTO;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.TimeCountUtil;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 2017/11/3.
 */

public class ForgetPasswordActivity2 extends BaseActivity {

    ActivityForgetPassword2Binding binding;
    ApiModule api = Api.of(ApiModule.class);

    public static Intent intentFor(Context context, String phone) {
        Intent i = new Intent();
        i.setClass(context, ForgetPasswordActivity2.class);
        i.putExtra(Constants.PHONE, phone);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password2);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("忘记密码");
        binding.tvPhone.setText(getPhone());
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final TimeCountUtil countUtil = new TimeCountUtil(ForgetPasswordActivity2.this, 60000, 1000, binding.tvGetCheck);
        binding.tvGetCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheck();
                countUtil.start();
            }
        });
        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    private void checkData() {
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etNewPassword.getText().toString().trim();
        if (binding.etCheck.getText().toString().trim().length() == 0) {
            showToast("验证码不能为空!");
            return;
        }
        if (null == password || password.length() < 8) {
            showToast("密码不能为空或密码长度不能小于8位");
            return;
        }
        if (null == confirmPassword || confirmPassword.length() < 8) {
            showToast("密码不能为空或密码长度不能小于8位");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showToast("两次输入的验证码不一致!");
            return;
        }
        updatePassword();
    }

    private void updatePassword() {
        api.noResponse(HttpUrls.forgetPassword(getPhone(), binding.etCheck.getText().toString().trim(),
                binding.etNewPassword.getText().toString().trim(),
                binding.etPassword.getText().toString().trim())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                showToast("密码修改成功,请重新登录!");
                startActivity(LoginActivity.class);
                finish();
            }
        });

    }


    private void getCheck() {
        api.noResponse(HttpUrls.getCheck(getPhone())).enqueue(new Callback<ApiDTO>() {
            @Override
            public void onResponse(retrofit2.Call<ApiDTO> call, Response<ApiDTO> response) {
                binding.tvMsg.setText(response.body().toString());
            }

            @Override
            public void onFailure(retrofit2.Call<ApiDTO> call, Throwable t) {

            }
        });
    }

    public String getPhone() {
        return getIntent().getStringExtra(Constants.PHONE);
    }
}
