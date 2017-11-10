package com.hlb.haolaoban.http;

import android.support.annotation.NonNull;

import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.utils.JacksonUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by heky on 2017/11/8.
 */

public class Api {
    public static final String TAG = Api.class.getSimpleName();
    public static final String BASE_URL = BuildConfig.BASE_URL;
    private static OkHttpClient httpClient = getOkHttpClient();

    @NonNull
    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new MyInterceptor()).build();
    }

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create(JacksonUtils.getInstance()));


    public static <S> S of(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

}
