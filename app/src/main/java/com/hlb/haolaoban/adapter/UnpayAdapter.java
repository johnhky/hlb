package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hlb.haolaoban.R;

import java.util.List;

/**
 * Created by heky on 2017/11/10.
 */

public class UnpayAdapter extends RecyclerView.Adapter<UnpayAdapter.ViewHolder> {

    private List<String> myDatas;
    private Context context;

    public UnpayAdapter(List<String> myDatas, Context context) {
        this.context = context;
        this.myDatas = myDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_title.setText(myDatas.get(position));
        DrugListAdapter mAdapter = new DrugListAdapter(myDatas, context);
        holder.recyclerView.setAdapter(mAdapter);
        setItemHeight(holder.recyclerView);
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_remind, tv_time, tv_done;
        Button bt_cancel, bt_pay;
        LinearLayout ll_unpay;
        ListView recyclerView;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_remind = (TextView) view.findViewById(R.id.tv_remind);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_done = (TextView) view.findViewById(R.id.tv_done);
            bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
            bt_pay = (Button) view.findViewById(R.id.bt_pay);
            ll_unpay = (LinearLayout) view.findViewById(R.id.ll_unpay);
            recyclerView = (ListView) view.findViewById(R.id.list_view);
        }

    }


    public static void setItemHeight(ListView listView) {
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
    }
}
