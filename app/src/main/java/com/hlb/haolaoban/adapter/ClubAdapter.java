package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.ArticleBean;

import java.util.List;

/**
 * Created by heky on 2017/11/2.
 */

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {
    private List<ArticleBean.ItemsBean> myDatas;
    private Context context;

    public ClubAdapter(List<ArticleBean.ItemsBean> myDatas, Context context) {
        this.myDatas = myDatas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_club, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(myDatas.get(position).getImage()).centerCrop().into(holder.iv_title);
        holder.tv_title.setText(myDatas.get(position).getTitle());
        holder.tv_content.setText(myDatas.get(position).getAbstractX());
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_title;
        TextView tv_title, tv_content;

        public ViewHolder(View convertView) {
            super(convertView);
            iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        }
    }
}
