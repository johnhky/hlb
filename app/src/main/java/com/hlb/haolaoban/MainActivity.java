package com.hlb.haolaoban;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.bean.TokenBean;
import com.hlb.haolaoban.databinding.ActivityMainBinding;
import com.hlb.haolaoban.fragment.MainClubFragment;
import com.hlb.haolaoban.fragment.MainHomeFragment;
import com.hlb.haolaoban.fragment.MainMineFragment;
import com.hlb.haolaoban.module.LoginModule;
import com.hlb.haolaoban.utils.Constants;
import com.orhanobut.hawk.Hawk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


/**
 * Created by heky on 2017/10/31.
 */

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    Fragment mainHome, mainClub, mainMine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.titlebar.tbTitle.setText("好老伴");
        long tokenout = 0;
        if (null != Hawk.get(Constants.TOKENOUT)){
            tokenout = Hawk.get(Constants.TOKENOUT);
        }
        long timestamp = System.currentTimeMillis() / 1000;
        if (null == Hawk.get(Constants.TOKEN) || tokenout < timestamp) {
            getToken();
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mainHome == null) {
            mainHome = new MainHomeFragment();
            transaction.add(R.id.fragment_container, mainHome);
            binding.titlebar.toolBar.setNavigationIcon(null);
        }
        transaction.commit();
        initView();
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

    public static void getToken() {
        final Gson gson = new GsonBuilder().create();
        OkHttpUtils.post().url(LoginModule.GET_TOKEN)
                .addParams("appid", BuildConfig.Appid)
                .addParams("appkey", BuildConfig.appkey)
                .addParams("timestamp", BuildConfig.timeStamp).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                TokenBean data = gson.fromJson(response, TokenBean.class);
                if (data.getCode() == 1) {
                    Hawk.put(Constants.TOKEN, data.getData().getToken());
                    Hawk.put(Constants.TOKENOUT, data.getData().getTokenout());
                }
            }
        });
    }

}
