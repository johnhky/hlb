package com.hlb.haolaoban.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hlb.haolaoban.utils.PayCallback;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付回调
 * Created by Roger on 8/13/15.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private IWXAPI api;
    private static PayCallback callback;

    public static void setCallback(PayCallback callback) {
        WXPayEntryActivity.callback = callback;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        api = WXAPIFactory.createWXAPI(this, "1493327682");
        api.handleIntent(getIntent(), this);
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

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = baseResp.errCode;
            Log.e("eeee",errCode+"");
            if (callback != null) {
                switch (errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        callback.onPaySuccess();
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_COMM:
                        callback.onPayFail();
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        callback.onPayFail();
                        finish();
                        break;
                    default:
                        break;
                }
                callback = null;
            }
        }
    }
}
