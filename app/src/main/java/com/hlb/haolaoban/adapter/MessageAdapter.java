package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.PrescriptionDetailActivity;
import com.hlb.haolaoban.bean.MessageBean;
import com.hlb.haolaoban.utils.Utils;

import java.util.List;

/**
 * Created by heky on 2017/11/21.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<MessageBean.MsgBean> lists;
    Context context;

    public MessageAdapter(List<MessageBean.MsgBean> lists, Context context) {
        this.context = context;
        this.lists = lists;
    }

    public void update(List<MessageBean.MsgBean> list) {
        this.lists = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_msg, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (lists.get(position).getMode().equals("message")) {
            holder.tv_type.setText("普通消息");
        } else if (lists.get(position).getMode().equals("system")) {
            holder.tv_type.setText("系统消息");
        } else if (lists.get(position).getMode().equals("order")) {
            holder.tv_type.setText("订单消息");
        }
        holder.tv_content.setText(lists.get(position).getMsg());
        if (!TextUtils.isEmpty(lists.get(position).getTime())) {
            long timeStamp = Long.parseLong(lists.get(position).getTime());
            holder.tv_time.setText(Utils.formatData(timeStamp));
        }
        holder.ll_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.get(position).getMode().equals("order")) {
                    if (null != lists.get(position).getData()) {
                        Intent i = PrescriptionDetailActivity.intentFor(context, lists.get(position).getData().getId());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_msg;
        TextView tv_time, tv_content, tv_type;

        public ViewHolder(View view) {
            super(view);
            ll_msg = (LinearLayout) view.findViewById(R.id.ll_msg);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_type = (TextView) view.findViewById(R.id.tv_type);
        }
    }

}
