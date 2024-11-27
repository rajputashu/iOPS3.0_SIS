package com.sisindia.ai.android.features.securityrisk;

import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.uimodels.AddSecurityCategory;

import java.util.List;

public class AddSecurityViewBindings {

    //setCategoryChips
    @BindingAdapter("setSecurityCategory")
    public static void setSecurityCategory(ChipGroup chipGroup, List<AddSecurityCategory> categories) {
        if (categories != null && categories.size() != 0) {
            chipGroup.setVisibility(View.VISIBLE);
            for (AddSecurityCategory category : categories) {
                Chip chip = (Chip) LayoutInflater.from(chipGroup.getContext()).inflate(R.layout.layout_chip_item, null, false);
                chip.setText(category.displayName);
                chip.setTag(category);
                chipGroup.addView(chip);
            }
        } else {
            chipGroup.setVisibility(View.GONE);
        }
    }
}
