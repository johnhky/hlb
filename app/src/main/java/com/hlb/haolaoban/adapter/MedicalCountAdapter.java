package com.hlb.haolaoban.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlb.haolaoban.BR;
import com.hlb.haolaoban.R;

import java.util.List;

/**
 * Created by heky on 2017/11/6.
 */

public class MedicalCountAdapter extends RecyclerView.Adapter<MedicalCountAdapter.MyViewHolder> {

    private List<String> mDatas;

    public MedicalCountAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_medical_count, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String data = mDatas.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object object) {
            binding.setVariable(BR.data, object);
            binding.executePendingBindings();
        }
    }
}
