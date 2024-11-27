package com.sisindia.ai.android.features.taskcheck;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.room.entities.CheckedPostEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;

import java.util.List;


public class DayCheckViewBindings {


    @BindingAdapter(value = {"setPostCheckRecyclerAdapter", "setViewListeners"})
    public static void setSitePostAdapter(RecyclerView recyclerView, PostCheckRecyclerAdapter recyclerAdapter, DayCheckViewListeners checkViewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(checkViewListeners);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter("setPostStatusIcon")
    public static void setPostStatusIcon(AppCompatImageView imageView, int checkedPostStatus) {
        imageView.setImageResource((checkedPostStatus == CheckedPostEntity.CheckedPostStatus.PENDING.getPostStatus()) ? R.drawable.ic_status_pending : R.drawable.ic_group);
    }

    @BindingAdapter(value = {"setSiteCheckListAdapter", "setViewListeners"})
    public static void setSiteCheckListAdapter(RecyclerView recyclerView, SiteCheckListRecyclerAdapter recyclerAdapter, DayCheckViewListeners checkViewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(checkViewListeners);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter("setSiteCheckListRadioButtons")
    public static void setSiteCheckListRadioButtons(RadioGroup group, List<SiteCheckListOptionEntity> options) {
        if (options != null && options.size() != 0) {
            group.setVisibility(View.VISIBLE);
            for (SiteCheckListOptionEntity option : options) {
                AppCompatRadioButton rb = (AppCompatRadioButton) LayoutInflater.from(group.getContext()).inflate(R.layout.layout_radio_button, group, false);
                rb.setText(option.optionLabel);
                rb.setTag(option);
                group.addView(rb);
            }
        } else {
            group.setVisibility(View.GONE);
        }
    }


    @BindingAdapter("setCheckListStatusIcon")
    public static void setCheckListStatusIcon(AppCompatImageView iv, boolean isEdited) {
        iv.setImageResource(isEdited ? R.drawable.ic_group : R.drawable.ic_status_pending);

    }

    @BindingAdapter("setStrengthCheckStatusIcon")
    public static void setStrengthCheckStatusIcon(AppCompatImageView iv, int pendingStrengthCheck) {
        iv.setImageResource(pendingStrengthCheck == 0 ? R.drawable.ic_group : R.drawable.ic_status_pending);
    }

    @BindingAdapter("setClientCheckStatusIcon")
    public static void setClientCheckStatusIcon(AppCompatImageView iv, int status) {
        iv.setImageResource(status == 2 ? R.drawable.ic_group : R.drawable.ic_status_pending);
    }

    @BindingAdapter("setClientFeedbackStar")
    public static void setClientFeedbackStar(AppCompatRatingBar rb, String feedbackStar) {
        if (!TextUtils.isEmpty(feedbackStar)) {
            rb.setRating(Float.parseFloat(feedbackStar));
        }
    }
}
