package com.sisindia.ai.android.features.taskcheck.postcheck;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemRegisterCheckBinding;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;

public class RegisterCheckRecyclerAdapter extends BaseRecyclerAdapter<CheckedRegisterEntity> {

    private PostCheckViewListeners viewListeners;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemRegisterCheckBinding binding = (AdapterItemRegisterCheckBinding) getViewDataBinding(R.layout.adapter_item_register_check, parent);
        return new RegisterCheckViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RegisterCheckViewHolder viewHolder = (RegisterCheckViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setViewListeners(PostCheckViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }

    class RegisterCheckViewHolder extends BaseViewHolder<CheckedRegisterEntity> {
        AdapterItemRegisterCheckBinding binding;

        public RegisterCheckViewHolder(@NonNull AdapterItemRegisterCheckBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(CheckedRegisterEntity item) {
            binding.setAdapterItem(item);
            binding.getRoot().setOnClickListener(v -> {
                viewListeners.onCheckedRegisterClick();
            });

            if (item.documents == null || item.documents.size() == 0) {
                binding.ivLeftDocument.setVisibility(View.GONE);
                binding.ivRightDocument.setVisibility(View.GONE);
            } else if (item.documents.size() == 1) {
                binding.ivLeftDocument.setVisibility(View.VISIBLE);
                binding.ivRightDocument.setVisibility(View.GONE);
                Glide.with(binding.ivLeftDocument)
                        .load(item.documents.get(item.documents.size() - 1).localPath)
                        .into(binding.ivLeftDocument);
            } else {
                binding.ivLeftDocument.setVisibility(View.VISIBLE);
                binding.ivRightDocument.setVisibility(View.VISIBLE);
                Glide.with(binding.ivLeftDocument)
                        .load(item.documents.get(item.documents.size() - 1).localPath)
                        .into(binding.ivLeftDocument);
                Glide.with(binding.ivRightDocument)
                        .load(item.documents.get(item.documents.size() - 2).localPath)
                        .into(binding.ivRightDocument);
            }
        }
    }
}
