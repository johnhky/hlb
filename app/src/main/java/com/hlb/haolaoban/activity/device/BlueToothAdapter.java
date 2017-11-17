package com.hlb.haolaoban.activity.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;

/**
 * Created by heky on 2017/11/17.
 */

public class BlueToothAdapter extends BaseAdapter {
    List<SearchResult> lists;
    Context context;

    public BlueToothAdapter(List<SearchResult> lists, Context context) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bluetooth_device, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText("蓝牙名称: " + lists.get(position).getName());
        holder.tv_address.setText(lists.get(position).getAddress());
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_address;
    }
}
