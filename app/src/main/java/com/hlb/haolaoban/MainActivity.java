package com.hlb.haolaoban;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.activity.ChatActivity;
import com.hlb.haolaoban.activity.TotalRemindActivity;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.bean.TokenBean;
import com.hlb.haolaoban.databinding.ActivityMainBinding;
import com.hlb.haolaoban.fragment.main.MainClubFragment;
import com.hlb.haolaoban.fragment.main.MainHomeFragment;
import com.hlb.haolaoban.fragment.main.MainMineFragment;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.JoinVideoEvent;
import com.hlb.haolaoban.otto.ShowNotificationEvent;
import com.hlb.haolaoban.otto.TokenOutEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by heky on 2017/10/31.
 */

public class MainActivity extends FragmentActivity {
    ActivityMainBinding binding;
    Fragment mainHome, mainClub, mainMine;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showToken();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkPermission();
        binding.titlebar.tbTitle.setText("好老伴");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mainHome == null) {
            mainHome = new MainHomeFragment();
            transaction.add(R.id.fragment_container, mainHome);
            binding.titlebar.toolBar.setNavigationIcon(null);
        }
        transaction.commit();
        initView();
    }

    public void showToken() {
        api.getToken(HttpUrls.getToken()).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                TokenBean data = gson.fromJson(response, TokenBean.class);
                if (null != data) {
                    Hawk.put(Constants.TOKEN, data.getToken());
                    Hawk.put(Constants.TOKENOUT, data.getTokenout());
                } else {
                    Log.e("eeee", "服务器异常");
                }
            }
        });
    }

    public void initView() {
        binding.mainRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                switch (checkedId) {
                    case R.id.main_radio_home:
                        if (mainHome == null) {
                            mainHome = new MainHomeFragment();
                            transaction.add(R.id.fragment_container, mainHome);
                        } else {
                            transaction.show(mainHome);
                        }
                        binding.titlebar.tbTitle.setText("好老伴");
                        binding.mainRadioHome.setTextColor(getResources().getColor(R.color.main_tab_color));
                        binding.mainRadioClub.setTextColor(getResources().getColor(R.color.gray_33));
                        binding.mainRadioMine.setTextColor(getResources().getColor(R.color.gray_33));
                        break;
                    case R.id.main_radio_club:
                        if (mainClub == null) {
                            mainClub = new MainClubFragment();
                            transaction.add(R.id.fragment_container, mainClub);
                        } else {
                            transaction.show(mainClub);
                        }
                        binding.titlebar.tbTitle.setText("俱乐部");
                        binding.mainRadioClub.setTextColor(getResources().getColor(R.color.main_tab_color));
                        binding.mainRadioHome.setTextColor(getResources().getColor(R.color.gray_33));
                        binding.mainRadioMine.setTextColor(getResources().getColor(R.color.gray_33));
                        break;
                    case R.id.main_radio_mine:
                        if (null != Settings.getUserProfile()) {
                            if (mainMine == null) {
                                mainMine = new MainMineFragment();
                                transaction.add(R.id.fragment_container, mainMine);
                            } else {
                                transaction.show(mainMine);
                            }
                            binding.titlebar.tbTitle.setText("我的");
                            binding.mainRadioMine.setTextColor(getResources().getColor(R.color.main_tab_color));
                            binding.mainRadioHome.setTextColor(getResources().getColor(R.color.gray_33));
                            binding.mainRadioClub.setTextColor(getResources().getColor(R.color.gray_33));
                        } else {
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        break;
                }
                transaction.commit();
            }
        });
    }

    public void hideAllFragment(FragmentTransaction transaction) {
        if (mainHome != null) {
            transaction.hide(mainHome);
        }
        if (mainClub != null) {
            transaction.hide(mainClub);
        }
        if (mainMine != null) {
            transaction.hide(mainMine);
        }
    }

    @Subscribe
    public void onReceiveEvent(TokenOutEvent event) {
        if (event.getCode() == -99) {
            Utils.showToast("请重新尝试!");
            showToken();
        }
    }


    @Subscribe
    public void onReceiveEvent(ShowNotificationEvent event) {
        if (!TextUtils.isEmpty(event.getMsg())) {
            Intent i = new Intent(MainActivity.this, TotalRemindActivity.class);
            i.putExtra(Constants.DATA, event.getMsg());
            startActivity(i);
        }
    }

    @Subscribe
    public void onReciveEvent(JoinVideoEvent event) {
        if (event.getType().equals("calling")) {
            Intent i = ChatActivity.intentFor(MainActivity.this, event.getChannel());
            startActivity(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
      /*      case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Utils.showToast("您拒绝了软件录音的权限,如有需要,请在应用程序权限管理中打开");
                }
                break;
            }*/
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        checkPermission();
                    }
                }
                break;

        }
    }

    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (!mPermissionList.isEmpty()) {
            String[] permission = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permission, 1);
        }
        Utils.mkDirs(Environment.getExternalStorageDirectory().getPath() + "/hlb/record/");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    boolean isExit = false;

    private void exit() {
        if (!isExit) {
            isExit = true;
            Utils.showToast("再按一次退出程序");
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
