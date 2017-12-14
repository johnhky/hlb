package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/10.
 */

public class FinishChatEvent extends Event {

    private String type;
    private String code;
    public FinishChatEvent(String type,String code) {
        this.type = type;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
