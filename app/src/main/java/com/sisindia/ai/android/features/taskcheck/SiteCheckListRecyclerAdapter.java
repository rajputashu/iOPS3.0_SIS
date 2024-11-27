package com.sisindia.ai.android.features.taskcheck;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemSiteCheckListBinding;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;

public class SiteCheckListRecyclerAdapter extends BaseRecyclerAdapter<CheckedSiteCheckListEntity> {

    private DayCheckViewListeners viewListeners;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemSiteCheckListBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_site_check_list, parent, false);
        return new SiteCheckListRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SiteCheckListRecyclerViewHolder viewHolder = (SiteCheckListRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setViewListeners(DayCheckViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }


    class SiteCheckListRecyclerViewHolder extends BaseViewHolder<CheckedSiteCheckListEntity> {
        final RecyclerAdapterItemSiteCheckListBinding binding;

        SiteCheckListRecyclerViewHolder(@NonNull RecyclerAdapterItemSiteCheckListBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(CheckedSiteCheckListEntity item) {
            binding.setAdapterItem(item);
            if (!TextUtils.isEmpty(item.imageUri))
                Glide.with(binding.ivSiteCheckList).load(item.imageUri).into(binding.ivSiteCheckList);
            binding.getRoot().setOnClickListener(v -> {
                viewListeners.onSiteCheckListItemClick(item);
            });
        }
    }
}
