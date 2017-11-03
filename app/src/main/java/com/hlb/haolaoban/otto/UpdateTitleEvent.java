package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/10/31.
 */

public class UpdateTitleEvent extends Event {

    private String title;

    public UpdateTitleEvent(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
