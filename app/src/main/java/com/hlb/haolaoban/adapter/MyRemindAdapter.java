package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.RemindBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/18.
 */

public class MyRemindAdapter extends BaseAdapter {

    List<RemindBean.ItemsBean> lists;
    Context context;

    public MyRemindAdapter(List<RemindBean.ItemsBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    public void update(List<RemindBean.ItemsBean> lists){
        this.lists = lists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (lists.size() > 3) {
            return 3;
        }
        return lists.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_remind, null);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(lists.get(position).getContent());
        if (lists.get(position).getStart_day() == 0) {
            holder.tv_time.setText(lists.get(position).getStart_his());
        }
        if (position == 0) {
            holder.tv_content.setTextSize(18);
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.text_red));
            holder.tv_time.setTextSize(18);
            holder.tv_time.setTextColor(context.getResources().getColor(R.color.text_red));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_content, tv_time;
    }
}