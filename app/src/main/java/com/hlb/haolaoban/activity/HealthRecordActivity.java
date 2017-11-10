package com.hlb.haolaoban.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityHealthRecordBinding;

/**
 * Created by heky on 2017/11/2.
 */

public class HealthRecordActivity extends BaseActivity {
    ActivityHealthRecordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_record);
        binding.titlebar.tbTitle.setText("健康档案");
        initView();
    }

    private void initView() {
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MedicalRecordActivity.class);
            }
        });
        binding.tvMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MedicalCountActivity.class);
            }
        });
        binding.tvMedicalRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = PrescriptionActivity.intentFor(HealthRecordActivity.this, 0);
                startActivity(i);
            }
        });
    }
}
