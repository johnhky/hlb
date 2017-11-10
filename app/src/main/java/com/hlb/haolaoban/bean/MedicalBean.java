package com.hlb.haolaoban.bean;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/9.
 */

public class MedicalBean {


    /**
     * items : [{"id":6,"mid":143,"did":4,"num":240,"addtime":1510132236,"name":"达比加群酯胶囊(泰毕全)","images":"http://test.haolaoban99.com/uploads/20171023/96b4ba99d63348824986ee9885875393.jpg","spec":""},{"id":4,"mid":143,"did":1,"num":336,"addtime":1510109592,"name":"养胃宁胶囊(天泰)","images":"http://test.haolaoban99.com/uploads/20170925/deffd030cd6941a45f8b32539866de68.jpg","spec":"0.3g*12s*2板"},{"id":5,"mid":143,"did":2,"num":240,"addtime":1510109592,"name":"摩罗丹(华山牌)","images":"http://test.haolaoban99.com/uploads/20170925/81254b8748b3bbb278bde33b5447ec52.jpg","spec":"72s"}]
     * total : 3
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

    public static class ItemsBean extends BaseItem {
        /**
         * id : 6
         * mid : 143
         * did : 4
         * num : 240
         * addtime : 1510132236
         * name : 达比加群酯胶囊(泰毕全)
         * images : http://test.haolaoban99.com/uploads/20171023/96b4ba99d63348824986ee9885875393.jpg
         * spec :
         */

        private int id;
        private int mid;
        private int did;
        private int num;
        private int addtime;
        private String name;
        private String images;
        private String spec;

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

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getAddtime() {
            return addtime;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_medical_count;
        }
    }
}
