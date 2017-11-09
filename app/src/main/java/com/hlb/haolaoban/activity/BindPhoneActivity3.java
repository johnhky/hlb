package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityBindPhone3Binding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.ApiDTO;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.TimeCountUtil;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 2017/11/6.
 */

public class BindPhoneActivity3 extends BaseActivity {

    ActivityBindPhone3Binding binding;
    ApiModule api = Api.of(ApiModule.class);
    public static Intent IntentFor(Context context, String phone) {
        Intent i = new Intent();
        i.setClass(context, BindPhoneActivity3.class);
        i.putExtra(Constants.PHONE, phone);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bind_phone3);
        initView();
    }

    private void initView() {
        binding.titlebar.tvSave.setText("提交");
        binding.titlebar.tbTitle.setText("绑定手机号码(3/3)");
        String phone = getPhone().substring(0, 3) + "****" + getPhone().substring(7, 11);
        String text = "点击按钮后,短信验证码将发送到:\n+86" + phone + ",请在3分钟内输入验证码";
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#76C5F2"));
        spannableString.setSpan(foregroundColorSpan, 16, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvPhone.setText(spannableString);
        final TimeCountUtil countUtil = new TimeCountUtil(BindPhoneActivity3.this, 60000, 1000, binding.btSubmit);
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
        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUtil.start();
            }
        });
    }

    private void check() {
        String check = binding.etCheck.getText().toString().trim();
        if (null == check) {
            showToast("验证码不能为空!");
            return;
        }
        bindPhone();
    }

    private void bindPhone() {
        api.noResponse(HttpUrls.bindPhone(getPhone(),binding.etCheck.getText().toString().trim())).enqueue(new Callback<ApiDTO>() {
            @Override
            public void onResponse(retrofit2.Call<ApiDTO> call, Response<ApiDTO> response) {
                showToast("修改成功!");
                startActivity(LoginActivity.class);
                finish();
            }

            @Override
            public void onFailure(retrofit2.Call<ApiDTO> call, Throwable t) {

            }
        });

    }


    private String getPhone() {
        return getIntent().getStringExtra(Constants.PHONE);
    }
}
