package com.hlb.haolaoban.bean;

/**
 * Created by heky on 2017/11/2.
 */

public class UserBean {
    private int mid;
    private String username;
    private int club_id;
    private int doctor_team_id;
    private int pid;
    private int zid;
    private String company;
    private long service_out_time;
    private int position;
    private String tel;
    private String code;
    private String address;
    private String birthday;
    private String voucherid;
    private int invalidtime;
    private String photo;
    private String name;
    private String sex;
    private int age;
    private String club_name;
    private String doctor_team_name;
    private int status;
    private int lingban;
    private String qianmin_img;
    private long addtime;
    public Children children;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public int getInvalidtime() {
        return invalidtime;
    }

    public void setInvalidtime(int invalidtime) {
        this.invalidtime = invalidtime;
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

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
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

    public String getQianmin_img() {
        return qianmin_img;
    }

    public void setQianmin_img(String qianmin_img) {
        this.qianmin_img = qianmin_img;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public Children getChildren() {
        return children;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    public int getZid() {
        return zid;
    }

    public void setZid(int zid) {
        this.zid = zid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getService_out_time() {
        return service_out_time;
    }

    public void setService_out_time(long service_out_time) {
        this.service_out_time = service_out_time;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

     public class Children {
        public String username;
        public String mid;
        public String sex;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "mid=" + mid +
                ", username='" + username + '\'' +
                ", club_id=" + club_id +
                ", doctor_team_id=" + doctor_team_id +
                ", pid=" + pid +
                ", voucherid='" + voucherid + '\'' +
                ", invalidtime=" + invalidtime +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", club_name='" + club_name + '\'' +
                ", doctor_team_name='" + doctor_team_name + '\'' +
                ", status=" + status +
                ", lingban=" + lingban +
                ", qianmin_img='" + qianmin_img + '\'' +
                ", addtime=" + addtime +
                ", children=" + children +
                '}';
    }
}