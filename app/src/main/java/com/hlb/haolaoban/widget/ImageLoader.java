package com.hlb.haolaoban.widget;

import android.app.Activity;
import android.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by heky on 2017/11/30.
 */

public class ImageLoader {

    private Activity activity;
    private Fragment fragment;
    private RequestManager manager;

    public ImageLoader(Fragment fragment) {
        this.fragment = fragment;
        manager = Glide.with(fragment);
    }

    public ImageLoader(Activity activity) {
        this.activity = activity;
        manager = Glide.with(activity);
    }

    /**
     * 加载普通图片
     *
     * @param object
     * @param
     * @param
     * @param view
     */
    public void loadImage(Object object, ImageView view) {
        if (activity != null && !activity.isDestroyed()) {
            manager.load(object).centerCrop().into(view);
        }
    }


}
