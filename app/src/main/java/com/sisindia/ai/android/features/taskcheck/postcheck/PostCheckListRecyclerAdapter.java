package com.sisindia.ai.android.features.taskcheck.postcheck;

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
import com.sisindia.ai.android.databinding.RecyclerAdapterItemPostCheckListBinding;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;

public class PostCheckListRecyclerAdapter extends BaseRecyclerAdapter<CheckedPostCheckListEntity> {

    private PostCheckViewListeners viewListeners;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemPostCheckListBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_post_check_list, parent, false);
        return new PostCheckListRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PostCheckListRecyclerViewHolder viewHolder = (PostCheckListRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setViewListeners(PostCheckViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }


    class PostCheckListRecyclerViewHolder extends BaseViewHolder<CheckedPostCheckListEntity> {
        final RecyclerAdapterItemPostCheckListBinding binding;

        PostCheckListRecyclerViewHolder(@NonNull RecyclerAdapterItemPostCheckListBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(CheckedPostCheckListEntity item) {
            binding.setAdapterItem(item);
            if (!TextUtils.isEmpty(item.imageUri))
                Glide.with(binding.ivSiteCheckList).load(item.imageUri).into(binding.ivSiteCheckList);
            binding.getRoot().setOnClickListener(v -> {
                viewListeners.onPostCheckListItemClick(item);
            });
        }
    }
}
