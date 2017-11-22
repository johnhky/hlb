package com.hlb.haolaoban.bean;

/**
 * Created by heky on 2017/11/22.
 */

public class MessageBean {

    /**
     * type : 1
     * msg : urgency message 11
     * time : 1511340638
     * taskid : 11
     * mode : message
     */

    private String type;
    private String msg;
    private String time;
    private int taskid;
    private String mode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
