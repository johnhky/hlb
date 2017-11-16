package com.hlb.haolaoban.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/15.
 */

public class ReportBean {

    /**
     * items : [{"id":10,"mid":15,"doctorid":27,"status":"0","content":"运动使我快乐","addtime":1510724247,"doctor_name":"王忠丽"},{"id":9,"mid":15,"doctorid":27,"status":"0","content":"多运动","addtime":1510724231,"doctor_name":"王忠丽"},{"id":8,"mid":15,"doctorid":27,"status":"0","content":"早睡早起身体好","addtime":1510724220,"doctor_name":"王忠丽"},{"id":7,"mid":15,"doctorid":27,"status":"0","content":"06366558556666999966559966698555","addtime":1510212890,"doctor_name":"王忠丽"},{"id":6,"mid":15,"doctorid":28,"status":"0","content":"复古风风","addtime":1510206230,"doctor_name":"叶荣根2"},{"id":5,"mid":15,"doctorid":28,"status":"0","content":"uv个护额u运费v徐火锅","addtime":1510205701,"doctor_name":"叶荣根2"}]
     * total : 6
     * currentPage : 1
     * listRows : 10
     */

    private int total;
    private int currentPage;
    private int listRows;
    private List<ItemsBean> items = new ArrayList<>();

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
         * id : 10
         * mid : 15
         * doctorid : 27
         * status : 0
         * content : 运动使我快乐
         * addtime : 1510724247
         * doctor_name : 王忠丽
         */

        private int id;
        private int mid;
        private int doctorid;
        private String status;
        private String content;
        private long  addtime;
        private String doctor_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getDoctorid() {
            return doctorid;
        }

        public void setDoctorid(int doctorid) {
            this.doctorid = doctorid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getAddtime() {
            return addtime;
        }

        public void setAddtime(long addtime) {
            this.addtime = addtime;
        }

        public String getDoctor_name() {
            return doctor_name;
        }

        public void setDoctor_name(String doctor_name) {
            this.doctor_name = doctor_name;
        }
    }
}
