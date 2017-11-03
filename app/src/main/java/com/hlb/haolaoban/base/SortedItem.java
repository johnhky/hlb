package com.hlb.haolaoban.base;

/**
 * Created by heky on 2017/11/2.
 */

public interface SortedItem {
    int getLayoutId();

    long getCreated();

    String getKey();

    String getValue();

    int getSpan();

    boolean isUserSelected();
}
