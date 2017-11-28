package com.hlb.haolaoban.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/22.
 */

public class MessageBean {


    /**
     * code : 1
     * msg : [{"id":"20171127120753117355","from":"15","type":"1","msg":"272您将于 12:10进行10086","data":"null","time":"1511755673","taskid":49,"mode":"message"},{"id":"20171127115752816981","from":"15","type":"1","msg":"272您将于 12:10进行10086","data":"null","time":"1511755072","taskid":48,"mode":"message"}]
     */

    private int code;
    private List<MsgBean> msg = new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MsgBean> getMsg() {
        if (null == msg) {
            return new ArrayList<>();
        }
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * id : 20171127120753117355
         * from : 15
         * type : 1
         * msg : 272您将于 12:10进行10086
         * data : null
         * time : 1511755673
         * taskid : 49
         * mode : message
         */

        private String id;
        private String from;
        private String type;
        private String msg;
        private MyData data;
        private String time;
        private int taskid;
        private String mode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public MyData getData() {
            return data;
        }

        public void setData(MyData data) {
            this.data = data;
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

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public static class MyData {
            public String id;
            public String msg;
            public int status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }

}
