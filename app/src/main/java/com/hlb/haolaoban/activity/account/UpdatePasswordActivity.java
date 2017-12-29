package com.hlb.haolaoban.activity.account;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityUpdatePasswordBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;
import com.orhanobut.hawk.Hawk;

/**
 * Created by heky on 2017/11/3.
 */

public class UpdatePasswordActivity extends BaseActivity {

    ActivityUpdatePasswordBinding binding;
    ApiModule api = Api.of(ApiModule.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_password);
        intiView();
    }

    private void intiView() {
        binding.titlebar.tbTitle.setText("修改密码");
        binding.titlebar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.titlebar.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.titlebar.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        binding.tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ForgetPasswordActivity2.intentFor(UpdatePasswordActivity.this, Settings.getUserProfile().getUsername());
                startActivity(i);
            }
        });
    }

    private void check() {
        String oldPassword = binding.etOldPassword.getText().toString().trim();
        String loginPassword = Hawk.get(Constants.PASSWORD);
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfrimPassword.getText().toString().trim();
        if (null == oldPassword) {
            showToast("密码不能为空!");
            return;
        }
        if (oldPassword.length() < 8 || oldPassword.length() > 20) {
            showToast("密码长度不能低于8位和大于12位!");
            return;
        }
        if (!oldPassword.equals(loginPassword)) {
            showToast("原密码不正确!");
            return;
        }
        if (null == password) {
            showToast("新密码不能为空!");
            return;
        }
        if (password.length() < 8 || password.length() > 20) {
            showToast("密码长度不能低于8位和大于12位!");
            return;
        }
        if (null == confirmPassword) {
            showToast("新密码不能为空!");
            return;
        }
        if (confirmPassword.length() < 8 || confirmPassword.length() > 20) {
            showToast("密码长度不能低于8位和大于12位!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showToast("两次输入的密码不一致!");
            return;
        }
        updatePassword();
    }

    private void updatePassword() {
        api.noResponse(HttpUrls.updatePassword(Settings.getUserProfile().getUsername(),
                binding.etOldPassword.getText().toString().trim(),
                binding.etPassword.getText().toString().trim(),
                binding.etConfrimPassword.getText().toString().trim())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                showToast("密码修改成功!");
                startActivity(LoginActivity.class);
                finish();
            }
        });

    }


}
