package com.hlb.haolaoban.http;

import android.app.Activity;
import android.util.Log;

import com.hlb.haolaoban.utils.PayCallback;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.HashMap;

/**
 * Created by heky on 2017/11/18.
 */

public class WechatCallback extends SimpleCallback {

    Activity activity;
    PayCallback callback;

    public WechatCallback(Activity activity, HashMap<String, String> params) {
        this.activity = activity;
        callback = null;
        callback = new PayCallback() {
            @Override
            public void onPaySuccess() {

            }

            @Override
            public void onPayFail() {

            }
        };
    }

    @Override
    protected void handleResponse(String response) {
        PayReq req = new PayReq();
        Log.e("eeee", response);
  /*      IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, response.getAppid());
        msgApi.registerApp(response.getAppid());
        msgApi.sendReq(req);*/
    }
}
