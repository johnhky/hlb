package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/9.
 */

public class TokenOutEvent extends Event {
    public int code;

    public TokenOutEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
