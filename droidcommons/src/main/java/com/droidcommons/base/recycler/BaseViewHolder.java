package com.droidcommons.base.recycler;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<I> extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull ViewDataBinding itemBinding) {
        super(itemBinding.getRoot());
    }

    public abstract void onBind(I item);
}
