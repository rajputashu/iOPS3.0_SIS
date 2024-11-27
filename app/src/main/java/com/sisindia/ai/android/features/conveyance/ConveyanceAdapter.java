package com.sisindia.ai.android.features.conveyance;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.base.recycler.BaseRecyclerAdapter;
import com.droidcommons.base.recycler.BaseViewHolder;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.RecyclerAdapterAoTimelineBinding;
import com.sisindia.ai.android.models.ConveyanceResponse;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class ConveyanceAdapter extends BaseRecyclerAdapter<ConveyanceResponse.AoTimeLine> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAdapterAoTimelineBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recycler_adapter_ao_timeline, parent, false);
        return new ConveyanceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ConveyanceResponse.AoTimeLine item = getItem(position);
        final ConveyanceViewHolder viewHolder = (ConveyanceViewHolder) holder;
        viewHolder.onBind(item);

    }

    static class ConveyanceViewHolder extends BaseViewHolder<ConveyanceResponse.AoTimeLine> {

        RecyclerAdapterAoTimelineBinding itemBinding;

        public ConveyanceViewHolder(@NonNull RecyclerAdapterAoTimelineBinding itemBinding) {
            super(itemBinding);
            this.itemBinding = itemBinding;
        }

        @Override
        public void onBind(ConveyanceResponse.AoTimeLine item) {
            itemBinding.setItem(item);

            itemBinding.tvTaskName.setText(item.taskName);

            if (item.taskId == -1) {
                itemBinding.tvSiteName.setVisibility(View.GONE);
                itemBinding.ivDivider.setVisibility(View.VISIBLE);
                itemBinding.tvDistance.setText(item.distance);
                itemBinding.ivStatusStart.setImageResource(R.drawable.ic_circle_green);
//                itemBinding.tvTaskStartEndDateTime.setText(LocalDateTime.parse(item.timelineDate).format(DateTimeFormatter.ofPattern("HH mm a")));
                itemBinding.tvTaskStartEndDateTime.setText(LocalDateTime.parse(item.actualStartDateTime).format(DateTimeFormatter.ofPattern("HH mm a")));
            } else if (item.taskId == -2) {
                itemBinding.tvSiteName.setVisibility(View.GONE);
                itemBinding.ivDivider.setVisibility(View.GONE);
                itemBinding.ivStatusStart.setImageResource(R.drawable.ic_circle_red);
                itemBinding.tvDistance.setText(item.distance);
                itemBinding.tvTaskStartEndDateTime.setText(LocalDateTime.parse(item.actualStartDateTime).format(DateTimeFormatter.ofPattern("HH mm a")));
            } else {
                itemBinding.tvSiteName.setVisibility(View.VISIBLE);
                itemBinding.tvSiteName.setText(item.siteName);
                itemBinding.ivStatusStart.setImageResource(R.drawable.ic_group);
                itemBinding.ivDivider.setVisibility(View.VISIBLE);
                itemBinding.tvDistance.setText(item.distance);
                itemBinding.tvTaskStartEndDateTime.setText(TextUtils.concat(
                        LocalDateTime.parse(item.actualStartDateTime).format(DateTimeFormatter.ofPattern("HH mm a")),
                        " - ",
                        LocalDateTime.parse(item.actualEndDateTime).format(DateTimeFormatter.ofPattern("HH mm a"))));
            }
        }
    }
}
