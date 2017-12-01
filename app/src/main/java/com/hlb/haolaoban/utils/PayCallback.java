package com.hlb.haolaoban.utils;

/**
 * Created by heky on 2017/11/18.
 */

public interface PayCallback {

    void onPaySuccess();

    void onPayFail(int code,String msg);

    void onCancel();
}
