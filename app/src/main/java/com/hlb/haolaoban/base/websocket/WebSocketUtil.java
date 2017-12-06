package com.hlb.haolaoban.base.websocket;

/**
 * Created by heky on 2017/12/6.
 */

public class WebSocketUtil {

    private volatile static WebSocketConnect webSocketConnect = null;

    public static WebSocketConnect getInstance() {
        if (webSocketConnect == null) {
            synchronized (WebSocketConnect.class) {
                if (webSocketConnect == null) {
                    webSocketConnect = new WebSocketConnect();
                }
            }
        }
        return webSocketConnect;
    }

    public void login(String url) {
        webSocketConnect.login(url);
    }
}
