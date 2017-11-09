package com.hlb.haolaoban.bean;

import java.io.Serializable;

/**
 * Created by heky on 2017/11/2.
 */

public class TokenBean implements Serializable{

    public String token;
    public long tokenout;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenout() {
        return tokenout;
    }

    public void setTokenout(long tokenout) {
        this.tokenout = tokenout;
    }


    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                ", tokenout=" + tokenout +
                '}';
    }
}
