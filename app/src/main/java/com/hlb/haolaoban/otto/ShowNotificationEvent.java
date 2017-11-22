package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/10.
 */

public class ShowNotificationEvent extends Event {

    private String msg;
    private String type;

    public ShowNotificationEvent(String type,String msg) {
        this.msg = msg;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
