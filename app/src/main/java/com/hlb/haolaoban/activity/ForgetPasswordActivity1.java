package com.hlb.haolaoban.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityForgetPassword1Binding;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2017/11/3.
 */

public class ForgetPasswordActivity1 extends BaseActivity {
    ActivityForgetPassword1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password1);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("忘记密码");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    private void next() {
        if (null == binding.etPhone.getText().toString().trim()) {
            showToast("手机号码不能为空!");
            return;
        }
        if (!Utils.isMobile(binding.etPhone.getText().toString().trim())) {
            showToast("手机号码格式不正确!");
            return;
        }
        Intent i = ForgetPasswordActivity2.intentFor(this, binding.etPhone.getText().toString().trim());
        startActivity(i);
    }
}
