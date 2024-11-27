package com.sisindia.ai.android.features.conveyance;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.utils.TimeUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import timber.log.Timber;

public class ConveyanceBindings {

    @BindingAdapter(value = {"setConveyanceDate"})
    public static void setConveyanceDate(AppCompatTextView tv, LocalDate date) {
        if (date != null) {
            tv.setText(date.format(DateTimeFormatter.ofPattern(TimeUtils.CREATE_TASK_DATE)));
        }
    }


    @BindingAdapter(value = {"setConveyanceAdapter"})
    public static void setConveyanceAdapter(RecyclerView recyclerView, ConveyanceAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setConveyanceDateTime"})
    public static void setConveyanceDateTime(AppCompatTextView tv, String datetime) {
        if (!TextUtils.isEmpty(datetime)) {
            try {
                tv.setText(LocalDateTime.parse(datetime).format(DateTimeFormatter.ofPattern("HH mm a")));
            } catch (Exception e) {
                Timber.e(e);
            }

        }
    }
}
