package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/10.
 */

public class JoinVideoEvent extends Event {
    private String type;
    private String channel;

    public JoinVideoEvent(String type) {
        this.type = type;
    }

    public JoinVideoEvent(String type, String channel) {
        this.type = type;
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
