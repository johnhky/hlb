package com.hlb.haolaoban.bean;

import java.util.List;

/**
 * Created by heky on 2017/11/15.
 */

public class PrescriptionDetailBean {


    /**
     * oid : 1711141500367936
     * order_type : prescription
     * mid : 15
     * doctorteam_id : 18
     * doctor_id : 28
     * status : 2
     * pharmacy_id : 0
     * ship_type : 1
     * ship_time : 0
     * ship_name : 梁达免
     * ship_address : 广东省廉江市车板镇大坝村委员会文头岭村 96 号
     * ship_tel : 13500000000
     * ship_mobile : 13500000000
     * cost_item : ￥120.00
     * cost_freight : ￥6.00
     * pay_total : 0.00
     * pay_no :
     * pay_time : 0
     * pay_mid : 0
     * pay_type : 1
     * diagnosis : 1234567896
     * kuaizhao_img : http://test.haolaoban99.com/uploads/prescription/20171115/1510729912.jpg
     * corntab_status : 0
     * addtime : 1510730585
     * goods : [{"id":18,"oid":"1711141500367936","drugs_id":4,"drugs_name":"达比加群酯胶囊(泰毕全)","mun":1,"smun":9,"day_mun":1,"drugs_spec":"","drugs_bn":"C14201221382","corntab_status":"0","unit":"盒","usetime":["06:00:00","11:00:00","13:00:00"],"dosage_unit":"支","dosage_mun":3,"image":"http://test.haolaoban99.com/uploads/20171023/96b4ba99d63348824986ee9885875393.jpg"},{"id":19,"oid":"1711141500367936","drugs_id":6,"drugs_name":"马应龙麝香痔疮栓12粒痔疮药痔疮膏","mun":15,"smun":15,"day_mun":1,"drugs_spec":"1.5g*12粒/盒","drugs_bn":"A03042342","corntab_status":"0","unit":"瓶","usetime":["13:00:00","16:00:00","19:00:00"],"dosage_unit":"支","dosage_mun":5,"image":"http://test.haolaoban99.com/uploads/20171030/bc48de589cbd1ccea8fd9d1268f7f898.jpg"}]
     * sex : 女
     * qianming : http://test.haolaoban99.com
     * age : 47
     * total_fee : ￥126.00
     */

    private String oid;
    private String order_type;
    private int mid;
    private int doctorteam_id;
    private int doctor_id;
    private String status;
    private int pharmacy_id;
    private String ship_type;
    private int ship_time;
    private String ship_name;
    private String ship_address;
    private String ship_tel;
    private String ship_mobile;
    private String cost_item;
    private String cost_freight;
    private String pay_total;
    private String pay_no;
    private int pay_time;
    private int pay_mid;
    private String pay_type;
    private String diagnosis;
    private String kuaizhao_img;
    private String corntab_status;
    private long addtime;
    private String sex;
    private String qianming;
    private int age;
    private String total_fee;
    private List<GoodsBean> goods;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getDoctorteam_id() {
        return doctorteam_id;
    }

