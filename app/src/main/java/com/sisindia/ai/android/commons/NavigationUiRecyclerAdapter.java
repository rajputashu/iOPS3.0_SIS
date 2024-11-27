package com.sisindia.ai.android.commons;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.EmptyViewHolder;
import com.droidcommons.databinding.RecyclerAdapterEmptyItemBinding;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterNavigationItemBinding;
import com.sisindia.ai.android.databinding.RowNavigationItemWithStatusBinding;
import com.sisindia.ai.android.uimodels.NavigationUIModel;

import java.util.ArrayList;

import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_LANDLORD;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_OTHERS;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_SPACE;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_STRENGTH;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.DAY_CHECK_CLIENT_HANDSHAKE;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.DAY_CHECK_POST;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.DAY_CHECK_STRENGTH;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.POST_CHECK_GRIEVANCE;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.POST_CHECK_GUARD;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.POST_CHECK_KIT_REQUEST;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.POST_CHECK_REGISTER;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.POST_CHECK_SECURITY_RISK;
import static com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.UNKNOWN;

public class NavigationUiRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<NavigationUIModel> itemList = new ArrayList<>();
    private NavigationViewListeners viewListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DAY_CHECK_POST.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == DAY_CHECK_STRENGTH.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == DAY_CHECK_CLIENT_HANDSHAKE.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == POST_CHECK_GUARD.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == POST_CHECK_REGISTER.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == POST_CHECK_SECURITY_RISK.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == POST_CHECK_GRIEVANCE.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == POST_CHECK_KIT_REQUEST.getNavigationViewType()) {
            RecyclerAdapterNavigationItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_navigation_item, parent, false);
            return new NavigationRecyclerViewHolder(binding);

        } else if (viewType == BARRACK_INSPECTION_STRENGTH.getNavigationViewType() || viewType == BARRACK_INSPECTION_SPACE.getNavigationViewType()
                || viewType == BARRACK_INSPECTION_OTHERS.getNavigationViewType() || viewType == BARRACK_INSPECTION_LANDLORD.getNavigationViewType()) {
            RowNavigationItemWithStatusBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.row_navigation_item_with_status, parent, false);
            return new NavigationRecyclerViewHolder(binding);
        } else {
            RecyclerAdapterEmptyItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_empty_item,
                    parent, false);
            return new EmptyViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NavigationUIModel model = itemList.get(position);
        ((NavigationRecyclerViewHolder) holder).viewHolderBind(model);

        /*if (holder.getItemViewType() == DAY_CHECK_POST.getNavigationViewType()) {
            final NavigationRecyclerViewHolder itemViewHolder = (NavigationRecyclerViewHolder) holder;
            itemViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == DAY_CHECK_STRENGTH.getNavigationViewType()) {
            final NavigationRecyclerViewHolder dividerViewHolder = (NavigationRecyclerViewHolder) holder;
            dividerViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == DAY_CHECK_CLIENT_HANDSHAKE.getNavigationViewType()) {
            final NavigationRecyclerViewHolder navigationViewHolder = (NavigationRecyclerViewHolder) holder;
            navigationViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == POST_CHECK_GUARD.getNavigationViewType()) {
            final NavigationRecyclerViewHolder navigationViewHolder = (NavigationRecyclerViewHolder) holder;
            navigationViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == POST_CHECK_REGISTER.getNavigationViewType()) {
            final NavigationRecyclerViewHolder navigationViewHolder = (NavigationRecyclerViewHolder) holder;
            navigationViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == POST_CHECK_SECURITY_RISK.getNavigationViewType()) {
            final NavigationRecyclerViewHolder navigationViewHolder = (NavigationRecyclerViewHolder) holder;
            navigationViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == POST_CHECK_GRIEVANCE.getNavigationViewType()) {
            final NavigationRecyclerViewHolder navigationViewHolder = (NavigationRecyclerViewHolder) holder;
            navigationViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == POST_CHECK_KIT_REQUEST.getNavigationViewType()) {
            final NavigationRecyclerViewHolder navigationViewHolder = (NavigationRecyclerViewHolder) holder;
            navigationViewHolder.viewHolderBind(model);
        } else if (holder.getItemViewType() == BARRACK_INSPECTION_STRENGTH.getNavigationViewType() ||
                holder.getItemViewType() == BARRACK_INSPECTION_SPACE.getNavigationViewType() ||
                holder.getItemViewType() == BARRACK_INSPECTION_OTHERS.getNavigationViewType() ||
                holder.getItemViewType() == BARRACK_INSPECTION_LANDLORD.getNavigationViewType()) {
            final NavigationRecyclerViewHolder navigationViewHolder = (NavigationRecyclerViewHolder) holder;
            navigationViewHolder.viewHolderBind(model);
        }*/
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (itemList.get(position).getViewType()) {
            case DAY_CHECK_POST:
            case DAY_CHECK_STRENGTH:
            case DAY_CHECK_CLIENT_HANDSHAKE:
                return DAY_CHECK_POST.getNavigationViewType();
            case POST_CHECK_GUARD:
            case POST_CHECK_REGISTER:
            case POST_CHECK_SECURITY_RISK:
            case POST_CHECK_GRIEVANCE:
            case POST_CHECK_KIT_REQUEST:
                return POST_CHECK_GUARD.getNavigationViewType();
            case BARRACK_INSPECTION_STRENGTH:
            case BARRACK_INSPECTION_SPACE:
            case BARRACK_INSPECTION_OTHERS:
            case BARRACK_INSPECTION_LANDLORD:
                return BARRACK_INSPECTION_STRENGTH.getNavigationViewType();
        }
        /*if (itemList.get(position).getViewType() == DAY_CHECK_POST) {
            return DAY_CHECK_POST.getNavigationViewType();
        } else if (itemList.get(position).getViewType() == DAY_CHECK_STRENGTH) {
            return DAY_CHECK_STRENGTH.getNavigationViewType();
        } else if (itemList.get(position).getViewType() == DAY_CHECK_CLIENT_HANDSHAKE) {
            return DAY_CHECK_CLIENT_HANDSHAKE.getNavigationViewType();
        } else if (itemList.get(position).getViewType() == POST_CHECK_GUARD) {
            return DAY_CHECK_STRENGTH.getNavigationViewType();
        } else if (itemList.get(position).getViewType() == POST_CHECK_REGISTER) {
            return DAY_CHECK_STRENGTH.getNavigationViewType();
        } else if (itemList.get(position).getViewType() == POST_CHECK_SECURITY_RISK) {
            return DAY_CHECK_STRENGTH.getNavigationViewType();
        } else if (itemList.get(position).getViewType() == POST_CHECK_GRIEVANCE) {
            return DAY_CHECK_STRENGTH.getNavigationViewType();
        } else if (itemList.get(position).getViewType() == POST_CHECK_KIT_REQUEST) {
            return DAY_CHECK_STRENGTH.getNavigationViewType();
        } else {
            return UNKNOWN.getNavigationViewType();
        }*/
        return UNKNOWN.getNavigationViewType();
    }

    public void updateAdapter(ArrayList<NavigationUIModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void setViewListener(NavigationViewListeners viewListener) {
        this.viewListener = viewListener;
    }

    class NavigationRecyclerViewHolder extends RecyclerView.ViewHolder {

        final ViewDataBinding binding;

        public NavigationRecyclerViewHolder(@NonNull RecyclerAdapterNavigationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public NavigationRecyclerViewHolder(@NonNull RowNavigationItemWithStatusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void viewHolderBind(NavigationUIModel model) {
            if (binding instanceof RecyclerAdapterNavigationItemBinding)
                ((RecyclerAdapterNavigationItemBinding) binding).setAdapterItem(model);
            else if (binding instanceof RowNavigationItemWithStatusBinding)
                ((RowNavigationItemWithStatusBinding) binding).setAdapterItem(model);
            binding.getRoot().setOnClickListener(v -> {
                if (viewListener != null)
                    viewListener.onNavigationItemClick(model);
            });
        }
    }
}
