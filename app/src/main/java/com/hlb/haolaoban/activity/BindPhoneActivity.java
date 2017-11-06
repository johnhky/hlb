package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityBindPhoneBinding;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2017/11/3.
 */

public class BindPhoneActivity extends BaseActivity {

    ActivityBindPhoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bind_phone);
        intiView();
    }

    private void intiView() {
        binding.titlebar.tbTitle.setText("手机绑定");
        String phone = Settings.getUserProfile().getUsername();
        binding.setData("绑定手机:" + Utils.phoneToAsterisk(phone));
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BindPhoneActivity1.class);
            }
        });
    }
}
