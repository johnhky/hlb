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
import com.hlb.haolaoban.activity.BigImageActivity;
import com.hlb.haolaoban.bean.HealthRecordBean;

import java.util.List;

/**
 * Created by heky on 2017/11/6.
 */

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MyViewHolder> {

    private List<HealthRecordBean.ItemsBean> mDatas;
    private Context context;

    public MedicalRecordAdapter(List<HealthRecordBean.ItemsBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_medical_record, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_title.setText(mDatas.get(position).getName());
        Glide.with(context).load(mDatas.get(position).getImage()).fitCenter().into(holder.iv_images);
        holder.ll_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = BigImageActivity.intentFor(context, mDatas.get(position).getImage());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_images;
        TextView tv_title;
        LinearLayout ll_record;

        public MyViewHolder(View binding) {
            super(binding);
            iv_images = (ImageView) binding.findViewById(R.id.iv_image);
            tv_title = (TextView) binding.findViewById(R.id.tv_title);
            ll_record = (LinearLayout) binding.findViewById(R.id.ll_record);
        }

    }
}
