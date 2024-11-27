package com.sisindia.ai.android.features.rota;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.google.gson.Gson;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.MysisRotaDashboardRowBinding;
import com.sisindia.ai.android.databinding.MysisRotaHeaderBinding;
import com.sisindia.ai.android.databinding.RecyclerAdapterDashboardItemBinding;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.uimodels.RotaTaskItemModel;
import com.sisindia.ai.android.uimodels.tasks.MySiSTaskDescription;

public class DashBoardRotaTaskRecyclerAdapter extends BaseRecyclerAdapter<RotaTaskItemModel> {

    private RotaViewListeners rotaViewListeners;
    private static final int TYPE_SIS_ROTA = 1;
    private static final int TYPE_MY_SIS_ROTA = 2;
    private static final int TYPE_MY_SIS_HEADER = 3;
    private Gson gson = null;

    public void setRotaViewListeners(@NonNull RotaViewListeners rotaViewListeners) {
        this.rotaViewListeners = rotaViewListeners;
        gson = new Gson();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_SIS_ROTA) {
            RecyclerAdapterDashboardItemBinding binding = (RecyclerAdapterDashboardItemBinding) getViewDataBinding(R.layout.recycler_adapter_dashboard_item, parent);
            return new DashBoardRecyclerViewHolder(binding);
        } else if (viewType == TYPE_MY_SIS_HEADER) {
            MysisRotaHeaderBinding binding = (MysisRotaHeaderBinding) getViewDataBinding(R.layout.mysis_rota_header, parent);
            return new MySISHeaderViewHolder(binding);
        } else if (viewType == TYPE_MY_SIS_ROTA) {
            MysisRotaDashboardRowBinding binding = (MysisRotaDashboardRowBinding) getViewDataBinding(R.layout.mysis_rota_dashboard_row, parent);
            return new MySISRotaViewHolder(binding);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int taskTypeId = getItem(position).taskTypeId;
        if (taskTypeId == 14 || taskTypeId == 15 || taskTypeId == 16 || taskTypeId == 17 || taskTypeId == 18
                || taskTypeId == 19 || taskTypeId == 20 || taskTypeId == 21 || taskTypeId == 22)
            return TYPE_MY_SIS_ROTA;
        else if (taskTypeId == 51) // Will create this id explicitly for MySIS Header
            return TYPE_MY_SIS_HEADER;
        return TYPE_SIS_ROTA;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final RotaTaskItemModel item = getItem(position);
        if (holder instanceof DashBoardRecyclerViewHolder) {
            final DashBoardRecyclerViewHolder viewHolder = (DashBoardRecyclerViewHolder) holder;
            viewHolder.onBind(item);
        } else if (holder instanceof MySISRotaViewHolder) {
            final MySISRotaViewHolder viewHolder = (MySISRotaViewHolder) holder;
            viewHolder.onBind(item);
        } else if (holder instanceof MySISHeaderViewHolder) {
            final MySISHeaderViewHolder viewHolder = (MySISHeaderViewHolder) holder;
            viewHolder.onBind(item);
        }
    }

    class DashBoardRecyclerViewHolder extends BaseViewHolder<RotaTaskItemModel> {
        RecyclerAdapterDashboardItemBinding binding;

        public DashBoardRecyclerViewHolder(@NonNull RecyclerAdapterDashboardItemBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(RotaTaskItemModel item) {
            binding.setModel(item);
            binding.getRoot().setOnClickListener(v -> rotaViewListeners.onDashBoardTaskItemClick(item));

            TaskEntity.TaskStatus status = TaskEntity.TaskStatus.of(item.taskStatus);
            switch (status) {
                case NEW:
                    binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_pending_small, 0, 0, 0);
                    binding.tvTaskItemStatus.setText(R.string.pending_text);
                    break;
                case IN_PROGRESS:
                    binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_pending_small, 0, 0, 0);
                    binding.tvTaskItemStatus.setText(R.string.string_inprogress);
                    break;
                case INACTIVE:
                    break;
                case COMPLETED:
                    if (item.isSynced) {
                        binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_completed_small, 0, 0, 0);
                        binding.tvTaskItemStatus.setText(R.string.string_completed);
                    } else {
                        binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_not_synced, 0, 0, 0);
                        binding.tvTaskItemStatus.setText(R.string.string_completed);
                    }
                    break;
            }

            binding.tvTaskItemUnitName.setText(item.taskTypeId == 3 ? item.barrackName : item.siteName);
            binding.tvRotaSiteCode.setText(item.taskTypeId == 3 ? item.barrackCode : item.siteCode);//android:text="@{adapterItem.siteCode}"

            //Commenting temporary
            /*binding.tvRotaDistanceTime.setText(binding.getRoot().getContext()
                    .getString(R.string.dynamic_distance_time, item.estimatedDistance == null ? "0" : item.estimatedDistance,
                            item.estimatedTravelTime == null ? "0" : item.estimatedTravelTime));*/
        }
    }

    class MySISRotaViewHolder extends BaseViewHolder<RotaTaskItemModel> {
        MysisRotaDashboardRowBinding binding;

        public MySISRotaViewHolder(@NonNull MysisRotaDashboardRowBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(RotaTaskItemModel item) {
            try {
                if (gson != null && item.description != null) {
//                MySisTaskDescriptionList[] description = gson.fromJson(item.description, MySisTaskDescriptionList[].class);
                    MySiSTaskDescription[] description = gson.fromJson(item.description, MySiSTaskDescription[].class);
                    if (description.length > 0)
                        binding.setModel(description[0]);
                }

                binding.getRoot().setOnClickListener(v -> rotaViewListeners.onDashBoardTaskItemClick(item));

                TaskEntity.TaskStatus status = TaskEntity.TaskStatus.of(item.taskStatus);
                switch (status) {
                    case NEW:
                        binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_pending_small, 0, 0, 0);
                        binding.tvTaskItemStatus.setText(R.string.pending_text);
                        break;
                    case IN_PROGRESS:
                        binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_pending_small, 0, 0, 0);
                        binding.tvTaskItemStatus.setText(R.string.string_inprogress);
                        break;
                    case INACTIVE:
                        break;
                    case COMPLETED:
                        if (item.isSynced)
                            binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_completed_small, 0, 0, 0);
                        else
                            binding.tvTaskItemStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_not_synced, 0, 0, 0);
                        binding.tvTaskItemStatus.setText(R.string.string_completed);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class MySISHeaderViewHolder extends BaseViewHolder<RotaTaskItemModel> {
        MysisRotaHeaderBinding binding;

        public MySISHeaderViewHolder(@NonNull MysisRotaHeaderBinding itemBinding) {
            super(itemBinding);
            this.binding = itemBinding;
        }

        @Override
        public void onBind(RotaTaskItemModel item) {
            binding.setMySISTaskCount(item.description);
        }
    }
}