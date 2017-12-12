package com.hlb.haolaoban.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.PaySuccessEvent;
import com.hlb.haolaoban.otto.RefreshOrderEvent;
import com.hlb.haolaoban.utils.PayCallback;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2017/12/11.
 */

public class AlipayCallback extends SimpleCallback {

    private static final String STATUS_SUCCESS = "9000";
    public static final int PAY_FLAG = 1;

    private Activity activity;
    private static PayCallback mCallback;
    private static PayStatusHandler payStatusHandler = new PayStatusHandler();

    public AlipayCallback(Activity activity){
        this.activity = activity;
        mCallback = null;
        mCallback = new PayCallback() {
            @Override
            public void onPaySuccess() {
                BusProvider.getInstance().postEvent(new PaySuccessEvent());
                BusProvider.getInstance().postEvent(new RefreshOrderEvent());
            }

            @Override
            public void onPayFail() {
                Utils.showToastLong("支付失败!");
            }

            @Override
            public void onCancel() {
                Utils.showToastLong("取消支付!");
            }
        };
    }

    @Override
    protected void handleResponse(final String response) {
        if (response.equals("finish_pay")) {
            if (mCallback != null) {
                mCallback.onPaySuccess();
            }
            return;
        }

        if (response != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(activity);
                    String result = payTask.pay(response,true);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    payStatusHandler.sendMessage(msg);
                }
            }).start();
        }
    }


    /**
     * 处理支付结果回调
     */
    public static class PayStatusHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAY_FLAG:
                    PayResult aliPayResult = new PayResult(msg.obj.toString());
                    String payStatus = aliPayResult.getResultStatus();
                    if (payStatus.equals(STATUS_SUCCESS)) {
                        if (mCallback != null) {
                            mCallback.onPaySuccess();
                        }
                    } else {
                        if (mCallback != null) {
                            mCallback.onPayFail();
                        }
                    }
                    mCallback = null;
            }
        }
    }


    public static class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(String rawResult) {

            if (TextUtils.isEmpty(rawResult))
                return;

            String[] resultParams = rawResult.split(";");
            for (String resultParam : resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus");
                }
                if (resultParam.startsWith("result")) {
                    result = gatValue(resultParam, "result");
                }
                if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo");
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        private String gatValue(String content, String key) {
            String prefix = key + "={";
            return content.substring(content.indexOf(prefix) + prefix.length(),
                    content.lastIndexOf("}"));
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }

}
