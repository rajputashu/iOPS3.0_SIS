package com.sisindia.ai.android.features.taskcheck;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemPostCheckBinding;
import com.sisindia.ai.android.uimodels.CheckedPostModel;

public class PostCheckRecyclerAdapter extends BaseRecyclerAdapter<CheckedPostModel> {

    private DayCheckViewListeners viewListeners;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemPostCheckBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_post_check, parent, false);
        return new DayCheckRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DayCheckRecyclerViewHolder viewHolder = (DayCheckRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setViewListeners(DayCheckViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }


    class DayCheckRecyclerViewHolder extends BaseViewHolder<CheckedPostModel> {
        final RecyclerAdapterItemPostCheckBinding binding;

        DayCheckRecyclerViewHolder(@NonNull RecyclerAdapterItemPostCheckBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(CheckedPostModel item) {
            binding.setAdapterItem(item);
            binding.getRoot().setOnClickListener(v -> {
                viewListeners.onCheckedPostClick(item);
            });
        }
    }
}
