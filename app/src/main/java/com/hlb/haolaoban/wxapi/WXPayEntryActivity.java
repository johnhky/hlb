package com.hlb.haolaoban.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hlb.haolaoban.utils.PayCallback;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付回调
 * Created by heky on 2017/11/18.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private static PayCallback callback;

    public static void setCallback(PayCallback callback) {
        WXPayEntryActivity.callback = callback;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        api = WXAPIFactory.createWXAPI(this,null);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = baseResp.errCode;
            if (callback != null) {
                switch (errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        callback.onPaySuccess();
                        finish();
                    case BaseResp.ErrCode.ERR_COMM:
                        callback.onPayFail(errCode,baseResp.errStr);
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        callback.onCancel();
                        finish();
                        break;
                    default:
                        break;
                }
                callback = null;
            }
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }


}
