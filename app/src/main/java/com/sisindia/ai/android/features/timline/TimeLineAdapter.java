package com.sisindia.ai.android.features.timline;

import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AdapterItemCompletedTaskBinding;
import com.sisindia.ai.android.databinding.AdapterItemTimeLineBinding;
import com.sisindia.ai.android.uimodels.TimeLineModel;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class TimeLineAdapter extends BaseRecyclerAdapter<TimeLineModel> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemTimeLineBinding binding = (AdapterItemTimeLineBinding) getViewDataBinding(R.layout.adapter_item_time_line, parent);
        return new TimeLineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TimeLineViewHolder viewHolder = (TimeLineViewHolder) holder;
        final TimeLineModel model = getItem(position);
        viewHolder.onBind(model);
    }


    static class TimeLineViewHolder extends BaseViewHolder<TimeLineModel> {

        AdapterItemTimeLineBinding itemBinding;

        TimeLineViewHolder(@NonNull AdapterItemTimeLineBinding itemBinding) {
            super(itemBinding);
            this.itemBinding = itemBinding;
        }

        @Override
        public void onBind(TimeLineModel item) {
            itemBinding.setAdapterItem(item);
            itemBinding.tvDutyOnDateTime.setText("Duty On Time: " + (TextUtils.isEmpty(item.dutyOnDateTime) ? "NA" : LocalDateTime.parse(item.dutyOnDateTime, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("hh:mm a  dd MMM` yyyy"))));
            itemBinding.tvDutyOffDateTime.setText("Duty Off Time: " + (TextUtils.isEmpty(item.dutyOffDateTime) ? "NA" : LocalDateTime.parse(item.dutyOffDateTime, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("hh:mm a  dd MMM` yyyy"))));
            LinearLayoutManager manager = new LinearLayoutManager(itemBinding.getRoot().getContext());
            itemBinding.rvCompletedTasks.setLayoutManager(manager);
            CompletedTasksAdapter adapter = new CompletedTasksAdapter();
            itemBinding.rvCompletedTasks.setAdapter(adapter);
            adapter.clearAndSetItems(item.tasks);
        }
    }

    static class CompletedTasksAdapter extends BaseRecyclerAdapter<TimeLineModel.CompletedTask> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AdapterItemCompletedTaskBinding binding = (AdapterItemCompletedTaskBinding) getViewDataBinding(R.layout.adapter_item_completed_task, parent);
            return new CompletedTaskViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final CompletedTaskViewHolder viewHolder = (CompletedTaskViewHolder) holder;
            final TimeLineModel.CompletedTask item = getItem(position);
            viewHolder.onBind(item);
        }


        static class CompletedTaskViewHolder extends BaseViewHolder<TimeLineModel.CompletedTask> {
            AdapterItemCompletedTaskBinding binding;

            public CompletedTaskViewHolder(@NonNull AdapterItemCompletedTaskBinding itemBinding) {
                super(itemBinding);
                binding = itemBinding;
            }

            @Override
            public void onBind(TimeLineModel.CompletedTask item) {
                binding.setAdapterItem(item);

                binding.taskTime.setText(LocalDateTime.parse(item.taskStartDateTime, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("hh:mm a")) + " - " +
                        LocalDateTime.parse(item.taskEndDateTime, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("hh:mm a")));
            }
        }
    }
}
