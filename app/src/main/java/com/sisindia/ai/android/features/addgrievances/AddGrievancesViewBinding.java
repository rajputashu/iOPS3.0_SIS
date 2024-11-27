package com.sisindia.ai.android.features.addgrievances;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.models.AudioPlayState;
import com.sisindia.ai.android.uimodels.GrievanceCategory;

import java.util.List;


public class AddGrievancesViewBinding {


    /*@BindingAdapter(value = {"allGuards", "setGuardSelectedListener"})
    public static void setAllGuardsAutoComplete(AppCompatAutoCompleteTextView textView, List<GuardSuggestionItem> list, AddGrievanceViewListeners viewListeners) {
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

    @BindingAdapter("setCategoryChips")
    public static void setCategoryChips(ChipGroup chipGroup, List<GrievanceCategory> categories) {
        if (categories != null && categories.size() != 0) {
            chipGroup.setVisibility(View.VISIBLE);
            for (GrievanceCategory category : categories) {
                Chip chip = (Chip) LayoutInflater.from(chipGroup.getContext()).inflate(R.layout.layout_chip_item, null, false);
                chip.setText(category.displayName);
                chip.setTag(category);
                chipGroup.addView(chip);
            }
        } else {
            chipGroup.setVisibility(View.GONE);
        }
    }

    @BindingAdapter(value = {"setRecordedAudioPlayState"})
    public static void setPlayButtonState(FloatingActionButton fab, AudioPlayState playState) {

        ColorStateList redColorStateList = ContextCompat.getColorStateList(fab.getContext(), R.color.colorLightRed);
        fab.setBackgroundTintList(redColorStateList);
        switch (playState) {

            case PLAY:
                fab.setImageResource(R.drawable.ic_media_playing);
                break;
            case PAUSE:
                fab.setImageResource(R.drawable.ic_play_media);
                break;
        }
    }
}
