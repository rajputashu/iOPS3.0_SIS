package com.sisindia.ai.android.features.rota;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.ViewPagerAdapterWeekRotaComplianceItemBinding;
import com.sisindia.ai.android.uimodels.WeekRotaCompliance;

public class WeeklyRotaComplianceRecyclerAdapter extends BaseRecyclerAdapter<WeekRotaCompliance> {

    private RotaViewListeners rotaViewListeners;

    public void setRotaViewListeners(RotaViewListeners rotaViewListeners) {
        this.rotaViewListeners = rotaViewListeners;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewPagerAdapterWeekRotaComplianceItemBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.view_pager_adapter_week_rota_compliance_item, parent, false);
        return new WeeklyRotaComplianceRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WeeklyRotaComplianceRecyclerViewHolder viewHolder = (WeeklyRotaComplianceRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }


    class WeeklyRotaComplianceRecyclerViewHolder extends BaseViewHolder<WeekRotaCompliance> {

        final ViewPagerAdapterWeekRotaComplianceItemBinding binding;

        public WeeklyRotaComplianceRecyclerViewHolder(ViewPagerAdapterWeekRotaComplianceItemBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }


        @Override
        public void onBind(WeekRotaCompliance item) {
            binding.setAdapterItem(item);
            binding.getRoot().setOnClickListener(v -> rotaViewListeners.onWeeklyComplianceItemClick());
            if (item.addRota != 0 || item.adhoc != 0) {
                int progress = Math.round(item.completed * 100 / (item.addRota + item.adhoc));
                if (progress == 100) {
                    binding.pbWeekRotaCompliance.setProgress(99);
                } else if (progress == 0) {
                    binding.pbWeekRotaCompliance.setProgress(1);
                } else {
                    binding.pbWeekRotaCompliance.setProgress(progress);
                }
            } else {
                binding.pbWeekRotaCompliance.setProgress(1);
            }
        }
    }
}
