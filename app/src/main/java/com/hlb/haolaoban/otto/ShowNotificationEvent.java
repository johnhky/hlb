package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/10.
 */

public class ShowNotificationEvent extends Event {

    private String msg;

    public ShowNotificationEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
