package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/14.
 */

public class LoginWebSocketEvent extends Event {

    private String url;

    public LoginWebSocketEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
