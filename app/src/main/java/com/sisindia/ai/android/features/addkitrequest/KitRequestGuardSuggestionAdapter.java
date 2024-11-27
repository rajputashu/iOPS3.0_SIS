package com.sisindia.ai.android.features.addkitrequest;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.databinding.AcAdapterKitRequestGuardSuggestionBinding;
import com.sisindia.ai.android.uimodels.KitRequestGuardItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KitRequestGuardSuggestionAdapter extends ArrayAdapter<KitRequestGuardItem> implements Filterable {

    private List<KitRequestGuardItem> items;
    private ArrayList<KitRequestGuardItem> suggestions = new ArrayList<>();
    private Context context;

    public KitRequestGuardSuggestionAdapter(Context context, List<KitRequestGuardItem> items) {
        super(context, R.layout.ac_adapter_kit_request_guard_suggestion);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public KitRequestGuardItem getItem(int i) {
        return suggestions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NotNull
    @Override
    public View getView(int i, View view, @NotNull ViewGroup viewGroup) {


        LayoutInflater inflater = LayoutInflater.from(context);

        KitRequestGuardSuggestionViewHolder holder;

        KitRequestGuardItem item = getItem(i);

        if (view == null) {
            AcAdapterKitRequestGuardSuggestionBinding itemCustomerPlantBinding =
                    DataBindingUtil.inflate(inflater, R.layout.ac_adapter_kit_request_guard_suggestion, viewGroup, false);
            holder = new KitRequestGuardSuggestionViewHolder(itemCustomerPlantBinding);

            holder.view.setTag(holder);

        } else {
            holder = (KitRequestGuardSuggestionViewHolder) view.getTag();
        }

        holder.binding.setAdapterItem(item);

        return holder.view;
    }

    @NotNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                suggestions.clear();


                for (KitRequestGuardItem item : items) {
                    if (TextUtils.isEmpty(constraint) || item.employeeName.toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                        suggestions.add(item);
                    } else {
                        suggestions.remove(item);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;


            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                clear();
                if (filterResults != null && filterResults.count > 0) {
                    // we have filtered results
                    addAll((ArrayList<KitRequestGuardItem>) filterResults.values);
                } else {
                    // no filter, add entire original list back in
                    addAll(suggestions);
                }
                notifyDataSetChanged();
            }

            @Override
            public String convertResultToString(Object resultValue) {

                KitRequestGuardItem item = (KitRequestGuardItem) resultValue;

                return item.employeeName;
            }
        };
    }


    private static class KitRequestGuardSuggestionViewHolder {
        private AcAdapterKitRequestGuardSuggestionBinding binding;
        private View view;

        KitRequestGuardSuggestionViewHolder(AcAdapterKitRequestGuardSuggestionBinding binding) {
            this.binding = binding;
            this.view = binding.getRoot();
        }
    }

}
