package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.ReportBean;
import com.hlb.haolaoban.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by heky on 2017/11/15.
 */

public class HealthReportAdapter extends RecyclerView.Adapter<HealthReportAdapter.ViewHolder> {

    List<ReportBean.ItemsBean> lists;
    Context context;

    public HealthReportAdapter(Context context, List<ReportBean.ItemsBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_health_report, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_report.setText(lists.get(position).getContent());
        holder.tv_name.setText(lists.get(position).getDoctor_name());
        holder.tv_time.setText(Utils.stampToDate(lists.get(position).getAddtime() + "000"));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_time, tv_report;

        public ViewHolder(View view) {
            super(view);
            tv_report = (TextView) view.findViewById(R.id.tv_report);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

}
