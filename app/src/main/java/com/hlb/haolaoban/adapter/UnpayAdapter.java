package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.PrescriptionDetailActivity;
import com.hlb.haolaoban.bean.OrderBean;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.RefreshOrderEvent;
import com.hlb.haolaoban.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

/**
 * Created by heky on 2017/11/10.
 */

public class UnpayAdapter extends RecyclerView.Adapter<UnpayAdapter.ViewHolder> {

    private List<OrderBean.ItemsBean> myDatas;
    private Context context;

    public UnpayAdapter(List<OrderBean.ItemsBean> myDatas, Context context) {
        this.context = context;
        this.myDatas = myDatas;
    }

    public void update(List<OrderBean.ItemsBean> myDatas) {
        this.myDatas = myDatas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_title.setText(myDatas.get(position).getOid() + "");
        Glide.with(context).load(myDatas.get(position).getKuaizhao_img()).fitCenter().into(holder.iv_drug);
        SpannableString spannableString = new SpannableString("合计: " + myDatas.get(position).getTotal_fee() + "元");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        spannableString.setSpan(foregroundColorSpan, 4, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_total.setText(spannableString);
        switch (myDatas.get(position).getStatus()) {
            case "1":
                holder.tv_done.setText("已付款");
                break;
            case "2":
                holder.ll_unpay.setVisibility(View.VISIBLE);
                holder.tv_done.setVisibility(View.GONE);
                String time = (myDatas.get(position).getAddtime() + 86400) + "";
                long times = Long.parseLong(time);
                long currentTime = System.currentTimeMillis() / 1000;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String data = Utils.formatData(currentTime);
                String date2 = Utils.formatData(times);
                Date date;
                Date date1;
                try {
                    date = df.parse(date2);
                    date1 = df.parse(data);
                    final long timess = date.getTime() - date1.getTime();
                    CountDownTimer timer = new CountDownTimer(timess, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                            long hours = (millisUntilFinished - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                            long minutes = (millisUntilFinished - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                            long seconds = (millisUntilFinished - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
                            holder.tv_time.setText("剩余:" + hours + "时" + minutes + "分" + seconds + "秒");
                        }

                        @Override
                        public void onFinish() {
                            BusProvider.getInstance().postEvent(new RefreshOrderEvent());
                        }
                    };
                    timer.start();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                holder.tv_done.setText("已配送");
                break;
            case "4":
                holder.tv_done.setText("已完成");
                break;
            case "5":
                holder.tv_done.setText("已过期");
                holder.ll_detail.setEnabled(false);
                break;
        }
        holder.ll_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = PrescriptionDetailActivity.intentFor(context, myDatas.get(position).getOid() + "");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_time, tv_total, tv_done;
        Button bt_cancel, bt_pay;
        LinearLayout ll_unpay, ll_detail;
        ImageView iv_drug;

        public ViewHolder(View view) {
            super(view);
            tv_done = (TextView) view.findViewById(R.id.tv_done);
            tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
            bt_pay = (Button) view.findViewById(R.id.bt_pay);
            ll_unpay = (LinearLayout) view.findViewById(R.id.ll_unpay);
            ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
            iv_drug = (ImageView) view.findViewById(R.id.iv_drug);
        }

    }


/*    public static void setItemHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }*/
}
