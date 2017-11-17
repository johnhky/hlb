package com.hlb.haolaoban.activity.device;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityDeviceManageBinding;

/**
 * Created by heky on 2017/11/3.
 */

public class DeviceManageActivity extends BaseActivity {
    ActivityDeviceManageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_manage);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("设备管理");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvWristbands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(WristbandsActivity.class);
            }
        });
    }


}
