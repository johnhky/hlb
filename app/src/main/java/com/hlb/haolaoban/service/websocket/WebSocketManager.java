package com.hlb.haolaoban.service.websocket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.MyApplication;
import com.hlb.haolaoban.bean.DrugRemind;
import com.hlb.haolaoban.handler.MsgHandler;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.FinishChatEvent;
import com.hlb.haolaoban.otto.JoinVideoEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.NotificationUtil;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by heky on 2017/11/23.
 */

public class WebSocketManager {

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int FRAME_QUEUE_SIZE = 5;
    private static WebSocketManager mInstance;
    private WebSocket ws;
    private WsListener mListener;
    private WsStatus wsStatus;
    private String url = Constants.WEBSOCKET;
    Gson gson = new GsonBuilder().create();

    public static WebSocketManager getInstance() {
        if (mInstance == null) {
            synchronized (WebSocketManager.class) {
                if (mInstance == null) {
                    mInstance = new WebSocketManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            ws = new WebSocketFactory().createSocket(url, CONNECT_TIMEOUT)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(mListener = new WsListener())//添加回调监听
                    .connectAsynchronously();//异步连接
            setWsStatus(WsStatus.CONNECTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("eeee", "第一次连接");
    }

    private Handler mHandler = new Handler();

    private int reconnectCount = 0;//重连次数
    private long minInterval = 3000;//重连最小时间间隔
    private long maxInterval = 60000;//重连最大时间间隔

    public void reconnect() {
        if (!isNetConnect()) {
            reconnectCount = 0;
            Log.e("eeee", "重连失败网络不可用");
            return;
        }

        //这里其实应该还有个用户是否登录了的判断 因为当连接成功后我们需要发送用户信息到服务端进行校验
        //由于我们这里是个demo所以省略了
        if (ws != null &&
                !ws.isOpen() &&//当前连接断开了
                getWsStatus() != WsStatus.CONNECTING) {//不是正在重连状态

            reconnectCount++;
            setWsStatus(WsStatus.CONNECTING);

            long reconnectTime = minInterval;
            if (reconnectCount > 3) {
                long temp = minInterval * (reconnectCount - 2);
                reconnectTime = temp > maxInterval ? maxInterval : temp;
            }

            ;
            Log.e("eeee", "准备开始第%d次重连,重连间隔%d -- url:" + reconnectCount + "  " + reconnectTime + "  " + url);
            mHandler.postDelayed(mReconnectTask, reconnectTime);
        }
    }

    private Runnable mReconnectTask = new Runnable() {

        @Override
        public void run() {
            try {
                ws = new WebSocketFactory().createSocket(url, CONNECT_TIMEOUT)
                        .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                        .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                        .addListener(mListener = new WsListener())//添加回调监听
                        .connectAsynchronously();//异步连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(mReconnectTask);
    }

    private boolean isNetConnect() {
        ConnectivityManager connectivity = (ConnectivityManager) MyApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    class WsListener extends WebSocketAdapter {

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);
            Log.e("eeee", "onMessage:" + text);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(text);
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
                                    saveMsg(text, Hawk.get(Constants.MID) + "");
                                }
                                break;
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            Log.e("eeee", "连接成功");
            setWsStatus(WsStatus.CONNECT_SUCCESS);
            cancelReconnect();
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
            setWsStatus(WsStatus.CONNECT_FAIL);
            reconnect();
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            Log.e("eeee", "连接断开");
            setWsStatus(WsStatus.CONNECT_FAIL);
            reconnect();
        }

    }


    public WsStatus getWsStatus() {
        return wsStatus;
    }

    public void setWsStatus(WsStatus wsStatus) {
        this.wsStatus = wsStatus;
    }

    private void saveMsg(String s, String id) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            String data = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data)) {
                jsonObject = new JSONObject(data);
                String drugs = jsonObject.getString("data");
                List<DrugRemind> list = gson.fromJson(drugs, new TypeToken<ArrayList<DrugRemind>>() {
                }.getType());
                MsgHandler.saveMsg(list, id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
