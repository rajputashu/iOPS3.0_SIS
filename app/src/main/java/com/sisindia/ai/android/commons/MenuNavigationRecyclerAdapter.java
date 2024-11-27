package com.sisindia.ai.android.commons;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemMenuNavigationBinding;

public class MenuNavigationRecyclerAdapter extends BaseRecyclerAdapter<MenuNavigationItem> {

    private MenuNavigationListeners listeners;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemMenuNavigationBinding binding = (AdapterItemMenuNavigationBinding) getViewDataBinding(R.layout.adapter_item_menu_navigation, parent);
        return new MenuNavigationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuNavigationViewHolder viewHolder = (MenuNavigationViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setListeners(MenuNavigationListeners listeners) {
        this.listeners = listeners;
    }


    class MenuNavigationViewHolder extends BaseViewHolder<MenuNavigationItem> {
        AdapterItemMenuNavigationBinding binding;

        public MenuNavigationViewHolder(@NonNull AdapterItemMenuNavigationBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(MenuNavigationItem item) {
            binding.setAdapterItem(item);
            binding.ivMenuIcon.setImageResource(item.menuIconId);
            binding.getRoot().setOnClickListener(v -> listeners.onMenuNavigationItemClick(item));
        }
    }
}
