package com.hlb.haolaoban.bean;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by heky on 2017/11/9.
 */

public class RemindBean {


    /**
     * items : [{"id":172,"mid":143,"type":1,"vid":2,"start_day":0,"start_his":"00:00:9","endtime":1510567467,"status":"1","content":"摩罗丹(华山牌) 2箱","addtime":1510135467},{"id":174,"mid":143,"type":1,"vid":2,"start_day":0,"start_his":"00:00:20","endtime":1510567467,"status":"1","content":"摩罗丹(华山牌) 2箱","addtime":1510135467},{"id":173,"mid":143,"type":1,"vid":2,"start_day":0,"start_his":"00:00:11","endtime":1510567467,"status":"1","content":"摩罗丹(华山牌) 2箱","addtime":1510135467},{"id":169,"mid":143,"type":1,"vid":4,"start_day":0,"start_his":"00:00:17","endtime":1510567467,"status":"1","content":"达比加群酯胶囊(泰毕全) 2箱","addtime":1510135467},{"id":168,"mid":143,"type":1,"vid":4,"start_day":0,"start_his":"00:00:11","endtime":1510567467,"status":"1","content":"达比加群酯胶囊(泰毕全) 2箱","addtime":1510135467},{"id":167,"mid":143,"type":1,"vid":4,"start_day":0,"start_his":"00:00:7","endtime":1510567467,"status":"1","content":"达比加群酯胶囊(泰毕全) 2箱","addtime":1510135467},{"id":171,"mid":143,"type":1,"vid":1,"start_day":0,"start_his":"00:00:20","endtime":1510740267,"status":"1","content":"养胃宁胶囊(天泰) 3粒","addtime":1510135467},{"id":170,"mid":143,"type":1,"vid":1,"start_day":0,"start_his":"00:00:9","endtime":1510740267,"status":"1","content":"养胃宁胶囊(天泰) 3粒","addtime":1510135467}]
     * total : 8
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
         * id : 172
         * mid : 143
         * type : 1
         * vid : 2
         * start_day : 0
         * start_his : 00:00:9
         * endtime : 1510567467
         * status : 1
         * content : 摩罗丹(华山牌) 2箱
         * addtime : 1510135467
         */

        private int id;
        private int mid;
        private int type;
        private int vid;
        private int start_day;
        private String start_his;
        private int endtime;
        private String status;
        private String content;
        private int addtime;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getVid() {
            return vid;
        }

        public void setVid(int vid) {
            this.vid = vid;
        }

        public int getStart_day() {
            return start_day;
        }

        public void setStart_day(int start_day) {
            this.start_day = start_day;
        }

        public String getStart_his() {
            return start_his;
        }

        public void setStart_his(String start_his) {
            this.start_his = start_his;
        }

        public int getEndtime() {
            return endtime;
        }

        public void setEndtime(int endtime) {
            this.endtime = endtime;
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

        public int getAddtime() {
            return addtime;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }
    }
}
