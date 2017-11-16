package com.hlb.haolaoban;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioGroup;

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

import retrofit2.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    Fragment mainHome, mainClub, mainMine;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    public static final int REQUEST_CODE = 1001;
    public static final int CALL_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {

        }
        checkPermission();
        binding.titlebar.tbTitle.setText("好老伴");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mainHome == null) {
            initUserData();
            mainHome = new MainHomeFragment();
            transaction.add(R.id.fragment_container, mainHome);
            binding.titlebar.toolBar.setNavigationIcon(null);
        }
        transaction.commit();
        initView();
    }

    private void initUserData() {
        api.getUserInfo(HttpUrls.getUserInfo()).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                Gson gson = new GsonBuilder().create();
                UserInfoBean data = gson.fromJson(response, UserInfoBean.class);
                Settings.setUesrProfile(data);
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
                            startActivity(LoginActivity.class);
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
            Intent i = new Intent(mActivity,LoginActivity.class);
            startActivity(i);
            finish();
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
            Intent i = ChatActivity.intentFor(mActivity, event.getChannel());
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
                    showToast("没有开启 " + Manifest.permission.RECORD_AUDIO + "权限!");
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA);
                } else {
                    showToast("没有开启 " + Manifest.permission.CAMERA + "权限!");
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
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
            int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
            }
        }
        Utils.mkDirs(Environment.getExternalStorageDirectory().getPath() + "/hlb/record/");
    }
}
