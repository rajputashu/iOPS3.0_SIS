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

import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemSiteSearchBinding;
import com.sisindia.ai.android.room.entities.SiteEntity;

import java.util.ArrayList;
import java.util.List;

public class SiteListRecyclerAdapter extends RecyclerView.Adapter implements Filterable {

    private AddTaskViewListeners viewListeners;

    private List<SiteEntity> actualItems = new ArrayList<>();
    private List<SiteEntity> filteredItems = new ArrayList<>();

    public void setViewListeners(AddTaskViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemSiteSearchBinding binding = (AdapterItemSiteSearchBinding) getViewDataBinding(R.layout.adapter_item_site_search, parent);
        return new SiteListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SiteEntity item = filteredItems.get(position);
        final SiteListViewHolder viewHolder = (SiteListViewHolder) holder;
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
                    for (SiteEntity entity : actualItems) {
                        if (entity.siteName.trim().toLowerCase().contains(query.toString().trim().toLowerCase())) {
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
                filteredItems = (List<SiteEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private ViewDataBinding getViewDataBinding(@LayoutRes int layoutRes, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(inflater, layoutRes, parent, false);
    }

    public void updateAdapter(List<SiteEntity> list) {
        this.actualItems = list;
        this.filteredItems = list;
        notifyDataSetChanged();
    }

    class SiteListViewHolder extends BaseViewHolder<SiteEntity> {
        AdapterItemSiteSearchBinding binding;

        public SiteListViewHolder(@NonNull AdapterItemSiteSearchBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(SiteEntity item) {
            binding.setAdapterItem(item);

            binding.getRoot().setOnClickListener(v -> {
                if (viewListeners != null) {
                    viewListeners.onSiteSelected(item);
                }
            });
        }
    }

}
