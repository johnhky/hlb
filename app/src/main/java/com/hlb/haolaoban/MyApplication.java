package com.hlb.haolaoban;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hlb.haolaoban.http.MyInteceptor;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.OkHttpClient;

/**
 * Created by heky on 2017/10/31.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    public static Context mContext;

    public static MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this;
        Hawk.init(this).build();
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new MyInteceptor(MyInteceptor.TAG, true))
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                .writeTimeout(60000L, TimeUnit.MILLISECONDS)
                //.addNetworkInterceptor(new CacheInterceptor())
                // .cache(cache)
                //其他配置
                .build();
        Utils.init(this);
        OkHttpUtils.initClient(okHttpClient);
        Realm.init(this);
    }

}
