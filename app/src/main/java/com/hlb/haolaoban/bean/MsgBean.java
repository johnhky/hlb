package com.hlb.haolaoban.bean;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by heky on 2017/11/15.
 */

public class MsgBean extends RealmObject {


    /**
     * to : 15
     * from : 15
     * type : 2
     * msg : 订单1711070953225776划价成功，请支付~
     * data : {"id":"46","voucherid":"1711070953225776","type":"push_vcall","num":"1","runtime":"1510717242","msg":"订单1711070953225776划价成功，请支付~","rdata":"{\"code\":1,\"desc\":\"ok\"}","data":"{\"secret\":\"4665e16c0046c53f83c5d4ac59bfddfd\",\"method\":\"normal.send\",\"data\":{\"to\":15,\"from\":15,\"type\":2,\"msg\":\"\\u8ba2\\u53551711070953225776\\u5212\\u4ef7\\u6210\\u529f\\uff0c\\u8bf7\\u652f\\u4ed8~\",\"data\":{\"id\":\"1711070953225776\",\"msg\":\"\\u8ba2\\u53551711070953225776\\u5212\\u4ef7\\u6210\\u529f\\uff0c\\u8bf7\\u652f\\u4ed8~\",\"status\":1},\"time\":1510039585}}","status":"1","addtime":"1510039586"}
     * time : 1510717318
     * taskid : 9
     */
    @Index
    private int id;
    private String to;
    private String from;
    private String type;
    private String msg;
    private String time;
    private int taskid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public static class DataBean {
        /**
         * id : 46
         * voucherid : 1711070953225776
         * type : push_vcall
         * num : 1
         * runtime : 1510717242
         * msg : 订单1711070953225776划价成功，请支付~
         * rdata : {"code":1,"desc":"ok"}
         * data : {"secret":"4665e16c0046c53f83c5d4ac59bfddfd","method":"normal.send","data":{"to":15,"from":15,"type":2,"msg":"\u8ba2\u53551711070953225776\u5212\u4ef7\u6210\u529f\uff0c\u8bf7\u652f\u4ed8~","data":{"id":"1711070953225776","msg":"\u8ba2\u53551711070953225776\u5212\u4ef7\u6210\u529f\uff0c\u8bf7\u652f\u4ed8~","status":1},"time":1510039585}}
         * status : 1
         * addtime : 1510039586
         */

        private String id;
        private String voucherid;
        private String type;
        private String num;
        private String runtime;
        private String msg;
        private String rdata;
        private String data;
        private String status;
        private String addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVoucherid() {
            return voucherid;
        }

        public void setVoucherid(String voucherid) {
            this.voucherid = voucherid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getRuntime() {
            return runtime;
        }

        public void setRuntime(String runtime) {
            this.runtime = runtime;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getRdata() {
            return rdata;
        }

        public void setRdata(String rdata) {
            this.rdata = rdata;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
