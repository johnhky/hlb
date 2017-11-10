package com.hlb.haolaoban.activity.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityAboutBinding;

/**
 * Created by heky on 2017/11/1.
 */

public class AboutActivity extends BaseActivity {

    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("关于好老伴");
        String version = "好老伴  " + BuildConfig.VERSION_NAME;
        binding.setData(version);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
