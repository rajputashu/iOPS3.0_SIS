package com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidcommons.views.ink.InkView;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;


public class PostGuardViewBindings {

    @BindingAdapter(value = {"addInkListeners"})
    public static void addInkViewListener(InkView inkView, InkView.InkListener inkListener) {
        inkView.addListener(inkListener);
    }

    @BindingAdapter(value = {"setGuardGrievancePendingRecyclerAdapter"})
    public static void setGuardGrievancePendingRecyclerAdapter(RecyclerView recyclerView, GuardGrievanceRecyclerAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter(value = {"guardSummaryRecyclerAdapeter1"})
    public static void bindGuardSummaryRecycler1(RecyclerView recyclerView, GuardGrievanceRecyclerAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter("setAddRewardFineGuardDeserve")
    public static void setAddRewardFineGuardDeserve(AppCompatTextView textView, String str) {
        if (!TextUtils.isEmpty(str)) {
            String text = str.concat(textView.getContext().getString(R.string.guard_deserve_reward));
            textView.setText(text);
        }
    }

    @BindingAdapter("setAddRewardFineGuardName")
    public static void setAddRewardFineGuardName(AppCompatTextView textView, String str) {
        if (!TextUtils.isEmpty(str)) {
            String text = str.concat(textView.getContext().getString(R.string.appos_score));
            textView.setText(text);
        }
    }

    @BindingAdapter({"setAddedRewardBackground", "setAddedRewardDone"})
    public static void setAddedRewardBackground(RelativeLayout layout, EmployeeFineRewardEntity.RewardType rewardType, Boolean isRewardAdded) {

        layout.setVisibility(isRewardAdded ? View.VISIBLE : View.GONE);
        switch (rewardType) {

            case UNKNOWN:
                break;
            case REWARD:
                layout.setBackgroundColor(ContextCompat.getColor(layout.getContext(), R.color.colorTransparentGreen));
                break;
            case FINE:
                layout.setBackgroundColor(ContextCompat.getColor(layout.getContext(), R.color.colorRed_30opct));
                break;
        }
    }

    @BindingAdapter({"setOnRewardFineAdded", "setRewardType"})
    public static void setOnRewardFineAdded(AppCompatTextView textView, String str, EmployeeFineRewardEntity.RewardType rewardType) {
        if (!TextUtils.isEmpty(str)) {
            String type = rewardType == EmployeeFineRewardEntity.RewardType.REWARD ?
                    textView.getContext().getString(R.string.added_reward) :
                    textView.getContext().getString(R.string.added_fine);
            String text = type.concat(str);
            int textColor = rewardType == EmployeeFineRewardEntity.RewardType.REWARD ? R.color.colorStatusDone : R.color.colorLightRed;
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), textColor));
            textView.setText(text);
        }
    }

    @BindingAdapter("setGuardSummaryRewardTypeIcon")
    public static void setGuardSummaryRewardTypeIcon(AppCompatImageView imageView, int reward) {
        EmployeeFineRewardEntity.RewardType rewardType = EmployeeFineRewardEntity.RewardType.of(reward);

        imageView.setImageResource(rewardType == EmployeeFineRewardEntity.RewardType.REWARD ?
                R.drawable.ic_guard_reward : R.drawable.ic_guard_fine);

    }


    @BindingAdapter("setGuardSummaryRewardTypeText")
    public static void setGuardSummaryRewardTypeText(AppCompatTextView textView, int reward) {
        EmployeeFineRewardEntity.RewardType rewardType = EmployeeFineRewardEntity.RewardType.of(reward);
        String str = rewardType == EmployeeFineRewardEntity.RewardType.REWARD ?
                textView.getContext().getString(R.string.reward_added) :
                textView.getContext().getString(R.string.fine_added);
        int textColor = rewardType == EmployeeFineRewardEntity.RewardType.REWARD ? R.color.colorStatusDone : R.color.colorLightRed;
        textView.setTextColor(ContextCompat.getColor(textView.getContext(), textColor));
        textView.setText(str);

    }

    @BindingAdapter("setRewardTypeText")
    public static void setRewardTypeText(AppCompatTextView textView, EmployeeFineRewardEntity.RewardType rewardType) {
        String text = rewardType == EmployeeFineRewardEntity.RewardType.REWARD ?
                textView.getContext().getString(R.string.string_add_reward_guard_text) :
                textView.getContext().getString(R.string.string_add_fine_guard_text);
        textView.setText(text);
    }

    @BindingAdapter("setPendingGrievanceCount")
    public static void setPendingGrievanceCount(AppCompatTextView textView, int count) {
        String text = TextUtils.concat(textView.getContext().getString(R.string.grievance_pending),
                textView.getContext().getString(R.string.symbol_open_bracket),
                String.valueOf(count),
                textView.getContext().getString(R.string.symbol_close_bracket)).toString();
        textView.setText(text);
    }


    @BindingAdapter("setAllGrievanceCount")
    public static void setAllGrievanceCount(AppCompatTextView textView, int count) {
        String text = TextUtils.concat(textView.getContext().getString(R.string.grievance_pending),
                textView.getContext().getString(R.string.symbol_open_bracket),
                String.valueOf(count),
                textView.getContext().getString(R.string.symbol_close_bracket)).toString();
        textView.setText(text);
    }

    @BindingAdapter(value = {"setAllGrievanceForGuardRecyclerAdapter"})
    public static void setAllGrievanceForGuardRecyclerAdapter(RecyclerView recyclerView, GuardGrievanceRecyclerAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setDeployedDate"})
    public static void setDeployedDate(AppCompatTextView tv, String datetime) {
        if (!TextUtils.isEmpty(datetime)) {
            long days = ChronoUnit.DAYS.between(LocalDateTime.parse(datetime, DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.now());
            tv.setText(String.valueOf(days).concat(" Days"));
        }
    }

}
