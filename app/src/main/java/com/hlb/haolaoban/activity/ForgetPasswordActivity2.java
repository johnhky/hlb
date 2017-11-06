package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityForgetPassword2Binding;
import com.hlb.haolaoban.module.HttpUrls;
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
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[username]", getPhone());
        params.put("param[smscode]", binding.etCheck.getText().toString().trim());
        params.put("param[new_password]", binding.etPassword.getText().toString().trim());
        params.put("param[confirm_password]", binding.etNewPassword.getText().toString().trim());
        params.put("method", "member.modify.password");
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
                        showToast("密码修改成功,请重新登录!");
                        startActivity(LoginActivity.class);
                        finish();
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


    private void getCheck() {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mobile]", getPhone());
        params.put("method", "public.msm.send");
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
                        binding.tvMsg.setText(response);
                    }/* else if (code == -99) {
                        startActivity(LoginActivity.class);
                        finish();
                    } else {
                        showToast(response);
                    }*/
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
