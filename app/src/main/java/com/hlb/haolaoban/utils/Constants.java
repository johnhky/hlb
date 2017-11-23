package com.hlb.haolaoban.utils;

import android.os.Build;

import com.hlb.haolaoban.BuildConfig;
import com.orhanobut.hawk.Hawk;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by heky on 2017/10/31.
 */

public class Constants {

    public static final String TOKEN = "TOKEN";
    public static final String TOKENOUT = "TOKENOUT";
    public static final String USER_PROFILE = "USER_PROFILE";
    public static final String PHONE = "PHONE";
    public static final String PASSWORD = "PASSWORD";
    public static final String IMAGE = "IMAGE";
    public static final String MID = "MID";
    public static final String CLUB_ID = "CLUB_ID";
    public static final String POSITION = "POSITION";
    public static final String TYPE = "TYPE";
    public static final String DATA = "DATA";
    public static final String CHANNEL = "CHANNEL";
    public static final String WEBSOCKET_URL = "WEBSOCKET_URL";

    public static Map<String, String> addParams() {
        long timestamp = System.currentTimeMillis() / 1000;
        Map<String, String> params = new LinkedHashMap<>();
        params.put("token", Hawk.get(Constants.TOKEN) + "");
        params.put("v", BuildConfig.VERSION_NAME + "");
        params.put("source", Build.MODEL);
        params.put("appid", BuildConfig.Appid);
        params.put("timestamp", timestamp + "");
        return params;
    }
}
