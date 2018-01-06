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
import com.hlb.haolaoban.otto.RefreshMsgList;
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
    private long time = 100000000000L;

    public WebSocketConnect() {
        mWebSocketOptions.setSocketConnectTimeout(30000);
        mWebSocketOptions.setSocketReceiveTimeout(10000);
    }

    public void login(final String url) {
        try {
            if (webSocketConnection.isConnected()) {
                webSocketConnection.disconnect();
            }
            webSocketConnection.connect(url, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    sendMsg(time);
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
                    Log.e("eeee", payload);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(payload);
                        String mode = jsonObject.getString("mode");
                        String channel = "";
                        switch (mode) {
                            case "message":
                                String type = jsonObject.getString("type");
                                String msg = jsonObject.getString("msg");
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
                                break;
                            case "consult":
                                if (!Constants.isRead) {
                                    NotificationUtil.showNotification(MyApplication.mContext, "message", "您收到了一条新消息!");
                                }
                                BusProvider.getInstance().postEvent(new RefreshMsgList());
                                break;
                            case "order":
                                String msgs = jsonObject.getString("msg");
                                String mType = jsonObject.getString("type");
                                switch (mType) {
                                    case "1":
                                    case "2":
                                    case "3":
                                    case "4":
                                    case "5":
                                        NotificationUtil.showNotification(MyApplication.mContext, mType, msgs);
                                        break;
                                }
                                break;
                            case "video":
                                String types = jsonObject.getString("type");
                                if (!TextUtils.isEmpty(types)) {
                                    if (types.equals("calling")) {
                                        channel = jsonObject.getString("channel");
                                    }
                                    switch (types) {
                                        case "meet":
                                            BusProvider.getInstance().postEvent(new JoinVideoEvent(types));
                                            break;
                                        case "refuse":
                                            BusProvider.getInstance().postEvent(new FinishChatEvent("finish", "1"));
                                            break;
                                        case "calling":
                                            BusProvider.getInstance().postEvent(new JoinVideoEvent(types, channel));
                                            break;
                                        case "leave":
                                            BusProvider.getInstance().postEvent(new FinishChatEvent("finish","2"));
                                            break;
                                    }
                                }
                                break;
                            case "-99":
                                WebSocketUtil.getInstance().login(url);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, mWebSocketOptions);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }

    }

    private void sendMsg(final long times) {
        timer = new CountDownTimer(times, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                webSocketConnection.sendTextMessage("");
            }

            @Override
            public void onFinish() {
                time = 10000000000L;
                sendMsg(time);
            }
        };
        timer.start();
    }

}
