package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.MedicalBean;

import java.util.List;

/**
 * Created by heky on 2017/11/9.
 */

public class MedicalAdapter extends RecyclerView.Adapter<MedicalAdapter.ViewHolder> {

    private List<MedicalBean.ItemsBean> myDatas;
    private Context context;

    public MedicalAdapter(List<MedicalBean.ItemsBean> myDatas, Context context) {
        this.myDatas = myDatas;
        this.context = context;
    }

    public void update(List<MedicalBean.ItemsBean> myDatas) {
        this.myDatas = myDatas;
        notifyDataSetChanged();
    }

    @Override
    public MedicalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medical_count, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedicalAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(myDatas.get(position).getImages()).centerCrop().into(holder.iv_image);
        holder.tv_name.setText(myDatas.get(position).getName());
        holder.tv_num.setText(myDatas.get(position).getNum() + "");
        holder.tv_specification.setText(myDatas.get(position).getSpec()+"");
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_num = (TextView) view.findViewById(R.id.tv_num);
            tv_specification = (TextView) view.findViewById(R.id.tv_specification);
        }

        ImageView iv_image;
        TextView tv_name, tv_num, tv_specification;
    }
}
