package com.hlb.haolaoban.bean;

/**
 * Created by heky on 2017/11/2.
 */

public class TokenBean {

    private String msg;
    private int code;
    private Data data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

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
    }


}
