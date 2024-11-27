package com.sisindia.ai.android.features.taskcheck.postcheck.postlist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterSitePostItemBinding;
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckViewListeners;
import com.sisindia.ai.android.uimodels.SitePostModel;

public class SitePostListAdapter extends BaseRecyclerAdapter<SitePostModel> {

    private PostCheckViewListeners viewListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerAdapterSitePostItemBinding binding = (RecyclerAdapterSitePostItemBinding)
                getViewDataBinding(R.layout.recycler_adapter_site_post_item, parent);
        return new PrePostCheckViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PrePostCheckViewHolder viewHolder = (PrePostCheckViewHolder) holder;
        viewHolder.onBind(getItem(position));
    }


    public void setViewListener(PostCheckViewListeners viewListener) {
        this.viewListener = viewListener;
    }

    class PrePostCheckViewHolder extends BaseViewHolder<SitePostModel> {

        private RecyclerAdapterSitePostItemBinding binding;

        public PrePostCheckViewHolder(@NonNull RecyclerAdapterSitePostItemBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(SitePostModel item) {
            binding.setAdapterItem(item);

            binding.getRoot().setOnClickListener(v -> {
                if (viewListener != null) {
                    viewListener.onSitePostItemClick(item);
                }
            });
        }
    }
}
