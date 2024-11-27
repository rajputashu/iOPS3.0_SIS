package com.sisindia.ai.android.features.predashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemPreDashboardEffortBinding;

import java.util.ArrayList;

public class EffortsRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<Object> efforts = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemPreDashboardEffortBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_pre_dashboard_effort, parent, false);
        return new EffortsRecyclerViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final EffortsRecyclerViewHolder viewHolder = (EffortsRecyclerViewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return efforts.size();
    }

    public void updateAdapter(ArrayList<Object> effortList) {
        this.efforts = effortList;
        notifyDataSetChanged();
    }


    class EffortsRecyclerViewHolder extends RecyclerView.ViewHolder {

        final RecyclerAdapterItemPreDashboardEffortBinding binding;

        public EffortsRecyclerViewHolder(@NonNull RecyclerAdapterItemPreDashboardEffortBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
//recycler_adapter_item_pre_dashboard_effort.xml