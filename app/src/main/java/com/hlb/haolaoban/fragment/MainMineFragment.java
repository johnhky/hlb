package com.hlb.haolaoban.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.AboutActivity;
import com.hlb.haolaoban.activity.AccountActivity;
import com.hlb.haolaoban.activity.FeedBackActivity;
import com.hlb.haolaoban.activity.HealthRecordActivity;
import com.hlb.haolaoban.activity.HelpCenterActivity;
import com.hlb.haolaoban.activity.PersonalActivity;
import com.hlb.haolaoban.databinding.ActivityMineBinding;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2017/10/31.
 */

public class MainMineFragment extends BaseFragment {
    ActivityMineBinding binding;
    Gson gson = new GsonBuilder().create();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_mine, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AboutActivity.class);
            }
        });
        binding.tvPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PersonalActivity.class);
            }
        });
        binding.tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showExitDialog(v.getContext());
            }
        });
        binding.tvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanCache(v.getContext());
            }
        });
        binding.tvHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HealthRecordActivity.class);
            }
        });
        binding.tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AccountActivity.class);
            }
        });
        binding.tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HelpCenterActivity.class);
            }
        });
        binding.tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FeedBackActivity.class);
            }
        });
    }

    private void cleanCache(final Context context) {
        DialogUtils.showLoading(context, "缓存清除中");
        Utils.deleteFilesByDirectory(context.getCacheDir());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = 1;
                    msg.sendToTarget();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    Toast.makeText(mActivity, "清除成功!", Toast.LENGTH_SHORT).show();
                    DialogUtils.hideLoading();
                    break;
            }
        }
    };

}
