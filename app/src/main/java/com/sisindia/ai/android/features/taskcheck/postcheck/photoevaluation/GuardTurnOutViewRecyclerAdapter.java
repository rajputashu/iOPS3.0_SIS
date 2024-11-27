package com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemGuardTurnOutViewBinding;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;

public class GuardTurnOutViewRecyclerAdapter extends BaseRecyclerAdapter<GuardTurnOutResult.GuardTurnoutModel> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        RecyclerAdapterItemGuardTurnOutViewBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_guard_turn_out_view, parent, false);
        return new GuardTurnOutViewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final GuardTurnOutViewViewHolder viewHolder = (GuardTurnOutViewViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    static class GuardTurnOutViewViewHolder extends BaseViewHolder<GuardTurnOutResult.GuardTurnoutModel> {

        private final RecyclerAdapterItemGuardTurnOutViewBinding binding;

        public GuardTurnOutViewViewHolder(RecyclerAdapterItemGuardTurnOutViewBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void onBind(GuardTurnOutResult.GuardTurnoutModel item) {
            binding.setItem(item);
        }
    }
}
