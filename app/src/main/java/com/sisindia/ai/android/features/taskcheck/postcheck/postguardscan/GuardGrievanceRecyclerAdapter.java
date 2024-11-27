package com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemGuardGrievanceBinding;
import com.sisindia.ai.android.models.GrievanceModel;

public class GuardGrievanceRecyclerAdapter extends BaseRecyclerAdapter<GrievanceModel> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemGuardGrievanceBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_guard_grievance, parent, false);
        return new GuardSummaryRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final GuardSummaryRecyclerViewHolder viewHolder = (GuardSummaryRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }


    class GuardSummaryRecyclerViewHolder extends BaseViewHolder<GrievanceModel> {
        final RecyclerAdapterItemGuardGrievanceBinding binding;

        public GuardSummaryRecyclerViewHolder(@NonNull RecyclerAdapterItemGuardGrievanceBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void onBind(GrievanceModel item) {
            binding.setAdapterItem(item);
        }
    }
}
