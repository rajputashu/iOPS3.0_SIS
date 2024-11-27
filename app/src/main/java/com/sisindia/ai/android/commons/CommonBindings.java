package com.sisindia.ai.android.commons;

import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.models.AudioPlayState;
import com.sisindia.ai.android.models.AudioRecordState;
import com.sisindia.ai.android.uimodels.BreadCumItemModel;
import com.sisindia.ai.android.uimodels.NavigationUIModel;
import com.sisindia.ai.android.uimodels.StatusUIModel;
import com.sisindia.ai.android.utils.TimeUtils;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class CommonBindings {

    @BindingAdapter(value = {"navigationRecyclerAdapter", "navigationViewListeners"})
    public static void bindNavigationRecyclerAdapter(RecyclerView recyclerView, NavigationUiRecyclerAdapter recyclerAdapter, NavigationViewListeners viewListeners) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListener(viewListeners);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @BindingAdapter(value = {"setNavigationImageResource"})
    public static void setImageResource(AppCompatImageView imageView, NavigationUIModel model) {
        imageView.setImageResource(model.getIconResId());
    }

    @BindingAdapter(value = {"setNavImgResWithStatus"})
    public static void setImageResourceWithStatus(AppCompatImageView imageView, NavigationUIModel model) {
        if (model.isCompleted())
            imageView.setImageResource(R.drawable.ic_status_completed_big);
        else
            imageView.setImageResource(model.getIconResId());
    }

    @BindingAdapter(value = {"setImageResource"})
    public static void setNavigationImageResource(AppCompatImageView imageView, StatusUIModel model) {
        imageView.setImageResource(model.isCompleted() ? model.getCompletedResId() : model.getPendingResId());
    }

    @BindingAdapter(value = {"bindTextColor"})
    public static void bindTextColor(AppCompatTextView textView, StatusUIModel model) {
        textView.setTextColor(ContextCompat.getColor(textView.getContext(), model.isCompleted() ? R.color.colorStatusDone : R.color.colorStatusPending));
    }

    @BindingAdapter(value = {"setRecordMediaAudioRecord", "setRecordMediaAudioPlay"})
    public static void setRecordButtonState(FloatingActionButton fab, AudioRecordState recordState, AudioPlayState playState) {
        switch (recordState) {

            case IDLE:
                fab.setImageResource(R.drawable.ic_mic);
                break;
            case RECORDING:
                fab.setImageResource(R.drawable.ic_stop_media);
                break;
            case RECORDED:
                fab.setImageResource(R.drawable.ic_replay);
                break;
        }
    }

    @BindingAdapter(value = {"setRecordTextState"})
    public static void setRecordTextState(AppCompatTextView textView, AudioRecordState state) {
        switch (state) {
            case IDLE:
                textView.setText(R.string.string_tap_to_record_audio);
                break;
            case RECORDING:
                textView.setText(R.string.string_tap_to_stop_audio);
                break;
            case RECORDED:
                textView.setText(R.string.string_tap_to_record_audio_again);
                break;
        }
    }


    @BindingAdapter(value = {"setPlayMediaAudioRecord", "setPlayMediaAudioPlay"})
    public static void setPlayButtonState(FloatingActionButton fab, AudioRecordState recordState, AudioPlayState playState) {

        int greyColor = ContextCompat.getColor(fab.getContext(), R.color.colorGreyDark);
        ColorStateList redColorStateList = ContextCompat.getColorStateList(fab.getContext(), R.color.colorLightRed);
        int whiteColor = ContextCompat.getColor(fab.getContext(), R.color.colorWhite);
        ColorStateList whiteColorStateList = ContextCompat.getColorStateList(fab.getContext(), R.color.colorRed_30opct);
        switch (recordState) {

            case IDLE:
            case RECORDING:
                fab.setEnabled(false);
                fab.setColorFilter(greyColor, android.graphics.PorterDuff.Mode.SRC_IN);
                fab.setBackgroundTintList(whiteColorStateList);
                break;

            case RECORDED:
                fab.setEnabled(true);
                fab.setColorFilter(whiteColor, android.graphics.PorterDuff.Mode.SRC_IN);
                fab.setBackgroundTintList(redColorStateList);
                break;
        }

        switch (playState) {

            case PLAY:
                fab.setImageResource(R.drawable.ic_media_playing);
                break;
            case PAUSE:
                fab.setImageResource(R.drawable.ic_play_media);
                break;
        }
    }


    @BindingAdapter("bindIntText")
    public static void bindIntTextView(AppCompatTextView textView, int value) {
        textView.setText(String.valueOf(value));
    }

    @BindingAdapter({"setBreadCum"})
    public static void setBreadCum(LinearLayout ll, List<BreadCumItemModel> items) {
        if (items != null) {
            ll.removeAllViews();
            for (int i = 0; i < items.size(); i++) {
                BreadCumItemModel item = items.get(i);

                LinearLayout child = (LinearLayout) LayoutInflater.from(ll.getContext()).inflate(R.layout.layout_item_bread_cumb, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                child.findViewById(R.id.breadCumLeft).setVisibility(i == 0 ? View.GONE : View.VISIBLE);
                child.findViewById(R.id.breadCumRight).setVisibility(i == items.size() - 1 ? View.GONE : View.VISIBLE);

                if (item.topTitleResId == R.string.register_item) {
                    ((AppCompatTextView) child.findViewById(R.id.tvBreadCumTitle)).setText(item.topTitle);
                } else {
                    ((AppCompatTextView) child.findViewById(R.id.tvBreadCumTitle)).setText(item.topTitleResId);
                }


                if (item.isDisable) {
                    ((AppCompatImageView) child.findViewById(R.id.ivBreadCumIcon)).setImageResource(R.drawable.ic_status_disable);
                    ((AppCompatTextView) child.findViewById(R.id.tvBreadCumValue)).setText(item.bottomTitle);
                    ((AppCompatTextView) child.findViewById(R.id.tvBreadCumValue))
                            .setTextColor(ContextCompat.getColor(ll.getContext(), R.color.colorLightGrey_2));
                } else {
                    ((AppCompatImageView) child.findViewById(R.id.ivBreadCumIcon)).setImageResource(item.isPending ? R.drawable.ic_status_pending : R.drawable.ic_status_completed);
                    ((AppCompatTextView) child.findViewById(R.id.tvBreadCumValue)).setText(item.bottomTitle);
                    ((AppCompatTextView) child.findViewById(R.id.tvBreadCumValue))
                            .setTextColor(ContextCompat.getColor(ll.getContext(), item.isPending ? R.color.colorLightGrey_2 : R.color.colorStatusDone));
                }

                child.setLayoutParams(params);
                ll.addView(child);
            }
        }
    }

    @BindingAdapter(value = {"setMenuNavigationAdapter", "setMenuNavigationListeners"})
    public static void setMenuNavigationAdapter(RecyclerView rv, MenuNavigationRecyclerAdapter adapter, MenuNavigationListeners listeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext(), RecyclerView.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        adapter.setListeners(listeners);
        rv.setAdapter(adapter);
    }


    @BindingAdapter(value = {"setTargetDateTime"})
    public static void setTargetDateTime(TextInputEditText et, LocalDateTime targetDateTime) {
        if (targetDateTime != null) {
            et.setText(targetDateTime.format(DateTimeFormatter.ofPattern(TimeUtils.TARGET_DATE)));
        }
    }

    @BindingAdapter("setTargetDate")
    public static void setTargetDate(AppCompatTextView tv, String targetDate) {
        if (!TextUtils.isEmpty(targetDate)) {
            tv.setText(LocalDateTime.parse(targetDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .format(DateTimeFormatter.ofPattern(TimeUtils.TARGET_DATE)));
        }
    }
}
