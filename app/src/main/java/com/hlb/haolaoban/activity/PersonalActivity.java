package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.UserInfoBean;
import com.hlb.haolaoban.databinding.ActivityPersonalBinding;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2017/10/31.
 */

public class PersonalActivity extends BaseActivity {
    ActivityPersonalBinding binding;

    public static Intent intentFor(Context context, UserInfoBean data) {
        Intent i = new Intent(context, PersonalActivity.class);
        i.putExtra(Constants.USER_PROFILE, data);
        return i;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
        initData(getData());
        initView();
    }

    private void initData(UserInfoBean data) {
        if (null == data) {
            return;
        }
        Glide.with(this).load(data.getPhoto()).into(binding.ivAvater);
        String birthDay = Utils.stampToDate(data.getBirthday() + "000");
        binding.etBirthday.setText(birthDay);
        binding.etName.setText(data.getName());
        String gender = "";
        if (!TextUtils.isEmpty(data.getSex())) {
            if (data.getSex().equals("0")) {
                gender = "女";
            } else {
                gender = "男";
            }
        }
        binding.etGender.setText(gender);
        binding.etAddress.setText(data.getAddress());
        binding.etChildren.setText(data.getChildren().getUsername());
        binding.etTeam.setText(data.getDoctor_team_name());
        binding.etClub.setText(data.getClub_name());
        binding.etServiceTime.setText(Utils.stampToDate(data.getService_out_time() + "000"));
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("个人资料");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private UserInfoBean getData() {
        return (UserInfoBean) getIntent().getSerializableExtra(Constants.USER_PROFILE);
    }

}
