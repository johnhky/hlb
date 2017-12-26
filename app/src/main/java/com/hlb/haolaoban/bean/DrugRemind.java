package com.hlb.haolaoban.bean;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heky on 2017/11/20.
 */

public class DrugRemind extends RealmObject {


    /**
     * id : 15111629053080
     * msg : 1312421414214 的用药提醒~
     * data : {"25200":{"2":{"msg":"摩罗丹(华山牌) 2箱","endtime":"1511594905","stat_his":"07:00"}},"32400":[{"msg":"养胃宁胶囊(天泰) 3粒","endtime":"1511767705","stat_his":"09:00:0"}],"39600":{"3":{"msg":"摩罗丹(华山牌) 2箱","endtime":"1511594905","stat_his":"11:00:0"}},"61200":{"4":{"msg":"摩罗丹(华山牌) 2箱","endtime":"1511594905","stat_his":"17:00:0"}},"72000":{"1":{"msg":"养胃宁胶囊(天泰) 3粒","endtime":"1511767705","stat_his":"20:00:0"}}}
     */
    @PrimaryKey
    private int id;
    private String mid;
    private String msg;
    private String endtime;
    private String stat_his;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStat_his() {
        return stat_his;
    }

    public void setStat_his(String stat_his) {
        this.stat_his = stat_his;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void add(DrugRemind data) {
        this.id = data.getId();
        this.mid = data.getMid();
        this.endtime = data.getEndtime();
        this.msg = data.getMsg();
        this.stat_his = data.getStat_his();
    }

    @Override
    public String toString() {
        return "data{" +
                "id=" + id +
                ", mid='" + mid + '\'' +
                ", msg='" + msg + '\'' +
                ", endtime='" + endtime + '\'' +
                ", stat_his='" + stat_his + '\'' +
                '}';
    }
}
