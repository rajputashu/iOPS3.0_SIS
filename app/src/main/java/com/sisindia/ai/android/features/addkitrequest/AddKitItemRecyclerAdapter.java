package com.sisindia.ai.android.features.addkitrequest;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemAddKitItemBinding;
import com.sisindia.ai.android.uimodels.KitItemModel;

import java.util.ArrayList;
import java.util.List;

public class AddKitItemRecyclerAdapter extends BaseRecyclerAdapter<KitItemModel> {

    private AddKitRequestViewListeners viewListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemAddKitItemBinding binding = (AdapterItemAddKitItemBinding) getViewDataBinding(R.layout.adapter_item_add_kit_item, parent);
        return new AddKitItemRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AddKitItemRecyclerViewHolder viewHolder = (AddKitItemRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    public void setViewListener(AddKitRequestViewListeners viewListener) {
        this.viewListener = viewListener;
    }

    public void updateItem(KitItemModel updatedItem) {
        if (getItems().contains(updatedItem)) {
            int index = getItems().indexOf(updatedItem);
            getItems().remove(index);
            getItems().add(index, updatedItem);
            notifyItemChanged(index);
        }
    }

    public List<KitItemModel> getSelectedItems() {
        List<KitItemModel> list = new ArrayList<>();
        for (KitItemModel model : getItems()) {
            if (model.isSelected) {
                list.add(model);
            }
        }
        return list;
    }

    class AddKitItemRecyclerViewHolder extends BaseViewHolder<KitItemModel> {

        private AdapterItemAddKitItemBinding itemBinding;

        public AddKitItemRecyclerViewHolder(@NonNull AdapterItemAddKitItemBinding itemBinding) {
            super(itemBinding);
            this.itemBinding = itemBinding;
        }

        @Override
        public void onBind(KitItemModel item) {
            itemBinding.setAdapterItem(item);

            //in some cases, it will prevent unwanted situations
            itemBinding.cbKitItem.setOnCheckedChangeListener(null);

            //if true, your checkbox will be selected, else unselected
            itemBinding.cbKitItem.setChecked(item.isSelected);

            itemBinding.cbKitItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && item.sizes != null && item.sizes.size() != 0) {
                    viewListener.showSizesForKitItem(item);
                } else {
                    item.selectedSize = null;
                    itemBinding.tvSelectedSize.setVisibility(View.GONE);
                    itemBinding.tvSelectedSize.setText("");
                    item.isSelected = isChecked;
                }

            });

            itemBinding.tvSelectedSize.setVisibility(item.selectedSize == null ? View.GONE : View.VISIBLE);

            if (item.selectedSize != null && !TextUtils.isEmpty(item.selectedSize.itemSizeName)) {
                itemBinding.tvSelectedSize.setText(item.selectedSize.itemSizeName);
            }
        }
    }
}
