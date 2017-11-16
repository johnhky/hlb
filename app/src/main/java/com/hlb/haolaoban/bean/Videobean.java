package com.hlb.haolaoban.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/8.
 */

public class Videobean implements Serializable{

    private int nFlag;
    private List<String> answers = new ArrayList<>();
    private String channel;
    private String type;

    public int getnFlag() {
        return nFlag;
    }

    public void setnFlag(int nFlag) {
        this.nFlag = nFlag;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
