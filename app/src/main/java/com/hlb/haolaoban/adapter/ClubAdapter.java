package com.hlb.haolaoban.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hlb.haolaoban.BR;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.ArticleBean;

import java.util.List;

/**
 * Created by heky on 2017/11/2.
 */

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.MyViewHolder>{
    private List<ArticleBean.DataBean.ItemsBean> myDatas;

    public ClubAdapter(List<ArticleBean.DataBean.ItemsBean> myDatas){
        this.myDatas = myDatas;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_club, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArticleBean.DataBean.ItemsBean itemsBean = myDatas.get(position);
        holder.bind(itemsBean);
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
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
