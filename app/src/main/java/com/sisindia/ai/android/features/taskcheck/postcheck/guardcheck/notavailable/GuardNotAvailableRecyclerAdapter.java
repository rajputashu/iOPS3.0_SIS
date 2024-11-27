package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemGuardNotFountBinding;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.CheckGuardViewListeners;
import com.sisindia.ai.android.room.entities.LookUpEntity;

public class GuardNotAvailableRecyclerAdapter extends BaseRecyclerAdapter<LookUpEntity> {

    private int lastCheckedPosition = -1;
    private CheckGuardViewListeners viewListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemGuardNotFountBinding binding = (AdapterItemGuardNotFountBinding) getViewDataBinding(R.layout.adapter_item_guard_not_fount, parent);
        return new GuardNotAvailableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GuardNotAvailableViewHolder viewHolder = (GuardNotAvailableViewHolder) holder;
        viewHolder.binding.rbGuardNotFound.setChecked(position == lastCheckedPosition);
        viewHolder.onBind(getItem(position));
    }

    public void setViewListener(CheckGuardViewListeners viewListener) {
        this.viewListener = viewListener;
    }

    class GuardNotAvailableViewHolder extends BaseViewHolder<LookUpEntity> {
        private AdapterItemGuardNotFountBinding binding;

        public GuardNotAvailableViewHolder(@NonNull AdapterItemGuardNotFountBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(LookUpEntity item) {
            binding.setAdapterItem(item);
            binding.rbGuardNotFound.setOnClickListener(v -> {
                lastCheckedPosition = getAdapterPosition();
                viewListener.onGuardNotAvailableStatusChanged(item);
                notifyDataSetChanged();

            });
        }
    }
}
