package com.hlb.haolaoban;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.activity.ChatActivity;
import com.hlb.haolaoban.activity.TotalRemindActivity;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.TokenBean;
import com.hlb.haolaoban.bean.UserInfoBean;
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

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class MainActivity extends FragmentActivity {
    ActivityMainBinding binding;
    Fragment mainHome, mainClub, mainMine;
    ApiModule api = Api.of(ApiModule.class);
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    public static final int REQUEST_CODE = 1001;
    public static final int CALL_REQUEST_CODE = 1002;
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkPermission();
        binding.titlebar.tbTitle.setText("好老伴");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mainHome == null) {
            getToken();
            mainHome = new MainHomeFragment();
            transaction.add(R.id.fragment_container, mainHome);
            binding.titlebar.toolBar.setNavigationIcon(null);
        }
        transaction.commit();
        initView();
    }

    private void getToken() {
        api.getToken(HttpUrls.getToken()).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                if (null != Hawk.get(Constants.TOKEN)) {
                    Hawk.delete(Constants.TOKEN);
                }
                if (!TextUtils.isEmpty(response)) {
                    TokenBean tokenBean = gson.fromJson(response, TokenBean.class);
                    Hawk.put(Constants.TOKEN, tokenBean.getToken());
                    Hawk.put(Constants.TOKENOUT, tokenBean.getTokenout());
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

    /*检测是否拥有语音 视频权限*/
    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @Subscribe
    public void onReceiveEvent(TokenOutEvent event) {
        if (event.getCode() == -99) {
            Utils.showToast("请重新尝试!");
            getToken();
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
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO);
                } else {
                    Utils.showToast("没有开启 " + Manifest.permission.RECORD_AUDIO + "权限!");
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA);
                } else {
                    Utils.showToast("没有开启 " + Manifest.permission.CAMERA + "权限!");
                }
                break;

            }
            case REQUEST_CODE: {
                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                }
                break;
            }
            case CALL_REQUEST_CODE: {
                if (permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                }
                break;
            }

        }

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO);
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
            int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
            }
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQ_ID_CAMERA);
            }
            int hasAudioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasAudioPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQ_ID_RECORD_AUDIO);
            }
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
