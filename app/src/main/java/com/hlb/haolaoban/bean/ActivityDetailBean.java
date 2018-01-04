package com.hlb.haolaoban.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by heky on 2018/1/3.
 */

public class ActivityDetailBean implements Serializable{

    private int id;
    private int catid;
    private int club_id;
    private String title;
    private String files;
    private String image;
    @SerializedName("abstract")
    private String abstractX;
    private String content;
    private String ishow;
    private int addtime;
    private String club_name;
    private String club_address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public int getClub_id() {
        return club_id;
    }

    public void setClub_id(int club_id) {
        this.club_id = club_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(String abstractX) {
        this.abstractX = abstractX;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIshow() {
        return ishow;
    }

    public void setIshow(String ishow) {
        this.ishow = ishow;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public String getClub_address() {
        return club_address;
    }

    public void setClub_address(String club_address) {
        this.club_address = club_address;
    }
}
