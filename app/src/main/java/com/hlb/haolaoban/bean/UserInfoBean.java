package com.hlb.haolaoban.bean;

import java.io.Serializable;
/**
 * Created by heky on 2017/11/2.
 */

public class UserInfoBean implements Serializable{

    private String mid;
    private String username;
    private String photo;
    private String sex;
    private int age;
    private String name;
    private int pid;
    private int club_id;
    private String club_name;
    private int doctor_team_id;
    private String doctor_team_name;
    private int status;
    private int lingban;
    private String birthday;
    private String address;
    private String device;
    private String qianmin_img;
    private String addtime;
    private Children children;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getClub_id() {
        return club_id;
    }

    public void setClub_id(int club_id) {
        this.club_id = club_id;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public int getDoctor_team_id() {
        return doctor_team_id;
    }

    public void setDoctor_team_id(int doctor_team_id) {
        this.doctor_team_id = doctor_team_id;
    }

    public String getDoctor_team_name() {
        return doctor_team_name;
    }

    public void setDoctor_team_name(String doctor_team_name) {
        this.doctor_team_name = doctor_team_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLingban() {
        return lingban;
    }

    public void setLingban(int lingban) {
        this.lingban = lingban;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getQianmin_img() {
        return qianmin_img;
    }

    public void setQianmin_img(String qianmin_img) {
        this.qianmin_img = qianmin_img;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public Children getChildren() {
        return children;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    public class Children implements Serializable {
        private String mid;
        private String username;
        private String sex;

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

}