package com.hlb.haolaoban.base.websocket;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.hlb.haolaoban.MyApplication;
import com.hlb.haolaoban.handler.MsgHandler;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.FinishChatEvent;
import com.hlb.haolaoban.otto.JoinVideoEvent;
import com.hlb.haolaoban.otto.LoginWebSocketEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.NotificationUtil;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * Created by heky on 2017/12/6.
 */

public class WebSocketConnect {
    WebSocketConnection webSocketConnection = new WebSocketConnection();
    private WebSocketOptions mWebSocketOptions = new WebSocketOptions();
    CountDownTimer timer;
    private long time = 100000000L;

    public WebSocketConnect() {
        mWebSocketOptions.setSocketConnectTimeout(30000);
        mWebSocketOptions.setSocketReceiveTimeout(10000);
    }

    public void login(final String url) {
        try {
            webSocketConnection.connect(url, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    sendMsg();
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                    if (null != timer) {
                        timer.cancel();
                    }
                    if (!TextUtils.isEmpty(url)) {
                        BusProvider.getInstance().postEvent(new LoginWebSocketEvent(url + ""));
                    }

                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
                    Log.e("eeee",payload);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(payload);
                        int code = jsonObject.optInt("nFlag");
                        String type = jsonObject.getString("type");
                        String channel = "";
                        if (code == 1) {
                            if (!TextUtils.isEmpty(type)) {
                                if (type.equals("calling")) {
                                    channel = jsonObject.getString("channel");
                                }
                                switch (type) {
                                    case "meet":
                                        BusProvider.getInstance().postEvent(new JoinVideoEvent(type));
                                        break;
                                    case "refuse":
                                        BusProvider.getInstance().postEvent(new FinishChatEvent("finish"));
                                        break;
                                    case "calling":
                                        BusProvider.getInstance().postEvent(new JoinVideoEvent(type, channel));
                                        break;
                                }
                            }
                        } else {
                            String msg = jsonObject.getString("msg");
                            String mode = jsonObject.getString("mode");
                            if (mode.equals("order")) {
                                switch (type) {
                                    case "1":
                                    case "2":
                                    case "3":
                                    case "4":
                                    case "5":
                                        NotificationUtil.showNotification(MyApplication.mContext, type, msg);
                                        break;
                                }
                            } else if (mode.equals("message")) {
                                switch (type) {
                                    case "1":
                                    case "4":
                                        NotificationUtil.showNotification(MyApplication.mContext, "", msg);
                                        break;
                                    case "2":
                                        if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                            MsgHandler.saveMsg(payload, Hawk.get(Constants.MID) + "");
                                        }
                                        break;
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, mWebSocketOptions);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }

    }

    private void sendMsg() {
        timer = new CountDownTimer(time, 30000) {
            @Override
            public void onTick(long millisUntilFinished) {
                webSocketConnection.sendTextMessage("");
                Log.e("eeee",System.currentTimeMillis()+"");
            }

            @Override
            public void onFinish() {
                time = 100000000L;
                sendMsg();
            }
        };
        timer.start();
    }

}
