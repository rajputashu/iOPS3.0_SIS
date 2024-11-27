package com.sisindia.ai.android.features.addkitrequest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.uimodels.KitItemModel;
import com.sisindia.ai.android.uimodels.KitRequestGuardItem;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import static com.sisindia.ai.android.utils.TimeUtils.KIT_REQUEST_DATE_TIME;

public class AddKitRequestViewBindings {


//    @BindingAdapter(value = {"allGuards", "setAddKitRequestViewListener"})
//    public static void setAllGuardsAutoComplete(AppCompatAutoCompleteTextView textView, List<KitRequestGuardItem> list, AddKitRequestViewListeners viewListeners) {
//        if (!list.isEmpty()) {
//            KitRequestGuardSuggestionAdapter suggestionAdapter = new KitRequestGuardSuggestionAdapter(textView.getContext(), list);
//            textView.setAdapter(suggestionAdapter);
//            textView.setOnItemClickListener((parent, view, position, id) -> {
//                InputMethodManager in = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (in != null) {
//                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//                }
//                viewListeners.onGuardSelected(suggestionAdapter.getItem(position));
//            });
//        }
//    }

    @BindingAdapter(value = {"setKitItemAdapter", "setKitViewListener"})
    public static void setKitItemAdapter(RecyclerView recyclerView, AddKitItemRecyclerAdapter recyclerAdapter, AddKitRequestViewListeners viewListeners) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setViewListener(viewListeners);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable item = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grey_divider_1sdp);

        if (item != null) {
            dividerItemDecoration.setDrawable(item);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        recyclerView.setAdapter(recyclerAdapter);
    }

    @BindingAdapter(value = {"setKitRequestAdapter"})
    public static void setKitRequestAdapter(RecyclerView recyclerView, KitRequestRecyclerAdapter recyclerAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @BindingAdapter("setKitItemSizes")
    public static void setKitItemSizes(RadioGroup group, List<KitItemModel.KitItemSizeModel> sizes) {
        if (sizes != null && sizes.size() != 0) {
            group.setVisibility(View.VISIBLE);
            for (KitItemModel.KitItemSizeModel size : sizes) {
                AppCompatRadioButton rb = (AppCompatRadioButton) LayoutInflater.from(group.getContext()).inflate(R.layout.layout_radio_button, null, false);
                rb.setText(size.itemSizeName);
                rb.setTag(size);
                group.addView(rb);
            }
        } else {
            group.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("setKitRequestedDateTime")
    public static void setKitRequestedDateTime(AppCompatTextView textView, String dateTime) {
        if (!TextUtils.isEmpty(dateTime)) {
            textView.setText(LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern(KIT_REQUEST_DATE_TIME)));
        }

    }
}
