package com.hlb.haolaoban.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/9.
 */

public class HealthRecordBean {


    /**
     * items : [{"id":3,"mid":15,"name":"病历2222","image":"http://test.haolaoban99.comhttp://shenzhen.southcn.com/content/images/attachement/jpg/site4/20160122/f8bc12817281180bdf934d.jpg","type":"1","addtime":1509522654},{"id":2,"mid":15,"name":"胃镜","image":"http://test.haolaoban99.com/uploads/20170926/00615ff5a1a995e1fea3fe16c311e8a6.jpg","type":"1","addtime":1506408697}]
     * total : 2
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
         * id : 3
         * mid : 15
         * name : 病历2222
         * image : http://test.haolaoban99.comhttp://shenzhen.southcn.com/content/images/attachement/jpg/site4/20160122/f8bc12817281180bdf934d.jpg
         * type : 1
         * addtime : 1509522654
         */

        private int id;
        private int mid;
        private String name;
        private String image;
        private String type;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getAddtime() {
            return addtime;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }
    }
}
