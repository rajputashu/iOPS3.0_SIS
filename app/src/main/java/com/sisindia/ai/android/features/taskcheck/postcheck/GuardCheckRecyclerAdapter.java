package com.sisindia.ai.android.features.taskcheck.postcheck;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemGuardCheckBinding;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;


public class GuardCheckRecyclerAdapter extends BaseRecyclerAdapter<CheckedGuardItemModel> {

    private PostCheckViewListeners viewListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemGuardCheckBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_guard_check, parent, false);
        return new PostCheckRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PostCheckRecyclerViewHolder viewHolder = (PostCheckRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));

    }

    public void setViewListener(PostCheckViewListeners viewListener) {
        this.viewListener = viewListener;
    }

    class PostCheckRecyclerViewHolder extends BaseViewHolder<CheckedGuardItemModel> {
        final RecyclerAdapterItemGuardCheckBinding binding;

        public PostCheckRecyclerViewHolder(@NonNull RecyclerAdapterItemGuardCheckBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void onBind(CheckedGuardItemModel item) {
            binding.setAdapterItem(item);
            binding.getRoot().setOnClickListener(v -> {
                viewListener.onCheckedGuardClick(item);
            });
        }
    }
}
