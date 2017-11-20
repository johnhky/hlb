package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.account.BindPhoneActivity;
import com.hlb.haolaoban.activity.account.UpdatePasswordActivity;
import com.hlb.haolaoban.databinding.ActivityAccountBinding;

/**
 * Created by heky on 2017/11/3.
 */

public class AccountActivity extends BaseActivity {

    ActivityAccountBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        initView();
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("账号安全");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.llChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UpdatePasswordActivity.class);
            }
        });
        binding.llBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BindPhoneActivity.class);
            }
        });
    }

}
