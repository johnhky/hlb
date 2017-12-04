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
    private String partnerid;
    private String prepayid;
    private String noncestr;
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

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
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
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", packageX='" + packageX + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
