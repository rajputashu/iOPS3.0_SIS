package com.sisindia.ai.android.features.taskcheck.postcheck;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.CheckGuardViewListeners;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable.GuardNotAvailableRecyclerAdapter;
import com.sisindia.ai.android.features.taskcheck.postcheck.postlist.SitePostListAdapter;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;
import com.sisindia.ai.android.uimodels.RewardFineCategory;

import java.util.List;

import static com.sisindia.ai.android.room.entities.CheckedGuardEntity.CheckedGuardStatus.PENDING;

public class PostCheckViewBindings {

    @BindingAdapter(value = {"postsRecyclerAdapter", "postCheckViewListeners"})
    public static void bindNavigationRecyclerAdapter(RecyclerView recyclerView, SitePostListAdapter recyclerAdapter, PostCheckViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListener(viewListeners);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.white_divider);

        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter(value = {"setGuardCheckRecyclerAdapter", "setViewListeners"})
    public static void setGuardCheckRecyclerAdapter(RecyclerView recyclerView, GuardCheckRecyclerAdapter recyclerAdapter, PostCheckViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListener(viewListeners);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter(value = {"setPostCheckListAdapter", "setViewListeners"})
    public static void setPostCheckListAdapter(RecyclerView recyclerView, PostCheckListRecyclerAdapter recyclerAdapter, PostCheckViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setSecurityCheckRecyclerAdapter"})
    public static void setSecurityCheckRecyclerAdapter(RecyclerView recyclerView, SecurityCheckRecyclerAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setRegisterCheckRecyclerAdapter", "setViewListeners"})
    public static void setRegisterCheckRecyclerAdapter(RecyclerView recyclerView, RegisterCheckRecyclerAdapter recyclerAdapter, PostCheckViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter(value = {"setGuardNotAvailableAdapter", "setViewListeners"})
    public static void setGuardNotAvailableAdapter(RecyclerView recyclerView, GuardNotAvailableRecyclerAdapter recyclerAdapter, CheckGuardViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerAdapter.setViewListener(viewListeners);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    /*@BindingAdapter(value = {"allGuards", "setAllGuardViewListener"})
    public static void setAllGuardsAutoComplete(AppCompatAutoCompleteTextView textView, List<GuardSuggestionItem> list, CheckGuardViewListeners viewListeners) {
        if (!list.isEmpty()) {
            GuardsSuggestionAdapter suggestionAdapter = new GuardsSuggestionAdapter(textView.getContext(), list);
            textView.setAdapter(suggestionAdapter);
            textView.setOnItemClickListener((parent, view, position, id) -> {
                InputMethodManager in = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (in != null) {
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                }
                viewListeners.onGuardSelected(suggestionAdapter.getItem(position));
            });
        }
    }*/

    @BindingAdapter("setGuardStatusIcon")
    public static void setGuardStatusIcon(AppCompatImageView imageView, CheckedGuardItemModel item) {
        imageView.setImageResource((item.checkedGuardStatus == PENDING.getCheckedGuardStatus()) ? R.drawable.ic_status_pending : R.drawable.ic_group);
    }


    @BindingAdapter("setGuardStatus")
    public static void setGuardStatus(AppCompatTextView textView, CheckedGuardItemModel item) {
        if (item.guardStatus == CheckedGuardEntity.GuardStatus.SLEEPING.getGuardStatus()) {
            textView.setText(R.string.string_guard_sleeping);
        } else if (item.guardStatus == CheckedGuardEntity.GuardStatus.NOT_AVAILABLE.getGuardStatus()) {
            textView.setText(R.string.string_guard_not_available_text);
        } else if (item.guardStatus == CheckedGuardEntity.GuardStatus.AVAILABLE.getGuardStatus()) {
            textView.setText(R.string.string_guard_available_text);
        }
    }

    //app:setGuardTotalTurnOut="@{adapterItem.totalTurnOut}"
    //app:setGuardTotalTurnOutScore="@{adapterItem.turnOutScore}"

    @BindingAdapter({"setGuardTotalTurnOut", "setGuardTotalTurnOutScore"})
    public static void setGuardTurnOutEvaluation(AppCompatTextView tv, int totalTurnOut, int turnOutScore) {
        if (turnOutScore == 0) {
            tv.setText(String.valueOf(turnOutScore));
        } else {
            if (totalTurnOut == 0)
                totalTurnOut = 1;
            CharSequence finalText = TextUtils.concat(String.valueOf((turnOutScore * 100 / totalTurnOut)), "/", "100");
            SpannableString ss1 = new SpannableString(finalText);
            ss1.setSpan(new RelativeSizeSpan(1.5f), 0, String.valueOf((turnOutScore * 100 / totalTurnOut)).length(), 0); // set size
            ss1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(tv.getContext(), R.color.colorBlue)), 0, 1, 0);
            tv.setText(ss1);
        }
    }

    //setCategoryChips
    @BindingAdapter("setRewardFineCategory")
    public static void setRewardFineCategory(ChipGroup chipGroup, List<RewardFineCategory> categories) {
        if (categories != null && categories.size() != 0) {
            chipGroup.removeAllViews();
            chipGroup.setVisibility(View.VISIBLE);
            for (RewardFineCategory category : categories) {
                Chip chip = (Chip) LayoutInflater.from(chipGroup.getContext()).inflate(R.layout.layout_chip_item, null, false);
                chip.setText(TextUtils.concat(chipGroup.getContext().getString(R.string.rupee_symbol), category.displayName));
                chip.setTag(category);
                chipGroup.addView(chip);
            }
        } else {
            chipGroup.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("setPostCheckListRadioButtons")
    public static void setPostCheckListRadioButtons(RadioGroup group, List<PostCheckListOptionEntity> options) {
        if (options != null && options.size() != 0) {
            group.setVisibility(View.VISIBLE);
            for (PostCheckListOptionEntity option : options) {
                AppCompatRadioButton rb = (AppCompatRadioButton) LayoutInflater.from(group.getContext()).inflate(R.layout.layout_radio_button, group, false);
                rb.setText(option.optionLabel);
                rb.setTag(option);
                group.addView(rb);
            }
        } else {
            group.setVisibility(View.GONE);
        }
    }


    @BindingAdapter({"setTotalTurnOut", "setTurnOutScore"})
    public static void setTurnOutText(AppCompatTextView tv, int totalTurnOut, int turnOutScore) {
        if (turnOutScore == 0) {
            tv.setText(String.valueOf(turnOutScore));
        } else {
            tv.setText(String.valueOf(turnOutScore * 100 / totalTurnOut));
        }
    }

}
