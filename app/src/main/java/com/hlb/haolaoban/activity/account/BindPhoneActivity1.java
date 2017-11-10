package com.hlb.haolaoban.activity.account;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityBindPhone1Binding;
import com.hlb.haolaoban.utils.Constants;
import com.orhanobut.hawk.Hawk;

/**
 * Created by heky on 2017/11/6.
 */

public class BindPhoneActivity1 extends BaseActivity {

    ActivityBindPhone1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bind_phone1);
        initView();
    }

    private void initView() {
        binding.titlebar.tvSave.setText("下一步");
        binding.titlebar.tbTitle.setText("修改绑定手机(1/3)");
        binding.titlebar.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetPasswordActivity2.class);
            }
        });
        binding.titlebar.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    public void checkPassword() {
        String password = binding.etPassword.getText().toString().trim();
        if (null == password) {
            showToast("密码不能为空!");
            return;
        }
        if (password.length() < 8 && password.length() > 12) {
            showToast("密码长度不能低于8位和高于12位!");
            return;
        }
        if (!password.equals(Hawk.get(Constants.PASSWORD))) {
            showToast("密码不正确!");
            return;
        }
        startActivity(BindPhoneActivity2.class);
    }
}
