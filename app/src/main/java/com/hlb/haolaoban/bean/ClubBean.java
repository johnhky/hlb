package com.hlb.haolaoban.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubBean implements Serializable {

    /**
     * mid : 240
     * doctor_id : 244
     * doctor_team_id : 242
     * content : <p></p><p>★适合人群：</p><p>　　1、初级英语爱好者</p><p>　　2、长时间未碰英语，想重新拾起来的学员</p><p>　　★活动目标：突破英语口语瓶颈，掌握连音略音吞音技巧，练就纯正美音，建立英语自信心，从此突破开口障碍。</p><p>　　★参与流程：</p><p>　　报名活动--接受预约电话及短信--按预约时间到达现场--登记--了解基本情况后安排入场--参与活动</p><p>　　★费用信息：</p><p>　　全部免费，记得带朋友一起来参加!!另外我们每周还举办不同系列主题活动，涉及文化，旅行，生活，工作，运动，商业等各个领域，为您的英语学习提供大量交流练习的机会。</p><p>　　报名要求：</p>
     * environment_image : ["http://test.haolaoban99.com/uploads/file/20180103/c2b7d0374803b8664951c056e986fdd0.jpg","http://test.haolaoban99.com/uploads/file/20180103/70a8bce0d98cd0a20085e97bd34ca4f4.jpg"]
     * username : 18924161619
     * name : 好老伴中关村俱乐部
     * photo : http://test.haolaoban99.com/uploads/file/20171128/dedc3588d7f6dd86a82b3f8827aad6fe.jpg
     * address : 北京中关村
     * doctor_team_name : 好老伴南村团队
     * doctor_team_photo :
     * doctor_name : 杨小华
     * doctor_photo : http://test.haolaoban99.com/uploads/file/20180103/618cedca82f7b4a4be1cb926ecb4c00c.jpg
     * doctor_introduce : 擅长：外科常见病的治疗，妇科，骨科、泌尿外科等常见病的治疗，通过全科医师培训，对其他科室的常见多发病也有一定的经验。
     * 简介：从事临床工作七八年，临床经验很多。作为医生，为患者服务，既是责任，也是义务。不忘学习本专业研究的新成果，不断汲取新的营养，锻炼科研思维；坚持“精益求精，一丝不苟”的原则。
     */

    private int mid;
    private int doctor_id;
    private int doctor_team_id;
    private String content = "";
    private String username = "";
    private String name = "";
    private String photo = "";
    private String address = "";
    private String doctor_team_name = "";
    private String doctor_team_photo = "";
    private String doctor_name = "";
    private String doctor_photo = "";
    private String doctor_introduce = "";
    private List<String> environment_image = new ArrayList<>();

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getDoctor_team_id() {
        return doctor_team_id;
    }

    public void setDoctor_team_id(int doctor_team_id) {
        this.doctor_team_id = doctor_team_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoctor_team_name() {
        return doctor_team_name;
    }

    public void setDoctor_team_name(String doctor_team_name) {
        this.doctor_team_name = doctor_team_name;
    }

    public String getDoctor_team_photo() {
        return doctor_team_photo;
    }

    public void setDoctor_team_photo(String doctor_team_photo) {
        this.doctor_team_photo = doctor_team_photo;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_photo() {
        return doctor_photo;
    }

    public void setDoctor_photo(String doctor_photo) {
        this.doctor_photo = doctor_photo;
    }

    public String getDoctor_introduce() {
        return doctor_introduce;
    }

    public void setDoctor_introduce(String doctor_introduce) {
        this.doctor_introduce = doctor_introduce;
    }

    public List<String> getEnvironment_image() {
        return environment_image;
    }

    public void setEnvironment_image(List<String> environment_image) {
        this.environment_image = environment_image;
    }
}
