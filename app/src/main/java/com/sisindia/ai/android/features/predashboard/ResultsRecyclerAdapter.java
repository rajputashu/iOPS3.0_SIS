package com.sisindia.ai.android.features.predashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemPreDashboardResultBinding;

import java.util.ArrayList;

public class ResultsRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<Object> results = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemPreDashboardResultBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_pre_dashboard_result, parent, false);
        return new ResultsRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResultsRecyclerViewHolder viewHolder = (ResultsRecyclerViewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateAdapter(ArrayList<Object> resultList) {
        this.results = resultList;
        notifyDataSetChanged();
    }

    class ResultsRecyclerViewHolder extends RecyclerView.ViewHolder {

        final RecyclerAdapterItemPreDashboardResultBinding binding;

        public ResultsRecyclerViewHolder(@NonNull RecyclerAdapterItemPreDashboardResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
