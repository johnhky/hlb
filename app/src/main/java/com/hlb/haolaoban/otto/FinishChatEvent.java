package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/10.
 */

public class FinishChatEvent extends Event {

    private String type;

    public FinishChatEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