    public void setDoctorteam_id(int doctorteam_id) {
        this.doctorteam_id = doctorteam_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPharmacy_id() {
        return pharmacy_id;
    }

    public void setPharmacy_id(int pharmacy_id) {
        this.pharmacy_id = pharmacy_id;
    }

    public String getShip_type() {
        return ship_type;
    }

    public void setShip_type(String ship_type) {
        this.ship_type = ship_type;
    }

    public int getShip_time() {
        return ship_time;
    }

    public void setShip_time(int ship_time) {
        this.ship_time = ship_time;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getShip_address() {
        return ship_address;
    }

    public void setShip_address(String ship_address) {
        this.ship_address = ship_address;
    }

    public String getShip_tel() {
        return ship_tel;
    }

    public void setShip_tel(String ship_tel) {
        this.ship_tel = ship_tel;
    }

    public String getShip_mobile() {
        return ship_mobile;
    }

    public void setShip_mobile(String ship_mobile) {
        this.ship_mobile = ship_mobile;
    }

    public String getCost_item() {
        return cost_item;
    }

    public void setCost_item(String cost_item) {
        this.cost_item = cost_item;
    }

    public String getCost_freight() {
        return cost_freight;
    }

    public void setCost_freight(String cost_freight) {
        this.cost_freight = cost_freight;
    }

    public String getPay_total() {
        return pay_total;
    }

    public void setPay_total(String pay_total) {
        this.pay_total = pay_total;
    }

    public String getPay_no() {
        return pay_no;
    }

    public void setPay_no(String pay_no) {
        this.pay_no = pay_no;
    }

    public int getPay_time() {
        return pay_time;
    }

    public void setPay_time(int pay_time) {
        this.pay_time = pay_time;
    }

    public int getPay_mid() {
        return pay_mid;
    }

    public void setPay_mid(int pay_mid) {
        this.pay_mid = pay_mid;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getKuaizhao_img() {
        return kuaizhao_img;
    }

    public void setKuaizhao_img(String kuaizhao_img) {
        this.kuaizhao_img = kuaizhao_img;
    }

    public String getCorntab_status() {
        return corntab_status;
    }

    public void setCorntab_status(String corntab_status) {
        this.corntab_status = corntab_status;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQianming() {
        return qianming;
    }

    public void setQianming(String qianming) {
        this.qianming = qianming;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * id : 18
         * oid : 1711141500367936
         * drugs_id : 4
         * drugs_name : 达比加群酯胶囊(泰毕全)
         * mun : 1
         * smun : 9
         * day_mun : 1
         * drugs_spec :
         * drugs_bn : C14201221382
         * corntab_status : 0
         * unit : 盒
         * usetime : ["06:00:00","11:00:00","13:00:00"]
         * dosage_unit : 支
         * dosage_mun : 3
         * image : http://test.haolaoban99.com/uploads/20171023/96b4ba99d63348824986ee9885875393.jpg
         */

        private int id;
        private String oid;
        private int drugs_id;
        private String drugs_name;
        private int mun;
        private int smun;
        private int day_mun;
        private String drugs_spec;
        private String drugs_bn;
        private String corntab_status;
        private String unit;
        private String dosage_unit;
        private int dosage_mun;
        private String image;
        private List<String> usetime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public int getDrugs_id() {
            return drugs_id;
        }

        public void setDrugs_id(int drugs_id) {
            this.drugs_id = drugs_id;
        }

        public String getDrugs_name() {
            return drugs_name;
        }

        public void setDrugs_name(String drugs_name) {
            this.drugs_name = drugs_name;
        }

        public int getMun() {
            return mun;
        }

        public void setMun(int mun) {
            this.mun = mun;
        }

        public int getSmun() {
            return smun;
        }

        public void setSmun(int smun) {
            this.smun = smun;
        }

        public int getDay_mun() {
            return day_mun;
        }

        public void setDay_mun(int day_mun) {
            this.day_mun = day_mun;
        }

        public String getDrugs_spec() {
            return drugs_spec;
        }

        public void setDrugs_spec(String drugs_spec) {
            this.drugs_spec = drugs_spec;
        }

        public String getDrugs_bn() {
            return drugs_bn;
        }

        public void setDrugs_bn(String drugs_bn) {
            this.drugs_bn = drugs_bn;
        }

        public String getCorntab_status() {
            return corntab_status;
        }

        public void setCorntab_status(String corntab_status) {
            this.corntab_status = corntab_status;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDosage_unit() {
            return dosage_unit;
        }

        public void setDosage_unit(String dosage_unit) {
            this.dosage_unit = dosage_unit;
        }

        public int getDosage_mun() {
            return dosage_mun;
        }

        public void setDosage_mun(int dosage_mun) {
            this.dosage_mun = dosage_mun;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<String> getUsetime() {
            return usetime;
        }

        public void setUsetime(List<String> usetime) {
            this.usetime = usetime;
        }
    }
}
