package com.hlb.haolaoban.bean;

import java.util.List;

/**
 * Created by heky on 2017/11/14.
 */

public class OrderBean {


    /**
     * items : [{"oid":1711101756347265,"order_type":"prescription","mid":15,"doctorteam_id":18,"doctor_id":27,"status":"2","pharmacy_id":0,"ship_type":"1","ship_time":0,"ship_name":"梁达免","ship_address":"广东省廉江市车板镇大坝村委员会文头岭村 96 号","ship_tel":"13500000000","ship_mobile":"13500000000","cost_item":"2000.00","cost_freight":"10.00","pay_total":"0.00","pay_no":"","pay_time":0,"pay_mid":0,"pay_type":"1","diagnosis":"严重感冒","kuaizhao_img":"","corntab_status":"0","addtime":1510307794}]
     * total : 1
     * currentPage : 1
     * listRows : 10
     */

    private int total;
    private int currentPage;
    private int listRows;
    private List<ItemsBean> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getListRows() {
        return listRows;
    }

    public void setListRows(int listRows) {
        this.listRows = listRows;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * oid : 1711101756347265
         * order_type : prescription
         * mid : 15
         * doctorteam_id : 18
         * doctor_id : 27
         * status : 2
         * pharmacy_id : 0
         * ship_type : 1
         * ship_time : 0
         * ship_name : 梁达免
         * ship_address : 广东省廉江市车板镇大坝村委员会文头岭村 96 号
         * ship_tel : 13500000000
         * ship_mobile : 13500000000
         * cost_item : 2000.00
         * cost_freight : 10.00
         * pay_total : 0.00
         * pay_no :
         * pay_time : 0
         * pay_mid : 0
         * pay_type : 1
         * diagnosis : 严重感冒
         * kuaizhao_img :
         * corntab_status : 0
         * addtime : 1510307794
         * total_fee:￥126.00
         */

        private long oid;
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
        private String total_fee;

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public long getOid() {
            return oid;
        }

        public void setOid(long oid) {
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
    }
}
