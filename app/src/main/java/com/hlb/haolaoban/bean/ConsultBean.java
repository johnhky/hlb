package com.hlb.haolaoban.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by heky on 2017/12/28.
 */

public class ConsultBean implements Serializable{


    /**
     * mode : consult
     * taskid : 2
     * data : {"consult_No":1,"type":1,"time":1514428165,"owner":328,"from":{"name":"屠呦呦","headlogo":"http://test.haolaoban99.com/uploads/file/20171221/767248a996303467df2c8704a2ec58ea.jpg"},"to":{"name":null,"headlogo":null}}
     */

    private String mode;
    private int taskid;
    private DataBean data;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * consult_No : 1
         * type : 1
         * time : 1514428165
         * owner : 328
         * from : {"name":"屠呦呦","headlogo":"http://test.haolaoban99.com/uploads/file/20171221/767248a996303467df2c8704a2ec58ea.jpg"}
         * to : {"name":null,"headlogo":null}
         */

        private int consult_No;
        private int type;
        private int time;
        private int owner;
        private String msg;
        private FromBean from;
        private ToBean to;
        @SerializedName("long")
        private String longs;
        public String getLongs() {
            return longs;
        }

        public void setLongs(String longs) {
            this.longs = longs;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getConsult_No() {
            return consult_No;
        }

        public void setConsult_No(int consult_No) {
            this.consult_No = consult_No;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getOwner() {
            return owner;
        }

        public void setOwner(int owner) {
            this.owner = owner;
        }

        public FromBean getFrom() {
            return from;
        }

        public void setFrom(FromBean from) {
            this.from = from;
        }

        public ToBean getTo() {
            return to;
        }

        public void setTo(ToBean to) {
            this.to = to;
        }

        public static class FromBean implements Serializable{
            /**
             * name : 屠呦呦
             * headlogo : http://test.haolaoban99.com/uploads/file/20171221/767248a996303467df2c8704a2ec58ea.jpg
             */

            private String name;
            private String headlogo;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHeadlogo() {
                return headlogo;
            }

            public void setHeadlogo(String headlogo) {
                this.headlogo = headlogo;
            }


            @Override
            public String toString() {
                return "FromBean{" +
                        "name='" + name + '\'' +
                        ", headlogo='" + headlogo + '\'' +
                        '}';
            }
        }

        public static class ToBean implements Serializable{
            /**
             * name : null
             * headlogo : null
             */

            private String name;
            private String headlogo;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHeadlogo() {
                return headlogo;
            }

            public void setHeadlogo(String headlogo) {
                this.headlogo = headlogo;
            }


            @Override
            public String toString() {
                return "ToBean{" +
                        "name='" + name + '\'' +
                        ", headlogo='" + headlogo + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "consult_No=" + consult_No +
                    ", type=" + type +
                    ", time=" + time +
                    ", owner=" + owner +
                    ", msg='" + msg + '\'' +
                    ", from=" + from +
                    ", to=" + to +
                    ", longs='" + longs + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConsultBean{" +
                "mode='" + mode + '\'' +
                ", taskid=" + taskid +
                ", data=" + data +
                '}';
    }
}
