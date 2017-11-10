package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hlb.haolaoban.R;

import java.util.List;

/**
 * Created by heky on 2017/11/10.
 */

public class DrugListAdapter extends BaseAdapter {

    Context context;
    List<String> myDatas;

    public DrugListAdapter(List<String> myDatas, Context context) {
        this.context = context;
        this.myDatas = myDatas;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drug, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_drugName);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_take = (TextView) convertView.findViewById(R.id.tv_take);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
                holder.tv_name.setText(myDatas.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_num, tv_take;
    }
}
