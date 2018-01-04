package com.hlb.haolaoban.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubListBean {


    /**
     * items : [{"mid":239,"username":"18022441619","name":"好老伴番禺南村俱乐部","address":"广东省广州市番禺区南村镇市广路26号","photo":"http://test.haolaoban99.com/uploads/file/20171128/f22ecc2ad47a29443e290849b9f4dfc1.jpg"}]
     * total : 1
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
         * mid : 239
         * username : 18022441619
         * name : 好老伴番禺南村俱乐部
         * address : 广东省广州市番禺区南村镇市广路26号
         * photo : http://test.haolaoban99.com/uploads/file/20171128/f22ecc2ad47a29443e290849b9f4dfc1.jpg
         */

        private int mid;
        private String username;
        private String name;
        private String address;
        private String photo;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
