package com.sisindia.ai.android.features.addtask;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterAddTaskBinding;
import com.sisindia.ai.android.uimodels.TaskTypeModel;

public class SelectTaskRecyclerAdapter extends BaseRecyclerAdapter<TaskTypeModel> {

    private SelectTaskTypeViewListeners viewListeners;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterAddTaskBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_add_task, parent, false);
        return new SelectTaskRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TaskTypeModel model = getItem(position);
        final SelectTaskRecyclerViewHolder viewHolder = (SelectTaskRecyclerViewHolder) holder;
        viewHolder.onBind(model);
    }

    public void setViewListeners(SelectTaskTypeViewListeners viewListeners) {
        this.viewListeners = viewListeners;
    }

    class SelectTaskRecyclerViewHolder extends BaseViewHolder<TaskTypeModel> {
        private RecyclerAdapterAddTaskBinding taskBinding;

        public SelectTaskRecyclerViewHolder(@NonNull RecyclerAdapterAddTaskBinding itemBinding) {
            super(itemBinding);
            this.taskBinding = itemBinding;
        }

        @Override
        public void onBind(TaskTypeModel item) {
            taskBinding.setItem(item);

            taskBinding.getRoot().setOnClickListener(v -> {
                if (viewListeners != null) {
                    viewListeners.onAddTaskItemClick(item);
                }
            });
        }
    }
}
