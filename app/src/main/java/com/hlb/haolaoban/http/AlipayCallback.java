package com.hlb.haolaoban.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.PaySuccessEvent;
import com.hlb.haolaoban.otto.RefreshOrderEvent;
import com.hlb.haolaoban.utils.PayCallback;
import com.hlb.haolaoban.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heky on 2017/12/11.
 */

public class AlipayCallback extends SimpleCallback {

    private static final String STATUS_SUCCESS = "9000";
    public static final int PAY_FLAG = 1;
    private Activity activity;
    private static PayCallback mCallback;
    private static PayStatusHandler payStatusHandler = new PayStatusHandler();

    public AlipayCallback(Activity activity) {
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
        Gson gson = new GsonBuilder().create();
        if (response != null) {
            final AlipayDTO data = gson.fromJson(response, AlipayDTO.class);
            final  String orderInfo = buildOrderParam(buildOrderParamMap(data),data);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(activity);
                    String result = payTask.pay(orderInfo, true);
                    Message msg = new Message();
                    msg.what = PAY_FLAG;
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

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map,AlipayDTO data) {
        List<String> keys = new ArrayList<>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true,data));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true,data));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode,AlipayDTO data) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, data.getCharset()));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    public static Map<String, String> buildOrderParamMap(AlipayDTO data) {
        Map<String, String> keyValues = new HashMap<>();
        keyValues.put("app_id", data.getApp_id());
        keyValues.put("biz_content", data.getBiz_content());
        keyValues.put("charset", data.getCharset());
        keyValues.put("method", data.getMethod());
        keyValues.put("notify_url", data.getNotify_url());
        keyValues.put("sign_type", data.getSign_type());
        keyValues.put("timestamp", data.getTimestamp());
        keyValues.put("version", data.getVersion());
        keyValues.put("sign", data.getSign());
        return keyValues;
    }


}
