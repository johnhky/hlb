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
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.TimeCountUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by heky on 2017/11/6.
 */

public class BindPhoneActivity3 extends BaseActivity {

    ActivityBindPhone3Binding binding;

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
        Map<String, String> params = new LinkedHashMap<>();
        params.put("param[mid]", Settings.getUserProfile().getMid() + "");
        params.put("param[mobile]", getPhone());
        params.put("param[smscode]", binding.etCheck.getText().toString().trim());
        params.put("method", "member.modify.mobile");
        params.putAll(Constants.addParams());
        OkHttpUtils.get().url(HttpUrls.BASE_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    if (code == 1) {
                        showToast("修改成功!");
                        startActivity(LoginActivity.class);
                        finish();
                    } else if (code == -99) {
                        showToast("token过期");
                    } else {
                        showToast(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    private String getPhone() {
        return getIntent().getStringExtra(Constants.PHONE);
    }
}
