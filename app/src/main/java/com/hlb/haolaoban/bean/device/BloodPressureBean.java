package com.hlb.haolaoban.bean.device;

/**
 * Created by heky on 2017/11/24.
 */

public class BloodPressureBean {
    /* [{"date":"11/20","value":"111"},{"date":"11/22","value":"116"},{"date":"11/24","value":"124"}]*/
    private String date;
    private float value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
