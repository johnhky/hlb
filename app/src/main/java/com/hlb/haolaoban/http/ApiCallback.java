package com.hlb.haolaoban.http;

import android.accounts.NetworkErrorException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.MyApplication;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.TokenOutEvent;
import com.hlb.haolaoban.utils.Utils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 2017/11/2.
 */

public abstract class ApiCallback<T> implements Callback<ApiDTO> {

    private int retryTime = 0;
    Gson gson = new GsonBuilder().create();

    @Override
    public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
        if (response.body() == null) {
            onFailure(call, new NullPointerException());
            return;
        }
        int code = response.body().getCode();
        if (code == 1) {
            Object data = response.body().getData();
            if (null != data || !"[]".equals(data)) {
                handleBody(gson.toJson(data));
            } else {
                handleApi(response.body());
            }
        } else if (code == -99) {
            BusProvider.getInstance().postEvent(new TokenOutEvent(code));
        } else {
            String msg = response.body().getMsg();
            Utils.showToast(msg);
            onFailure(call, new NetworkErrorException());
        }
    }

    protected void handleApi(ApiDTO body) {
    }

    protected void handleBody(String body) {
        handleResponse(body);
    }

    protected abstract void handleResponse(String response);

    @Override
    public void onFailure(Call<ApiDTO> call, Throwable t) {
        t.printStackTrace();
        if (t instanceof UnknownHostException) {
            Toast.makeText(MyApplication.mContext, "无法连接服务器,请检查您的网络连接", Toast.LENGTH_SHORT).show();
        } else if (t instanceof SocketTimeoutException) {
            if (retryTime == 0) {
                retryTime += 1;
                call.clone().enqueue(this);
            } else {
                Toast.makeText(MyApplication.mContext, "无法连接服务器,请检查您的网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
