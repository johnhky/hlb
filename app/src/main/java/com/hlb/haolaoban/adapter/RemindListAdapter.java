package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.RemindBean;

import java.util.List;

/**
 * Created by heky on 2017/11/9.
 */

public class RemindListAdapter extends RecyclerView.Adapter<RemindListAdapter.Holder> {

    Context context;
    List<RemindBean.ItemsBean> myDatas;

    public RemindListAdapter(Context context,List<RemindBean.ItemsBean> myDatas) {
        this.context =context;
        this.myDatas = myDatas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_remindlist,null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
            holder.tv_title.setText(myDatas.get(position).getContent());
        holder.tv_time.setText(myDatas.get(position).getStart_his());
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_time;

        public Holder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }

}
