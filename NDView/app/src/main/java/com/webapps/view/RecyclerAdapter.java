package com.webapps.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.webapps.view.databinding.MainItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 17/1/2.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    private List<ItemBean> data ;


    public RecyclerAdapter() {
        data = new ArrayList<>();
        for (int i =0;i < 20;i++){
            ItemBean bean = new ItemBean();
            bean.mes = "我是第" + i + "item";
            data.add(bean);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,
                parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.mMainItemBinding.tv.setText(data.get(position).mes);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        MainItemBinding mMainItemBinding;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mMainItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}
