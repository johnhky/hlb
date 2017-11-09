package com.hlb.haolaoban;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.utils.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    public Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        BusProvider.getInstance().register(this);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("");
            toolbar.setSubtitle("");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isTaskRoot()) {
                        onBackPressed();
                    }
                }
            });
        }
        TextView midTitle = (TextView) findViewById(R.id.tb_title);
        if (midTitle != null) {
            midTitle.setText(getMidTitleString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    public String getMidTitleString() {
        return getString(getMidTitle());
    }

    public int getMidTitle() {
        return R.string.title;
    }


    public void showToast(String msg) {
        Utils.showToast(msg);
    }
    public void startActivity(Class clazz){;
        Intent i = new Intent();
        i.setClass(this,clazz);
        startActivity(i);
    }
}
