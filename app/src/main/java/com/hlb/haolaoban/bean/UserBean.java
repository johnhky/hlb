package com.hlb.haolaoban.bean;

import java.io.Serializable;

/**
 * Created by heky on 2017/11/8.
 */

public class UserBean implements Serializable{

    /**
     * mid : 272
     * username : 13560553124
     * invalidtime : 1511942183
     * club_id : 240
     * doctor_team_id : 242
     * pid : 0
     * voucherid : 448a3a1203bacddf0620e7b7e15eeadd
     * photo : http://test.haolaoban99.com/uploads/20171128/1511846919.jpeg
     * name : 陈梦瑶
     * addtime : 1511847075
     */

    private int mid;
    private String username;
    private int invalidtime;
    private int club_id;
    private int doctor_team_id;
    private int pid;
    private String voucherid;
    private String photo;
    private String name;
    private int addtime;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }
}
