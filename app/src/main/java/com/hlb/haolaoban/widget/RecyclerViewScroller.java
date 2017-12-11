package com.hlb.haolaoban.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by heky on 2017/12/11.
 */

public abstract class RecyclerViewScroller extends RecyclerView.OnScrollListener {

    int lastVisibleItem  = 0;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView.Adapter mAdapter;

    public RecyclerViewScroller(
            LinearLayoutManager linearLayoutManager, RecyclerView.Adapter mAdapter) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.mAdapter = mAdapter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState ==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==mAdapter.getItemCount()){
            onLoadMore();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        lastVisibleItem  = mLinearLayoutManager.findLastVisibleItemPosition();
    }

    public abstract void onLoadMore();
}
