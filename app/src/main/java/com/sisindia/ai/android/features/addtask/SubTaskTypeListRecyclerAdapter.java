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
import com.sisindia.ai.android.databinding.AdapterItemSubTaskTypeSearchBinding;
import com.sisindia.ai.android.room.entities.LookUpEntity;

import java.util.ArrayList;
import java.util.List;

public class SubTaskTypeListRecyclerAdapter extends RecyclerView.Adapter implements Filterable {

    private AddTaskViewListeners viewListeners;

    private List<LookUpEntity> filteredItems = new ArrayList<>();
    private List<LookUpEntity> actualItems = new ArrayList<>();


    public void setViewListeners(AddTaskViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemSubTaskTypeSearchBinding binding = (AdapterItemSubTaskTypeSearchBinding) getViewDataBinding(R.layout.adapter_item_sub_task_type_search, parent);
        return new SubTaskListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final LookUpEntity item = filteredItems.get(position);
        final SubTaskListViewHolder viewHolder = (SubTaskListViewHolder) holder;
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
                    for (LookUpEntity entity : actualItems) {
                        if (entity.displayName.trim().toLowerCase().contains(query.toString().trim().toLowerCase())) {
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
                filteredItems = (List<LookUpEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private ViewDataBinding getViewDataBinding(@LayoutRes int layoutRes, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(inflater, layoutRes, parent, false);
    }

    public void updateAdapter(List<LookUpEntity> itemList) {
        this.actualItems = itemList;
        this.filteredItems = itemList;
        notifyDataSetChanged();
    }


    class SubTaskListViewHolder extends RecyclerView.ViewHolder {
        AdapterItemSubTaskTypeSearchBinding binding;

        public SubTaskListViewHolder(@NonNull AdapterItemSubTaskTypeSearchBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }


        public void onBind(LookUpEntity item) {
            binding.setSubTaskType(item.displayName);

            binding.getRoot().setOnClickListener(v -> {
                if (viewListeners != null) {
                    viewListeners.onSubTaskTypeSelected(item);
                }
            });
        }
    }

}