package com.hlb.haolaoban.base;

import android.databinding.BaseObservable;

/**
 * Created by heky on 2017/11/2.
 */

public class BaseItem extends BaseObservable implements LayoutId, SortedItem {

    private int itemLayoutId = -1;

    public BaseItem(){

    }

    public BaseItem(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }


    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    @Override
    public int getLayoutId() {
        return getItemLayoutId();
    }

    @Override
    public long getCreated() {
        return 0;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public int getSpan() {
        return 0;
    }

    @Override
    public boolean isUserSelected() {
        return false;
    }
}
