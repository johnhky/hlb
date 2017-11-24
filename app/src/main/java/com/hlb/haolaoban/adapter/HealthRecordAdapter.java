package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.device.BloodPressureActivity;
import com.hlb.haolaoban.bean.HealthBean;

import java.util.List;

/**
 * Created by heky on 2017/11/22.
 */

public class HealthRecordAdapter extends BaseAdapter {


    List<HealthBean> list;
    Context context;

    public HealthRecordAdapter(List<HealthBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void update(List<HealthBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_health_record, null);
            holder.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_data.setText(list.get(position).getType());
        holder.tv_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = BloodPressureActivity.intentFor(v.getContext(), list.get(position).getId() + "");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(i);
            }
        });
        switch (list.get(position).getId()) {
            case 1:
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_blood_pressure);
                Drawable rightDrawable = context.getResources().getDrawable(R.drawable.blood_pressure);
                rightDrawable.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable, null, null);
                break;
            case 2:
                Drawable rightDrawable2 = context.getResources().getDrawable(R.drawable.blood_pressure);
                rightDrawable2.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable2, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_temperature);
                break;
            case 3:
                Drawable rightDrawable3 = context.getResources().getDrawable(R.drawable.heart_rate);
                rightDrawable3.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable3, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_heartrate);
                break;
            case 4:
                Drawable rightDrawable4 = context.getResources().getDrawable(R.drawable.blood_fat);
                rightDrawable4.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable4, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_bloodfat);
                break;
            case 5:
                Drawable rightDrawable5 = context.getResources().getDrawable(R.drawable.movement);
                rightDrawable5.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable5, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_sport);
                break;
            case 6:
                Drawable rightDrawable6 = context.getResources().getDrawable(R.drawable.face);
                rightDrawable6.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable6, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_face);
                break;
            case 7:
                Drawable rightDrawable7 = context.getResources().getDrawable(R.drawable.blood_fat);
                rightDrawable7.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable7, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_bloodfat);
                break;
            case 8:
                Drawable rightDrawable8 = context.getResources().getDrawable(R.drawable.body);
                rightDrawable8.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable8, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_muscle);
                break;
            case 14:
                Drawable rightDrawable9 = context.getResources().getDrawable(R.drawable.nutrition);
                rightDrawable9.setBounds(0, 15, 60, 90);
                holder.tv_data.setCompoundDrawables(null, rightDrawable9, null, null);
                holder.tv_data.setBackgroundResource(R.drawable.shape_health_nutrition);
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_data;
    }

}
