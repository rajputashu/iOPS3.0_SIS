package com.sisindia.ai.android.features.rota;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.uimodels.RotaTaskItemModel;
import com.sisindia.ai.android.utils.TimeUtils;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class RotaViewBindings {

    @BindingAdapter(value = {"weeklyRotaComplianceRecyclerAdapter", "rotaViewListeners"})
    public static void bindWeeklyComplianceViewPagerAdapter(ViewPager2 viewPager, WeeklyRotaComplianceRecyclerAdapter recyclerAdapter, RotaViewListeners rotaViewListeners) {
        recyclerAdapter.setRotaViewListeners(rotaViewListeners);
        viewPager.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"dashBoardAdapter", "rotaViewListeners"})
    public static void bindDashBoardAdapter(RecyclerView recyclerView, DashBoardRotaTaskRecyclerAdapter recyclerAdapter, RotaViewListeners rotaViewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setRotaViewListeners(rotaViewListeners);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter("setTaskTitle")
    public static void setChipTaskTitle(Chip chip, RotaTaskItemModel item) {
        if (item != null && !TextUtils.isEmpty(item.estimatedTaskExecutionStartDateTime) && !TextUtils.isEmpty(item.estimatedTaskExecutionEndDateTime)) {
            LocalDateTime taskStartTime = LocalDateTime.parse(item.estimatedTaskExecutionStartDateTime, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime taskEndTime = LocalDateTime.parse(item.estimatedTaskExecutionEndDateTime, DateTimeFormatter.ISO_DATE_TIME);

            chip.setText(TextUtils.concat(item.name,
                    " : ", TimeUtils.TASK_TIME_FORMAT(taskStartTime.toLocalTime()),
                    " - ", TimeUtils.TASK_TIME_FORMAT(taskEndTime.toLocalTime())));
        }
    }

    @BindingAdapter(value = {"taskTime", "minGuardCount"})
    public static void setMinuteAndGuardCount(AppCompatTextView textView, String estimatedTime, int guardsCount) {
        if (guardsCount > 10) {
            textView.setText(textView.getContext().getString(R.string.string_mins_guards, estimatedTime, String.valueOf(guardsCount)));
        } else if (guardsCount > 0 && guardsCount < 10) {
            textView.setText(textView.getContext().getString(R.string.string_mins_guard, estimatedTime, String.valueOf(guardsCount)));
        } else {
            textView.setText(textView.getContext().getString(R.string.string_mins_only, estimatedTime));
        }
    }
}
