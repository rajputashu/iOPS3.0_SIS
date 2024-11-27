package com.sisindia.ai.android.features.addtask;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.uimodels.TaskTypeModel;
import com.sisindia.ai.android.utils.TimeUtils;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class AddTaskViewBinding {

    @BindingAdapter(value = {"selectTaskRecyclerAdapter", "addTaskViewListeners"})
    public static void bindAddTaskRecycler(RecyclerView recyclerView, SelectTaskRecyclerAdapter recyclerAdapter, SelectTaskTypeViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grey_divider_1sdp);

        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        recyclerView.setAdapter(recyclerAdapter);


    }

    @BindingAdapter(value = {"setSiteListAdapter", "setViewListeners"})
    public static void bindSiteListAdapter(RecyclerView recyclerView, SiteListRecyclerAdapter recyclerAdapter, AddTaskViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grey_divider_1sdp);

        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setReasonListAdapter", "setViewListeners"})
    public static void bindReasonListAdapter(RecyclerView recyclerView, ReasonListRecyclerAdapter recyclerAdapter, AddTaskViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grey_divider_1sdp);

        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setSubTaskTypeListAdapter", "setViewListeners"})
    public static void setSubTaskTypeListAdapter(RecyclerView recyclerView, SubTaskTypeListRecyclerAdapter recyclerAdapter, AddTaskViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grey_divider_1sdp);

        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setBarrackAdapter", "setBarrackListListener"})
    public static void setBarrackAdapter(RecyclerView recyclerView, BarrackListRecyclerAdapter recyclerAdapter, SelectBarrackViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grey_divider_1sdp);

        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setAddTaskTitle"})
    public static void setAddTaskTitle(AppCompatTextView tv, TaskTypeModel type) {
        if (type != null) {
            tv.setText(type.name);
        } else {
            tv.setText(R.string.string_add_task);
        }
    }

    @BindingAdapter(value = {"bindSiteName"})
    public static void setSiteName(AppCompatTextView textView, String name) {
        textView.setText(TextUtils.isEmpty(name) ? textView.getContext().getString(R.string.string_select_unit_name) : name);
    }

    @BindingAdapter(value = {"bindBarrackName"})
    public static void setBarrackName(AppCompatTextView textView, String name) {
        textView.setText(TextUtils.isEmpty(name) ? textView.getContext().getString(R.string.string_select_barrack_name) : name);
    }

    @BindingAdapter(value = {"bindReasonName"})
    public static void setReasonName(AppCompatTextView textView, String name) {
        textView.setText(TextUtils.isEmpty(name) ? textView.getContext().getString(R.string.string_select_reason) : name);
    }

    @BindingAdapter(value = {"setCreateTaskDate"})
    public static void setCreateTaskDate(AppCompatTextView tv, LocalDateTime dateTime) {
        if (dateTime != null) {
            tv.setText(dateTime.format(DateTimeFormatter.ofPattern(TimeUtils.CREATE_TASK_DATE)));
        }
    }

    //app:setCreateTaskTime="@{vm.taskStartDateTime}"
    @BindingAdapter(value = {"setCreateTaskTime"})
    public static void setCreateTaskTime(AppCompatTextView tv, LocalDateTime dateTime) {
        if (dateTime != null) {
            tv.setText(dateTime.format(DateTimeFormatter.ofPattern(TimeUtils.CREATE_TASK_TIME)));
        }
    }
}
