package com.sisindia.ai.android.features.predashboard;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.utils.TimeUtils;

import org.threeten.bp.LocalDate;

public class PreDashBoardViewBindings {

    @BindingAdapter(value = {"resultsRecyclerAdapter"})
    public static void bindResultRecyclerViewAdapater(RecyclerView recyclerView, ResultsRecyclerAdapter recyclerAdapter) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @BindingAdapter(value = {"effortsRecyclerAdapter"})
    public static void bindEffortsRecyclerViewAdapater(RecyclerView recyclerView, EffortsRecyclerAdapter recyclerAdapter) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @BindingAdapter(value = {"setRotaSelectedDate"})
    public static void setRotaSelectedDate(AppCompatTextView textView, LocalDate date) {
        if (date != null) {
            textView.setText(TimeUtils.DASHBOARD_DATE_FORMAT(date));
        }
    }
}
