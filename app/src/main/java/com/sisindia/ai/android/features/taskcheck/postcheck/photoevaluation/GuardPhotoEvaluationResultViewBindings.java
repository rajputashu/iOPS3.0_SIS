package com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation;

import android.net.Uri;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;

public class GuardPhotoEvaluationResultViewBindings {

    @BindingAdapter(value = {"srcImageFromUri"})
    public static void setImageResourceFromUri(AppCompatImageView imageView, Uri uri) {
        if (uri != null) {
            Glide.with(imageView).load(uri).into(imageView);
        }
    }

    @BindingAdapter(value = {"turnOutAdapter"})
    public static void setTurnOutAdapter(RecyclerView recyclerView, GuardTurnOutViewRecyclerAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setGuardTurnOutEditRecycler"})
    public static void setTurnOutAdapter(RecyclerView recyclerView, GuardTurnOutEditRecyclerAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter("setTurnOutTint")
    public static void setTurnOutTint(AppCompatImageView imageView, GuardTurnOutResult.GuardTurnoutModel model) {
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(),
                model.isSelected ? R.color.colorStatusDone : R.color.colorLightRed),
                android.graphics.PorterDuff.Mode.SRC_IN);
    }
}
