package com.hlb.haolaoban.activity.mine;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.databinding.ActivityHelpCenterBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/6.
 */

public class HelpCenterActivity extends BaseActivity {
    ActivityHelpCenterBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_center);
        initView();
        initData();
    }

    private void initData() {
        api.getBaseUrl(HttpUrls.getHelpCenter(1)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                ArticleBean data = gson.fromJson(response, ArticleBean.class);
                initViews(data.getItems());
            }
        });
    }

    private void initViews(List<ArticleBean.ItemsBean> mDatas) {
        for (int i = 0; i < mDatas.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_help_center, null);
            final LinearLayout ll_answer = (LinearLayout) view.findViewById(R.id.ll_answer);
            LinearLayout ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            final CheckBox tv_hide = (CheckBox) view.findViewById(R.id.tv_hide);
            tv_content.setText(mDatas.get(i).getAbstractX());
            tv_title.setText(mDatas.get(i).getTitle());
            final Drawable drawableTop = getResources().getDrawable(R.drawable.triangle_hide);
            drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
            final Drawable drawableBottom = getResources().getDrawable(R.drawable.triangle);
            drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
            ll_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ll_answer.getVisibility() == View.VISIBLE) {
                        ll_answer.setVisibility(View.GONE);
                        tv_hide.setCompoundDrawables(null, drawableTop, null, null);
                    } else {
                        ll_answer.setVisibility(View.VISIBLE);
                        tv_hide.setCompoundDrawables(null, drawableBottom, null, null);
                    }
                }
            });
            binding.llQuestion.addView(view);
        }
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("帮助中心");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
