package com.hlb.haolaoban.activity.account;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityBindPhone2Binding;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2017/11/6.
 */

public class BindPhoneActivity2 extends BaseActivity {

    ActivityBindPhone2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bind_phone2);
        initView();
    }

    private void initView() {
        binding.titlebar.tvSave.setText("下一步");
        binding.titlebar.tbTitle.setText("修改绑定手机(2/3)");
        binding.titlebar.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.titlebar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.titlebar.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhone();
            }
        });
    }

    public void checkPhone() {
        String phone = binding.etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号码不能为空!");
            return;
        }
        if (!Utils.isMobile(phone)) {
            showToast("请输入正确的手机号码!");
            return;
        }
        Intent i = BindPhoneActivity3.IntentFor(this, binding.etPhone.getText().toString().trim());
        startActivity(i);
    }
}
