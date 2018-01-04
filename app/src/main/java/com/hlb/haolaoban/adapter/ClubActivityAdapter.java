package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.ClubActivity;
import com.hlb.haolaoban.bean.ClubActivityBean;
import com.hlb.haolaoban.databinding.ItemClubBinding;

import java.util.List;

/**
 * Created by heky on 2018/1/3.
 */

public class ClubActivityAdapter extends RecyclerView.Adapter<ClubActivityAdapter.ViewHolder> {

    Context context;
    List<ClubActivityBean.ItemsBean> list;

    public ClubActivityAdapter(Context context, List<ClubActivityBean.ItemsBean> list) {
        this.context = context;
        this.list = list;
    }

    public void update(List<ClubActivityBean.ItemsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemClubBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_club, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_content.setText(list.get(position).getAbstractX());
        holder.tv_title.setText(list.get(position).getTitle());
        Glide.with(context).load(list.get(position).getImage()).fitCenter().into(holder.iv_image);
        holder.ll_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ClubActivity.intentFor(context, list.get(position).getId() + "");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_detail;
        ImageView iv_image;
        TextView tv_title, tv_content;

        public ViewHolder(View view) {
            super(view);
            ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
