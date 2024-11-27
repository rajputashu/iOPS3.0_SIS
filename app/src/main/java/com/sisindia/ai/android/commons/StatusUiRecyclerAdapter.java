package com.sisindia.ai.android.commons;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.EmptyViewHolder;
import com.droidcommons.databinding.RecyclerAdapterEmptyItemBinding;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterStatusDividerBinding;
import com.sisindia.ai.android.databinding.RecyclerAdapterStatusItemBinding;
import com.sisindia.ai.android.features.taskcheck.DayCheckViewListeners;
import com.sisindia.ai.android.uimodels.StatusUIModel;

import java.util.ArrayList;
import java.util.List;

import static com.sisindia.ai.android.uimodels.StatusUIModel.StatusUIViewType.UNKNOWN;
import static com.sisindia.ai.android.uimodels.StatusUIModel.StatusUIViewType.VIEW_TYPE_DIVIDER;
import static com.sisindia.ai.android.uimodels.StatusUIModel.StatusUIViewType.VIEW_TYPE_ITEM;

public class StatusUiRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<StatusUIModel> itemList = new ArrayList<>();
    private DayCheckViewListeners viewListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_DIVIDER.getStatusViewType()) {
            RecyclerAdapterStatusDividerBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_status_divider, parent, false);
            return new StatusUiDividerRecyclerViewHolder(binding);

        } else if (viewType == VIEW_TYPE_ITEM.getStatusViewType()) {
            RecyclerAdapterStatusItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_status_item, parent, false);
            return new StatusUiItemRecyclerViewHolder(binding);

        } else {
            RecyclerAdapterEmptyItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_empty_item, parent, false);
            return new EmptyViewHolder(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final StatusUIModel model = itemList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_ITEM.getStatusViewType()) {
            final StatusUiItemRecyclerViewHolder itemViewHolder = (StatusUiItemRecyclerViewHolder) holder;
            itemViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == VIEW_TYPE_DIVIDER.getStatusViewType()) {
            final StatusUiDividerRecyclerViewHolder dividerViewHolder = (StatusUiDividerRecyclerViewHolder) holder;
            dividerViewHolder.viewHolderBind(model);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).getViewType() == VIEW_TYPE_ITEM) {
            return VIEW_TYPE_ITEM.getStatusViewType();
        } else if (itemList.get(position).getViewType() == VIEW_TYPE_DIVIDER) {
            return VIEW_TYPE_DIVIDER.getStatusViewType();
        } else {
            return UNKNOWN.getStatusViewType();
        }

    }

    public void updateAdapter(ArrayList<StatusUIModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }


    public void setViewListener(DayCheckViewListeners viewListener) {
        this.viewListener = viewListener;
    }

    class StatusUiItemRecyclerViewHolder extends RecyclerView.ViewHolder {

        final RecyclerAdapterStatusItemBinding binding;

        public StatusUiItemRecyclerViewHolder(@NonNull RecyclerAdapterStatusItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void viewHolderBind(StatusUIModel model) {
            binding.setAdapterItem(model);
        }
    }

    class StatusUiDividerRecyclerViewHolder extends RecyclerView.ViewHolder {

        final RecyclerAdapterStatusDividerBinding binding;

        public StatusUiDividerRecyclerViewHolder(@NonNull RecyclerAdapterStatusDividerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void viewHolderBind(StatusUIModel model) {
            binding.setAdapterItem(model);
        }
    }

    class StatusUiDiffUtilCallBack extends DiffUtil.Callback {

        private final List<StatusUIModel> oldList;
        private final List<StatusUIModel> newList;

        public StatusUiDiffUtilCallBack(List<StatusUIModel> oldList, List<StatusUIModel> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

            return oldList.get(oldItemPosition).getValueTxt().equals(newList.get(newItemPosition).getValueTxt()) &&
                    oldList.get(oldItemPosition).getCompletedResId() == newList.get(newItemPosition).getCompletedResId() &&

                    oldList.get(oldItemPosition).isCompleted() == newList.get(newItemPosition).isCompleted();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            final StatusUIModel oldItem = oldList.get(oldItemPosition);
            final StatusUIModel newItem = newList.get(newItemPosition);

            return oldItem.getValueTxt().equals(newItem.getValueTxt()) &&
                    oldItem.getCompletedResId() == newItem.getCompletedResId() &&
                    oldItem.isCompleted() == newItem.isCompleted();

        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }


        /** USAGE
         * */

        /*final StatusUiDiffUtilCallBack diffCallback = new StatusUiDiffUtilCallBack(this.itemList, itemList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.itemList.clear();
        this.itemList.addAll(itemList);
        diffResult.dispatchUpdatesTo(this);*/
    }
}
