package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.databinding.ActivityBigimageBinding;
import com.hlb.haolaoban.utils.Constants;

/**
 * Created by heky on 2017/11/9.
 */

public class BigImageActivity extends BaseActivity {

    ActivityBigimageBinding binding;

    public static Intent intentFor(Context context, String photo) {
        Intent i = new Intent(context, BigImageActivity.class);
        i.putExtra(Constants.IMAGE, photo);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bigimage);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("");
        Glide.with(this).load(getImage()).into(binding.ivImage);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getImage() {
        return getIntent().getStringExtra(Constants.IMAGE);
    }
}
