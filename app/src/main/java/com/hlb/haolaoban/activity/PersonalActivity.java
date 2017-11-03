package com.hlb.haolaoban.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.UserBean;
import com.hlb.haolaoban.bean.UserData;
import com.hlb.haolaoban.databinding.ActivityPersonalBinding;
import com.hlb.haolaoban.module.LoginModule;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class PersonalActivity extends BaseActivity {
    ActivityPersonalBinding binding;
    Gson gson = new GsonBuilder().create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
        getUserData();
        initView();
    }

    private void initData() {
        UserBean data = Settings.getUserProfile();
        Glide.with(this).load(data.getPhoto()).into(binding.ivAvater);
        binding.etBirthday.setText(data.getBirthday());
        binding.etName.setText(data.getName());
        String gender = "";
        if (data.getSex().equals("0")) {
            gender = "男";
        } else {
            gender = "女";
        }
        binding.etGender.setText(gender);
        binding.etAddress.setText(data.getAddress());
        binding.etChildren.setText(data.getChildren().getUsername());
        binding.etTeam.setText(data.getDoctor_team_name());
        binding.etClub.setText(data.getClub_name());
    }

    public void initView() {
        binding.titlebar.tbTitle.setText("个人资料");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
   /*     binding.etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBirthDay(v.getContext());
            }
        });*/
    }

/*    @SuppressWarnings("ResourceType")
    public void pickBirthDay(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.etBirthday.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }*/


    private void getUserData() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("param[mid]", Settings.getUserProfile().getMid() + "");
        params.put("param[username]", Settings.getUserProfile().getUsername());
        params.put("method", "member.get.info");
        params.putAll(Constants.addParams());
        OkHttpUtils.post().url(LoginModule.BASE_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    if (code == 1) {
                        UserData data = gson.fromJson(response, UserData.class);
                        Settings.setUesrProfile(data.getData());
                        initData();
                    } else if (code == -99) {
                        startActivity(LoginActivity.class);
                        finish();
                    } else {
                        showToast(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
