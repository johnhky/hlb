package com.hlb.haolaoban.bean;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseItem;

import java.util.List;

/**
 * Created by heky on 2017/11/2.
 */

public class ArticleBean {

    /**
     * code : 1
     * msg : 成功
     * data : {"items":[{"id":21,"catid":9,"title":"20150831养生堂视频和笔记:王庆国,人参的功效,过食人参的表现","files":"http://test.haolaoban99.com/uploads/file/20171102/ae83b4ff3f5f5f867b8856a91fe71770.mp3","image":"http://test.haolaoban99.com/uploads/file/20171102/90a3c617bbe48fb93eaa7c23bf646b9b.jpg","author":"王庆国","abstract":"本页提供2015年8月31日北京卫视（BTV）养生堂节目视频在线观看和重点笔记，节目请到的嘉宾是王庆国。主题是《情暖老区 走进临江-补正气》。主要介绍人参的神奇功效，人参银耳汤的制作方法，治疗失眠方，过食人参的表现等相关内容，百年养生网养生堂栏目提供视频全集的在线观看和主要内容介绍（节目要点笔记）。\r\n\r\n王庆国：国家级名老中医。\r\n\r\n国家级名老中医王庆国，随《养身堂》栏目组来到了吉林临江。这里山清水秀、人杰地灵，在长白山深处还藏着一味神秘中药。王老对这味中药评价极高，称它\u201c药效第一，功效多样\u201d。这种中药究竟是什么？日常养生保健如何使用这种药材呢？\r\n\r\n答案将在今天的节目中揭晓。敬请收看《情暖老区走进临江-补正气》。","ishow":"1","addtime":1509593604}],"total":3,"currentPage":1,"listRows":10}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * items : [{"id":21,"catid":9,"title":"20150831养生堂视频和笔记:王庆国,人参的功效,过食人参的表现","files":"http://test.haolaoban99.com/uploads/file/20171102/ae83b4ff3f5f5f867b8856a91fe71770.mp3","image":"http://test.haolaoban99.com/uploads/file/20171102/90a3c617bbe48fb93eaa7c23bf646b9b.jpg","author":"王庆国","abstract":"本页提供2015年8月31日北京卫视（BTV）养生堂节目视频在线观看和重点笔记，节目请到的嘉宾是王庆国。主题是《情暖老区 走进临江-补正气》。主要介绍人参的神奇功效，人参银耳汤的制作方法，治疗失眠方，过食人参的表现等相关内容，百年养生网养生堂栏目提供视频全集的在线观看和主要内容介绍（节目要点笔记）。\r\n\r\n王庆国：国家级名老中医。\r\n\r\n国家级名老中医王庆国，随《养身堂》栏目组来到了吉林临江。这里山清水秀、人杰地灵，在长白山深处还藏着一味神秘中药。王老对这味中药评价极高，称它\u201c药效第一，功效多样\u201d。这种中药究竟是什么？日常养生保健如何使用这种药材呢？\r\n\r\n答案将在今天的节目中揭晓。敬请收看《情暖老区走进临江-补正气》。","ishow":"1","addtime":1509593604}]
         * total : 3
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

        public static class ItemsBean extends BaseItem{
            /**
             * id : 21
             * catid : 9
             * title : 20150831养生堂视频和笔记:王庆国,人参的功效,过食人参的表现
             * files : http://test.haolaoban99.com/uploads/file/20171102/ae83b4ff3f5f5f867b8856a91fe71770.mp3
             * image : http://test.haolaoban99.com/uploads/file/20171102/90a3c617bbe48fb93eaa7c23bf646b9b.jpg
             * author : 王庆国
             * abstract : 本页提供2015年8月31日北京卫视（BTV）养生堂节目视频在线观看和重点笔记，节目请到的嘉宾是王庆国。主题是《情暖老区 走进临江-补正气》。主要介绍人参的神奇功效，人参银耳汤的制作方法，治疗失眠方，过食人参的表现等相关内容，百年养生网养生堂栏目提供视频全集的在线观看和主要内容介绍（节目要点笔记）。

             王庆国：国家级名老中医。

             国家级名老中医王庆国，随《养身堂》栏目组来到了吉林临江。这里山清水秀、人杰地灵，在长白山深处还藏着一味神秘中药。王老对这味中药评价极高，称它“药效第一，功效多样”。这种中药究竟是什么？日常养生保健如何使用这种药材呢？

             答案将在今天的节目中揭晓。敬请收看《情暖老区走进临江-补正气》。
             * ishow : 1
             * addtime : 1509593604
             */

            private int id;
            private int catid;
            private String title;
            private String files;
            private String image;
            private String author;
            @SerializedName("abstract")
            private String abstractX;
            private String ishow;
            private int addtime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCatid() {
                return catid;
            }

            public void setCatid(int catid) {
                this.catid = catid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFiles() {
                return files;
            }

            public void setFiles(String files) {
                this.files = files;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getAbstractX() {
                return abstractX;
            }

            public void setAbstractX(String abstractX) {
                this.abstractX = abstractX;
            }

            public String getIshow() {
                return ishow;
            }

            public void setIshow(String ishow) {
                this.ishow = ishow;
            }

            public int getAddtime() {
                return addtime;
            }

            public void setAddtime(int addtime) {
                this.addtime = addtime;
            }


            @Override
            public int getLayoutId() {
                return R.layout.item_club;
            }
        }
    }
}
