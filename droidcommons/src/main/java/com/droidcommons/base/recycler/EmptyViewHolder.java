package com.droidcommons.base.recycler;

import androidx.annotation.NonNull;

import com.droidcommons.databinding.RecyclerAdapterEmptyItemBinding;

public class EmptyViewHolder extends BaseViewHolder<Object> {
    RecyclerAdapterEmptyItemBinding binding;

    public EmptyViewHolder(@NonNull RecyclerAdapterEmptyItemBinding itemBinding) {
        super(itemBinding);
        this.binding = binding;
    }

    @Override
    public void onBind(Object item) {

    }
}
