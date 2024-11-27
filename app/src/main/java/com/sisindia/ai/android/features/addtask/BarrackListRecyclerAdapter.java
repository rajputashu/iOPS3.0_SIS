package com.sisindia.ai.android.features.addtask;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemBarrackSearchBinding;
import com.sisindia.ai.android.room.entities.BarrackEntity;

import java.util.ArrayList;
import java.util.List;

public class BarrackListRecyclerAdapter extends RecyclerView.Adapter implements Filterable {

    private SelectBarrackViewListeners viewListeners;

    private List<BarrackEntity> filteredItems = new ArrayList<>();
    private List<BarrackEntity> actualItems = new ArrayList<>();


    public void setViewListeners(SelectBarrackViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemBarrackSearchBinding binding = (AdapterItemBarrackSearchBinding) getViewDataBinding(R.layout.adapter_item_barrack_search, parent);
        return new BarrackListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BarrackEntity item = filteredItems.get(position);
        final BarrackListViewHolder viewHolder = (BarrackListViewHolder) holder;
        viewHolder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence query) {
                FilterResults results = new FilterResults();
                filteredItems = new ArrayList<>();
                if (TextUtils.isEmpty(query)) {
                    results.count = getItemCount();
                    results.values = actualItems;
                } else {
                    for (BarrackEntity entity : actualItems) {
                        if (entity.name.trim().toLowerCase().contains(query.toString().trim().toLowerCase())) {
                            filteredItems.add(entity);
                        } else {
                            filteredItems.remove(entity);
                        }
                    }
                    results.count = filteredItems.size();
                    results.values = filteredItems;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (List<BarrackEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private ViewDataBinding getViewDataBinding(@LayoutRes int layoutRes, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(inflater, layoutRes, parent, false);
    }

    public void updateAdapter(List<BarrackEntity> itemList) {
        this.actualItems = itemList;
        this.filteredItems = itemList;
        notifyDataSetChanged();
    }


    class BarrackListViewHolder extends RecyclerView.ViewHolder {
        AdapterItemBarrackSearchBinding binding;

        public BarrackListViewHolder(@NonNull AdapterItemBarrackSearchBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }


        public void onBind(BarrackEntity item) {
            binding.setBarrack(item.name);

            binding.getRoot().setOnClickListener(v -> {
                if (viewListeners != null) {
                    viewListeners.onBarrackSelected(item);
                }
            });
        }
    }

}
