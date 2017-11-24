package com.hlb.haolaoban.bean;

import java.io.Serializable;

/**
 * Created by heky on 2017/11/8.
 */

public class UserBean implements Serializable{

    /**
     * mid : 99
     * username : 13660053578
     * invalidtime : 1510210979
     * club_id : 46
     * doctor_team_id : 37
     * pid : 0
     * voucherid : 1330fd53779b40fbb84c85fb93b5fbdb
     */

    private int mid;
    private String username;
    private int invalidtime;
    private int club_id;
    private int doctor_team_id;
    private int pid;
    private String voucherid;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getInvalidtime() {
        return invalidtime;
    }

    public void setInvalidtime(int invalidtime) {
        this.invalidtime = invalidtime;
    }

    public int getClub_id() {
        return club_id;
    }

    public void setClub_id(int club_id) {
        this.club_id = club_id;
    }

    public int getDoctor_team_id() {
        return doctor_team_id;
    }

    public void setDoctor_team_id(int doctor_team_id) {
        this.doctor_team_id = doctor_team_id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getVoucherid() {
        return voucherid;
    }

    public void setVoucherid(String voucherid) {
        this.voucherid = voucherid;
    }
}
