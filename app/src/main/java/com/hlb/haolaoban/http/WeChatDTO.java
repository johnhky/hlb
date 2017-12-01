package com.hlb.haolaoban.http;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heky on 2017/11/18.
 */

public class WeChatDTO {


    /**
     * appid : wx2936ba651e971bb4
     * mch_id : 1493327682
     * prepay_id : wx201712011440056049e4c8c80926317874
     * trade_type : APP
     * nonce_str : 8vuZgQWc7eHfnHJd
     * package : Sign=WXPay
     * timestamp : 1512110405
     * sign : BB72222B38DFBF0CFC030BC870D871A1
     */

    private String appid;
    private String mch_id;
    private String prepay_id;
    private String trade_type;
    private String nonce_str;
    @SerializedName("package")
    private String packageX;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WeChatDTO{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", packageX='" + packageX + '\'' +
                ", timestamp=" + timestamp +
                ", sign='" + sign + '\'' +
                '}';
    }
}
