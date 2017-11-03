package com.hlb.haolaoban.http;

import android.app.Activity;
import android.content.Intent;

import com.hlb.haolaoban.MyApplication;
import com.hlb.haolaoban.activity.LoginActivity;
import com.hlb.haolaoban.utils.Utils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by heky on 2017/11/2.
 */

public abstract class SimpleCallback extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return response.body().toString();
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(String response, int id) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            int code = jsonObject.optInt("code");
            String msg = jsonObject.getString("msg");
            if (code == 1) {
                onResponseAll(response, msg, code);
            } else if (code == -99) {
                Intent i = new Intent();
                i.setClass(MyApplication.mContext, LoginActivity.class);
                MyApplication.mContext.startActivity(i);
                ((Activity) MyApplication.mContext).finish();
            } else {
                Utils.showToast(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onResponseAll(String response, String msg, int code) {

    }


}
