package com.hlb.haolaoban.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.bean.UserBean;
import com.orhanobut.hawk.Hawk;

/**
 * Created by heky on 2017/11/2.
 */

public class Settings {

    static Gson gson = new GsonBuilder().create();

    public static void setUesrProfile(UserBean data) {
        Hawk.put(Constants.USER_PROFILE, gson.toJson(data));
    }

    public static UserBean getUserProfile() {
        String json = Hawk.get(Constants.USER_PROFILE);
        if (json == null || json.equals("")) {
            return null;
        }
        UserBean data = gson.fromJson(json, UserBean.class);
        return data;
    }
}
