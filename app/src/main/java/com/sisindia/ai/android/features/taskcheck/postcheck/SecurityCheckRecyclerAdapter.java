package com.sisindia.ai.android.features.taskcheck.postcheck;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterItemSecurityCheckBinding;
import com.sisindia.ai.android.uimodels.SecurityRiskModel;


public class SecurityCheckRecyclerAdapter extends BaseRecyclerAdapter<SecurityRiskModel> {


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterItemSecurityCheckBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_item_security_check, parent, false);
        return new SecurityCheckRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SecurityCheckRecyclerViewHolder viewHolder = (SecurityCheckRecyclerViewHolder) holder;
        viewHolder.onBind(getItem(position));

    }

    class SecurityCheckRecyclerViewHolder extends BaseViewHolder<SecurityRiskModel> {
        final RecyclerAdapterItemSecurityCheckBinding binding;

        public SecurityCheckRecyclerViewHolder(@NonNull RecyclerAdapterItemSecurityCheckBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void onBind(SecurityRiskModel item) {
            binding.setAdapterItem(item);
            if (!TextUtils.isEmpty(item.localFilePath) && Uri.parse(item.localFilePath) != null) {
                Glide.with(binding.ivRiskImage).load(Uri.parse(item.localFilePath)).into(binding.ivRiskImage);
            }
        }
    }
}
