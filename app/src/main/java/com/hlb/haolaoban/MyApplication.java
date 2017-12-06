package com.hlb.haolaoban;

import android.app.Application;
import android.content.Context;

import com.hlb.haolaoban.http.MyInteceptor;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.*;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
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
                .build();
        Utils.init(this);
        OkHttpUtils.initClient(okHttpClient);
        CrashReport.initCrashReport(getApplicationContext(), BuildConfig.buglyId, false);
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().schemaVersion(2).migration(new HLBMigration()).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
    }


    private static class HLBMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            if (oldVersion <= 1) {
                realm.deleteAll();
            }
        }
    }

}
