package com.sisindia.ai.android.features.register;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterPostRegisterItemBinding;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;

public class PostRegisterRecyclerAdapter extends BaseRecyclerAdapter<CheckedRegisterEntity> {

    private RegisterCheckViewListeners viewListeners;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerAdapterPostRegisterItemBinding binding = (RecyclerAdapterPostRegisterItemBinding) getViewDataBinding(R.layout.recycler_adapter_post_register_item, parent);

        return new PostRegisterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostRegisterViewHolder viewHolder = (PostRegisterViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setViewListeners(RegisterCheckViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }

    class PostRegisterViewHolder extends BaseViewHolder<CheckedRegisterEntity> {
        private RecyclerAdapterPostRegisterItemBinding itemBinding;

        public PostRegisterViewHolder(@NonNull RecyclerAdapterPostRegisterItemBinding itemBinding) {
            super(itemBinding);
            this.itemBinding = itemBinding;
        }

        @Override
        public void onBind(CheckedRegisterEntity item) {
            itemBinding.setAdapteritem(item);

            itemBinding.llLeftUpload.setOnClickListener(v -> viewListeners.onRegisterItemClick(item));
            itemBinding.llRightUpload.setOnClickListener(v -> viewListeners.onRegisterItemClick(item));

            //in some cases, it will prevent unwanted situations
            itemBinding.switchMissing.setOnCheckedChangeListener(null);

            //if true, your checkbox will be selected, else unselected
            itemBinding.switchMissing.setChecked(item.isMissing);

            itemBinding.switchMissing.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.isMissing = isChecked;
            });

            itemBinding.ivLeftDocumentClear.setOnClickListener(v -> {
                viewListeners.onRegisterItemClick(item);
            });

            itemBinding.ivRightDocumentClear.setOnClickListener(v -> {
                viewListeners.onRegisterItemClick(item);
            });

            if (item.documents == null || item.documents.size() == 0) {
                itemBinding.ivLeftDocument.setVisibility(View.GONE);
                itemBinding.ivLeftDocumentClear.setVisibility(View.GONE);
                itemBinding.ivRightDocument.setVisibility(View.GONE);
                itemBinding.ivRightDocumentClear.setVisibility(View.GONE);
                itemBinding.llLeftUpload.setVisibility(View.VISIBLE);
                itemBinding.llRightUpload.setVisibility(View.VISIBLE);
            } else if (item.documents.size() == 1) {
                itemBinding.ivLeftDocument.setVisibility(View.VISIBLE);
                itemBinding.ivLeftDocumentClear.setVisibility(View.VISIBLE);
                itemBinding.ivRightDocument.setVisibility(View.GONE);
                itemBinding.ivRightDocumentClear.setVisibility(View.GONE);

                itemBinding.llLeftUpload.setVisibility(View.GONE);
                itemBinding.llRightUpload.setVisibility(View.VISIBLE);

                Glide.with(itemBinding.ivLeftDocument)
                        .load(item.documents.get(item.documents.size() - 1).localPath)
                        .into(itemBinding.ivLeftDocument);
            } else {
                itemBinding.ivLeftDocument.setVisibility(View.VISIBLE);
                itemBinding.ivLeftDocumentClear.setVisibility(View.VISIBLE);
                itemBinding.ivRightDocument.setVisibility(View.VISIBLE);
                itemBinding.ivRightDocumentClear.setVisibility(View.VISIBLE);
                itemBinding.llLeftUpload.setVisibility(View.GONE);
                itemBinding.llRightUpload.setVisibility(View.GONE);

                Glide.with(itemBinding.ivLeftDocument)
                        .load(item.documents.get(item.documents.size() - 1).localPath)
                        .into(itemBinding.ivLeftDocument);
                Glide.with(itemBinding.ivRightDocument)
                        .load(item.documents.get(item.documents.size() - 2).localPath)
                        .into(itemBinding.ivRightDocument);
            }

        }
    }
}
