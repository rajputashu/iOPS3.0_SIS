package com.sisindia.ai.android.features.register;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.R;

public class RegisterCheckBindings {

    @BindingAdapter({"setRegisterAdapter", "setRegisterViewListener"})
    public static void setRegisterAdapter(RecyclerView recyclerView, PostRegisterRecyclerAdapter recyclerAdapter, RegisterCheckViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grey_divider);
        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
        recyclerAdapter.setViewListeners(viewListeners);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter({"setCapturedDocumentAdapter", "setViewListeners"})
    public static void setCapturedDocumentAdapter(RecyclerView rv, CapturedDocumentRecyclerAdapter recyclerAdapter, RegisterCheckViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext(), RecyclerView.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListeners(viewListeners);
        rv.setAdapter(recyclerAdapter);

    }
}
