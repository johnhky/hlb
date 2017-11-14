package com.hlb.haolaoban.adapter;

import android.content.Context;
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
    private long oneDay = 24 * 60 * 60;

    public UnpayAdapter(List<OrderBean.ItemsBean> myDatas, Context context) {
        this.context = context;
        this.myDatas = myDatas;
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
        double total = Double.parseDouble(myDatas.get(position).getCost_item()) + Double.parseDouble(myDatas.get(position).getCost_freight());
        SpannableString spannableString = new SpannableString("合计: " + total + "元");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        spannableString.setSpan(foregroundColorSpan, 4, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_total.setText(spannableString);
        switch (myDatas.get(position).getStatus()) {
            case "1":

                break;
            case "2":
                long time = myDatas.get(position).getAddtime() + oneDay;
                long currentTime = System.currentTimeMillis() / 1000;
                long surplusTime = currentTime - time;
                CountDownTimer timer = new CountDownTimer(Utils.dateTimeMs(surplusTime), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        BusProvider.getInstance().postEvent(new RefreshOrderEvent());
                    }
                };
                timer.start();
                break;
            case "3":

                break;
            case "4":
                holder.ll_unpay.setVisibility(View.GONE);
                holder.tv_done.setVisibility(View.VISIBLE);
                break;
            case "5":

                break;
        }
        holder.ll_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

        }
    };

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
