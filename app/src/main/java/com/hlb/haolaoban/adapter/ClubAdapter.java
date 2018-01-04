package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.ClubDetailActivity;
import com.hlb.haolaoban.bean.ArticleBean;
import com.hlb.haolaoban.bean.ClubListBean;

import java.util.List;

/**
 * Created by heky on 2017/11/2.
 */

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {
    private List<ClubListBean.ItemsBean> myDatas;
    private Context context;

    public ClubAdapter(List<ClubListBean.ItemsBean> myDatas, Context context) {
        this.myDatas = myDatas;
        this.context = context;
    }

    public void update(List<ClubListBean.ItemsBean> myDatas) {
        this.myDatas = myDatas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clublist, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(myDatas.get(position).getPhoto()).centerCrop().into(holder.iv_image);
        holder.tv_name.setText(myDatas.get(position).getName()+"");
        holder.tv_address.setText(myDatas.get(position).getAddress()+"");
        holder.tv_phone.setText(myDatas.get(position).getUsername()+"");
        holder.ll_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ClubDetailActivity.intentFor(context,myDatas.get(position).getMid()+"");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_name, tv_address, tv_phone;
        LinearLayout ll_detail;

        public ViewHolder(View convertView) {
            super(convertView);
            ll_detail = (LinearLayout) convertView.findViewById(R.id.ll_detail);
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            tv_name = (TextView) convertView.findViewById(R.id.tv_club_name);
            tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        }
    }

}
