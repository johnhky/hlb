package com.hlb.haolaoban.bean;

import java.io.Serializable;

/**
 * Created by heky on 2017/11/8.
 */

public class ImageBean implements Serializable {

    private String image_name;
    private String image_patg;

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_patg() {
        return image_patg;
    }

    public void setImage_patg(String image_patg) {
        this.image_patg = image_patg;
    }
}
