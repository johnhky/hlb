package com.hlb.haolaoban.module;

import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.bean.ImageBean;
import com.hlb.haolaoban.bean.TokenBean;
import com.hlb.haolaoban.bean.UserInfoBean;
import com.hlb.haolaoban.bean.Userbean;
import com.hlb.haolaoban.http.ApiDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by heky on 2017/11/8.
 */

public interface ApiModule {

    @GET("index")
    Call<ApiDTO> getClub(@QueryMap Map<String, String> params);

    /*获取token*/
    @FormUrlEncoded
    @POST("getToken")
    Call<ApiDTO> getToken(@FieldMap Map<String, String> params);

    /*获取首页文章列表*/
    @FormUrlEncoded
    @POST("index")
    Call<ApiDTO> getArticle(@FieldMap Map<String, String> params);

    /*获取用户个人信息*/
    @FormUrlEncoded
    @POST("index")
    Call<ApiDTO> getUserInfo(@FieldMap Map<String, String> params);

    /*用户登录*/
    @GET("index")
    Call<ApiDTO> login(@QueryMap Map<String, String> params);

    /*密码修改*/
    @FormUrlEncoded
    @POST("index")
    Call<ApiDTO> noResponse(@FieldMap Map<String, String> params);

    /*图片上传*/
    @FormUrlEncoded
    @POST("index")
    Call<ApiDTO> uploadImage(@FieldMap Map<String, String> params);

    /*发起通话请求*/
    @FormUrlEncoded
    @POST("index")
    Call video(@FieldMap Map<String, String> params);

}
