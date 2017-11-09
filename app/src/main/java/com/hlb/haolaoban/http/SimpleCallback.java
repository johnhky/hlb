package com.hlb.haolaoban.http;

/**
 * Created by heky on 2017/11/8.
 */

public abstract class SimpleCallback extends ApiCallback {

    @Override
    protected void handleApi(ApiDTO body) {
        super.handleApi(body);
        handleResponse(null);
    }
}
