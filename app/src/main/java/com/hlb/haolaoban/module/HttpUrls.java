package com.hlb.haolaoban.module;


import android.util.Log;

import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;
import com.orhanobut.hawk.Hawk;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by heky on 2017/10/31.
 */

public class HttpUrls {


    /*token参数*/
    public static Map<String, String> getToken() {
        long timestamp = System.currentTimeMillis() / 1000;
        Map<String, String> params = new LinkedHashMap<>();
        params.put("appid", BuildConfig.Appid);
        params.put("appkey", BuildConfig.appkey);
        params.put("timestamp", timestamp + "");
        return params;
    }

    /*首页文章列表参数*/
    public static Map<String, String> getArticle(int pageNo) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[catid]", 9 + "");
        params.put("param[pageno]", pageNo + "");
        params.put("method", "article.get.list");
        return params;
    }

    /*俱乐部详情*/
    public static Map<String, String> clubDetail(String mid) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", mid);
        params.put("method", "club.get.info");
        return params;
    }

    /*俱乐部列表*/
    public static Map<String, String> club(String keyword, String pageNo, String mid) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[exclude_club_id]", mid);
        params.put("param[keyword]", keyword);
        params.put("param[pageno]", pageNo);
        params.put("method", "club.get.list");
        return params;
    }

    /*俱乐部活动列表*/
    public static Map<String, String> clubActivity(String keyword, String pageNo, String mid) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[club_id]", mid);
        params.put("param[keyword]", keyword);
        params.put("param[pageno]", pageNo);
        params.put("method", "club.get.activity.list");
        Log.e("eeee",params.toString());
        return params;
    }

    /*俱乐部活动详情*/
    public static Map<String, String> clubActivityDetail(String id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[id]",id);
        params.put("method", "club.get.activity.info");
        return params;
    }


    /*首页文章列表参数*/
    public static Map<String, String> getHelpCenter(int pageNo) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[catid]", 12 + "");
        params.put("param[pageno]", pageNo + "");
        params.put("method", "article.get.list");
        return params;
    }

    /*用户个人信息参数*/
    public static Map<String, String> getUserInfo(int id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", id + "");
        params.put("param[username]", Hawk.get(Constants.PHONE) + "");
        params.put("method", "member.get.info");
        return params;
    }

    /*登录参数*/
    public static Map<String, String> login(String phone, String psw) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("username", phone);
        params.put("password", psw);
        params.put("type", BuildConfig.USER_TYPE + "");
        params.put("device", "mobile");
        return params;
    }

    /*忘记密码*/
    public static Map<String, String> forgetPassword(String phone, String smsCode, String newPsw, String confirmPsw) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[username]", phone);
        params.put("param[smscode]", smsCode);
        params.put("param[new_password]", newPsw);
        params.put("param[confirm_password]", confirmPsw);
        params.put("method", "member.modify.password");
        return params;
    }

    /*获取验证码*/
    public static Map<String, String> getCheck(String phone) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mobile]", phone);
        params.put("param[type]", "code");
        params.put("method", "public.msm.send");
        return params;
    }

    /*绑定手机*/
    public static Map<String, String> bindPhone(String phone, String smsCode) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", Settings.getUserProfile().getMid() + "");
        params.put("param[mobile]", phone);
        params.put("param[smscode]", smsCode);
        params.put("method", "member.modify.mobile");
        return params;
    }

    /*忘记密码*/
    public static Map<String, String> updatePassword(String phone, String oldPsw, String newPsw, String confirmPsw) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[username]", phone);
        params.put("param[lod_password]", oldPsw);
        params.put("param[new_password]", newPsw);
        params.put("param[confirm_password]", confirmPsw);
        params.put("method", "member.modify.password");
        return params;
    }

    /*发起视频通话请求*/
    public static Map<String, String> uploadData(String mid, String name, String photo, String teamId) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "user.infos");
        params.put("mid", mid);
        params.put("name", name);
        params.put("photo", photo);
        params.put("role", "member");
        params.put("team", teamId);
        return params;
    }


    /*药品存量列表*/
    public static Map<String, String> getMedical(String mid, int pageNo) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", mid);
        params.put("param[pageno]", pageNo + "");
        params.put("method", "member.get.drugsstock");
        return params;
    }

    /*获取病历档案列表*/
    public static Map<String, String> getHealthRecord(int mid, int pageNo) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", mid + "");
        params.put("param[pageno]", pageNo + "");
        params.put("method", "member.get.disease");
        return params;
    }

    /*获取健康报告列表*/
    public static Map<String, String> getHealthReport(int mid, int pageNo) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", mid + "");
        params.put("param[pageno]", pageNo + "");
        params.put("method", "member.healtheport.list");
        return params;
    }

    /*获取提醒事项列表*/
    public static Map<String, String> getRemind(int mid) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", mid + "");
        params.put("method", "member.reminders.list");
        return params;
    }

    /*获取提醒事项列表*/
    public static Map<String, String> getTodayRemind(int mid) {
        Map<String, String> params = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String today;
        if (day < 10) {
            today = "0" + day;
        } else {
            today = day + "";
        }
        params.putAll(Constants.addParams());
        params.put("param[mid]", mid + "");
        params.put("param[start_day]", today);
        params.put("method", "member.reminders.list");
        return params;
    }

    /*获取提醒事项列表*/
    public static Map<String, String> getOrderDetail(String oid) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[oid]", oid + "");
        params.put("method", "orders.get.info");
        return params;
    }

    /*获取订单列表*/
    public static Map<String, String> getOrderList(String mid, int pageNo, String status) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "orders.get.list");
        params.putAll(Constants.addParams());
        params.put("param[mid]", mid);
        params.put("param[order_type]", "prescription");
        params.put("param[status]", status);
        params.put("param[pageno]", pageNo + "");
        return params;
    }

    /*上传图片*/
    public static Map<String, String> uploadImage(String base64) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[image]", "data:image/jpeg;base64," + base64);
        params.put("method", "public.uploads.images");
        return params;
    }

    /*发起视频通话请求*/
    public static Map<String, String> startVideo(String club_id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "video.calling");
        params.put("call", Settings.getUserProfile().getMid() + "");
        params.put("token", Hawk.get(com.hlb.haolaoban.utils.Constants.TOKEN) + "");
        params.put("answers[]=", "306");
        return params;
    }

    /*拒绝视频通话
    * @params mode 参数
    *  1  应答者在呼叫时直接挂断
    *  2  进⼊视频会话后主动退出
    *  3  呼叫超时
    *  4  异常
    *  5  发起者在呼叫阶段主动取消
     *  */
    public static Map<String, String> rejectVideo(String mid, String mode, String channel) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "video.refuse");
        params.put("mid", mid);
        params.put("token", Hawk.get(com.hlb.haolaoban.utils.Constants.TOKEN) + "");
        params.put("mode", mode);
        params.put("channel", channel + "");
        return params;
    }

    /*接受视频通话*/
    public static Map<String, String> acceptVideo(String mid, String channel) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "video.accept");
        params.put("mid", mid);
        params.put("token", Hawk.get(com.hlb.haolaoban.utils.Constants.TOKEN) + "");
        params.put("channel", channel);
        return params;
    }

    /*上传语音*/
    public static Map<String, String> uploadAudio(int mid, String club_id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "voice.push");
        params.put("mid", mid + "");
        params.put("team", club_id);
        return params;
    }

    /*获取消息列表*/
    public static Map<String, String> getMessage(String pageNo, String mid) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "history.infos");
        params.put("mid", mid);
        params.put("token", "1");
        params.put("page", pageNo);
        return params;
    }

    /*获取体征项列表*/
    public static Map<String, String> getRealtimetypeList() {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("method", "member.realtimetype.list");
        return params;
    }

    /*获取老人血压值
    * @param device   1.血压计  2.血糖仪  3.手环 4.体温计 5.体脂称
    * */
    public static Map<String, String> getRealTime(String id, String device, String start_time, String end_time) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", id);
        params.put("param[type]", device);
        params.put("param[start_time]", start_time);
        params.put("param[end_time]", end_time);
        params.put("param[ds]", "0");
        params.put("method", "member.realtime.chart");
        return params;
    }

    /*发送消息*/
    public static Map<String, String> sendMsg(String text, String id, String type) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("method", "consult.acceptMessage");
        params.put("mid", id);
        params.put("content", text);
        params.put("format", type);
        return params;
    }


    /*微信支付*/
    public static Map<String, String> wechatPay(String mid, String orderId) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[oid]", orderId);
        params.put("param[mid]", mid);
        params.put("method", "payment.pay.weixin.creater");
        return params;
    }

    /*微信支付*/
    public static Map<String, String> aliPay(String mid, String orderId) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[oid]", orderId);
        params.put("param[mid]", mid);
        params.put("method", "payment.pay.alipay.creater");
        return params;
    }

    /*语音发送*/
    public static Map<String, String> sendAudio(String mid) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("mid", mid);
        params.put("format", "audio");
        return params;
    }

    /*文字发送*/
    public static Map<String, String> sendText(String mid, String text) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("mid", mid);
        params.put("format", "text");
        params.put("content", text);
        params.put("method", "consult.acceptMessage");
        return params;
    }

    /*文字发送*/
    public static Map<String, String> sendImage(String mid, String image) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("mid", mid);
        params.put("format", "image");
        params.put("content", image);
        params.put("method", "consult.acceptMessage");
        return params;
    }

    /*消息列表*/
    public static Map<String, String> msgList(int pageNo) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("mid", Settings.getUserProfile().getMid() + "");
        params.put("token", Hawk.get(Constants.TOKEN) + "");
        params.put("page", pageNo + "");
        params.put("method", "consult.myConsultDetail");
        return params;
    }

}
