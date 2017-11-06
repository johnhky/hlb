package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.BR;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.ArticleBean;

import java.util.List;

/**
 * Created by heky on 2017/11/2.
 */

public class ClubAdapter extends BaseAdapter {
    private List<ArticleBean.DataBean.ItemsBean> myDatas;
    private Context context;

    public ClubAdapter(List<ArticleBean.DataBean.ItemsBean> myDatas, Context context) {
        this.myDatas = myDatas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_club, null);
            holder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(myDatas.get(position).getImage()).centerCrop().into(holder.iv_title);
        holder.tv_title.setText(myDatas.get(position).getTitle());
        holder.tv_content.setText(myDatas.get(position).getAbstractX());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_title;
        TextView tv_title, tv_content;
    }
}
