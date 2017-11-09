package com.hlb.haolaoban.http;

import android.os.Build;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by heky on 2017/11/8.
 */

public class MyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("source", Build.MODEL).build();
        Log.e(Api.TAG, "method: " + request.method() + "  url: " + request.url());
        return chain.proceed(request);
    }
}
