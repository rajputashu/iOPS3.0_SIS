package com.sisindia.ai.android.features.addkitrequest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemKitRequestBinding;
import com.sisindia.ai.android.uimodels.KitRequestModel;

public class KitRequestRecyclerAdapter extends BaseRecyclerAdapter<KitRequestModel> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AdapterItemKitRequestBinding binding = (AdapterItemKitRequestBinding) getViewDataBinding(R.layout.adapter_item_kit_request, parent);
        return new KitRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final KitRequestViewHolder viewHolder = (KitRequestViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }

    static class KitRequestViewHolder extends BaseViewHolder<KitRequestModel> {
        private AdapterItemKitRequestBinding itemBinding;


        public KitRequestViewHolder(@NonNull AdapterItemKitRequestBinding itemBinding) {
            super(itemBinding);
            this.itemBinding = itemBinding;
        }

        @Override
        public void onBind(KitRequestModel item) {
            this.itemBinding.setAdapterItem(item);
        }
    }
}
