package com.hlb.haolaoban.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityUpdatePasswordBinding;
import com.hlb.haolaoban.utils.Settings;

/**
 * Created by heky on 2017/11/3.
 */

public class UpdatePasswordActivity extends BaseActivity {

    ActivityUpdatePasswordBinding binding;

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
}
