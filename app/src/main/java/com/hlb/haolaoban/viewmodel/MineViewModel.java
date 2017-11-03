package com.hlb.haolaoban.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.activity.PersonalActivity;

/**
 * Created by heky on 2017/11/1.
 */

public class MineViewModel {

    public void toPersonal(Context context) {
        Intent i = new Intent();
        i.setClass(context, PersonalActivity.class);
        context.startActivity(i);
    }

}
