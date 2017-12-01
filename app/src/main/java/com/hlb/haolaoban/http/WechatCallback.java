package com.hlb.haolaoban.http;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.RefreshOrderEvent;
import com.hlb.haolaoban.utils.PayCallback;
import com.hlb.haolaoban.utils.Utils;
import com.hlb.haolaoban.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by heky on 2017/11/18.
 */

public class WechatCallback extends SimpleCallback {

    Activity activity;
    PayCallback callback;
    Gson gson = new GsonBuilder().create();

    public WechatCallback(Activity activity) {
        this.activity = activity;
        callback = null;
        callback = new PayCallback() {
            @Override
            public void onPaySuccess() {
                Utils.showToast("支付成功!");
                BusProvider.getInstance().postEvent(new RefreshOrderEvent());
            }

            @Override
            public void onPayFail() {
                Utils.showToast("支付失败!");
            }
        };
        WXPayEntryActivity.setCallback(callback);
    }

    @Override
    protected void handleResponse(String response) {
        PayReq req = new PayReq();
        WeChatDTO data = gson.fromJson(response, WeChatDTO.class);
        IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, data.getAppid());
        req.appId = data.getAppid();
        req.partnerId = data.getPartnerid();
        req.prepayId = data.getPrepayid();
        req.packageValue = data.getPackageX();
        req.nonceStr = data.getNoncestr();
        req.timeStamp = data.getTimestamp();
        req.sign = data.getSign();
        msgApi.registerApp(data.getAppid());
        msgApi.sendReq(req);
    }
}
