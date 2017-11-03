package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityForgetPassword2Binding;
import com.hlb.haolaoban.module.LoginModule;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.TimeCountUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by heky on 2017/11/3.
 */

public class ForgetPasswordActivity2 extends BaseActivity {

    ActivityForgetPassword2Binding binding;


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
        binding.tvGetCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheck();
                TimeCountUtil countUtil = new TimeCountUtil(ForgetPasswordActivity2.this, 60000, 1000, binding.tvGetCheck);
                countUtil.start();
            }
        });
    }

    private void getCheck() {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mobile]", getPhone());
        OkHttpUtils.get().url(LoginModule.BASE_URL).params(params).build().execute(new StringCallback() {
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
                        binding.tvMsg.setText(response);
                    } else if (code == -99) {
                        startActivity(LoginActivity.class);
                        finish();
                    } else {
                        showToast(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getPhone() {
        return getIntent().getStringExtra(Constants.PHONE);
    }
}
