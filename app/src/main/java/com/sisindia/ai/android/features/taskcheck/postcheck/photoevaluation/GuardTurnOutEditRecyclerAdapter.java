package com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemGuardTurnOutEditBinding;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;

public class GuardTurnOutEditRecyclerAdapter extends BaseRecyclerAdapter<GuardTurnOutResult.GuardTurnoutModel> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        RecyclerAdapterItemGuardTurnOutEditBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_guard_turn_out_edit, parent, false);
        return new GuardTurnOutEditViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final GuardTurnOutEditViewHolder viewHolder = (GuardTurnOutEditViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    static class GuardTurnOutEditViewHolder extends BaseViewHolder<GuardTurnOutResult.GuardTurnoutModel> {

        private final RecyclerAdapterItemGuardTurnOutEditBinding binding;

        public GuardTurnOutEditViewHolder(RecyclerAdapterItemGuardTurnOutEditBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void onBind(GuardTurnOutResult.GuardTurnoutModel item) {
            binding.setItem(item);

            //in some cases, it will prevent unwanted situations
            binding.scGuardTurnOut.setOnCheckedChangeListener(null);

            //if true, your checkbox will be selected, else unselected
            binding.scGuardTurnOut.setChecked(item.isSelected);

            binding.scGuardTurnOut.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.isSelected = isChecked;
            });


        }
    }
}
