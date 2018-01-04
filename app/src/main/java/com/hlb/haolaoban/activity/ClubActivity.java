package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.ActivityDetailBean;
import com.hlb.haolaoban.databinding.ActivityMClubDetailBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubActivity extends BaseActivity {

    ActivityMClubDetailBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();


    public static Intent intentFor(Context context, String id) {
        Intent i = new Intent(context, ClubActivity.class);
        i.putExtra(Constants.DATA, id);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_m_club_detail);
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("活动");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detail();
    }

    private void detail() {
        api.getBaseUrl(HttpUrls.clubActivityDetail(id())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                ActivityDetailBean data = gson.fromJson(response, ActivityDetailBean.class);
                binding.tvContent.setText(Html.fromHtml(data.getContent(), new ImgGetter(binding.tvContent), null));
                binding.tvTime.setText(Utils.stampToDate(data.getAddtime() + "000"));
                binding.tvClubName.setText(data.getClub_name());
                binding.tvTitle.setText(data.getTitle());
                /*Glide.with(mActivity).load(data.getImage()).fitCenter().into(binding.ivImage);*/
            }
        });
    }

    class ImgGetter implements Html.ImageGetter {

        private URLDrawable urlDrawable = null;
        private TextView textView;

        public ImgGetter( TextView textView) {
            this.textView = textView;
        }

        @Override
        public Drawable getDrawable(final String source) {
            urlDrawable = new URLDrawable();

            Glide.with(mActivity).load(source).asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    urlDrawable.bitmap = resource;
                    urlDrawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                    textView.invalidate();
                    textView.setText(textView.getText());//不加这句显示不出来图片，原因不详
                }
            });
            return urlDrawable;
        }

        public class URLDrawable extends BitmapDrawable {
            public Bitmap bitmap;

            @Override
            public void draw(Canvas canvas) {
                super.draw(canvas);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, 0, 0, getPaint());
                }
            }
        }
    }

    private String id() {
        return getIntent().getStringExtra(Constants.DATA);
    }
}
