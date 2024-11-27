package com.sisindia.ai.android.features.register;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemCapturedDocumentBinding;
import com.sisindia.ai.android.room.entities.RegisterAttachmentEntity;

public class CapturedDocumentRecyclerAdapter extends BaseRecyclerAdapter<RegisterAttachmentEntity> {

    private RegisterCheckViewListeners viewListeners;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemCapturedDocumentBinding binding = (AdapterItemCapturedDocumentBinding) getViewDataBinding(R.layout.adapter_item_captured_document, parent);
        return new CapturedDocumentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CapturedDocumentViewHolder viewHolder = (CapturedDocumentViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setViewListeners(RegisterCheckViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }

    class CapturedDocumentViewHolder extends BaseViewHolder<RegisterAttachmentEntity> {

        private AdapterItemCapturedDocumentBinding binding;

        public CapturedDocumentViewHolder(@NonNull AdapterItemCapturedDocumentBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(RegisterAttachmentEntity item) {
            binding.setAdapterItem(item);

            binding.ivCapturedDocumentClear.setOnClickListener(v -> {
                viewListeners.onRemoveRegisterAttachment(item);
                remove(item);
            });

            Glide.with(binding.ivCapturedDocument).load(item.localPath).into(binding.ivCapturedDocument);
        }
    }
}
