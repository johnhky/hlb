package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/10/31.
 */

public class ConnectVideoEvent extends Event {

    private String title;

    public ConnectVideoEvent(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
