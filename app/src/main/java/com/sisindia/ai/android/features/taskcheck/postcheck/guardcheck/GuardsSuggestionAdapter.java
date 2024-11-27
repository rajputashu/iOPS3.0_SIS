package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.databinding.AcAdapterItemGuardSuggestionBinding;
import com.sisindia.ai.android.features.issues.grievance.GuardSuggestionViewListeners;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuardsSuggestionAdapter extends ArrayAdapter<GuardSuggestionItem> implements Filterable {

    private List<GuardSuggestionItem> items;
    private ArrayList<GuardSuggestionItem> suggestions = new ArrayList<>();
    private GuardSuggestionViewListeners viewListeners;
    private Context context;

    public GuardsSuggestionAdapter(Context context, List<GuardSuggestionItem> items, GuardSuggestionViewListeners viewListeners) {
        super(context, R.layout.ac_adapter_item_guard_suggestion);
        this.context = context;
        this.items = items;
        this.viewListeners = viewListeners;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public GuardSuggestionItem getItem(int i) {
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

        GuardSuggestionViewHolder holder;

        GuardSuggestionItem item = getItem(i);

        if (view == null) {
            AcAdapterItemGuardSuggestionBinding itemCustomerPlantBinding =
                    DataBindingUtil.inflate(inflater, R.layout.ac_adapter_item_guard_suggestion, viewGroup, false);
            holder = new GuardSuggestionViewHolder(itemCustomerPlantBinding);

            holder.view.setTag(holder);

        } else {
            holder = (GuardSuggestionViewHolder) view.getTag();
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

                if (!TextUtils.isEmpty(constraint) && constraint.length() % 2 == 0) {
                    for (GuardSuggestionItem item : items) {
                        if (item.employeeName != null && item.employeeNo != null) {
                            if (item.employeeName.toLowerCase().contains(constraint.toString().trim().toLowerCase()) ||
                                    item.employeeNo.toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                                suggestions.add(item);
                            } else
                                suggestions.remove(item);
                        }
                    }
                }

                int minCharLength = 2; // By Default value for UNIQ = 2
                if (!TextUtils.isEmpty(constraint) && Prefs.getInt(PrefConstants.COMPANY_ID) == 1)
                    minCharLength = 9;
                else if (!TextUtils.isEmpty(constraint) && Prefs.getInt(PrefConstants.COMPANY_ID) == 4)
                    minCharLength = 9;

                if (suggestions != null && suggestions.size() == 0 && constraint.length() >= minCharLength && constraint.length() < 12 && viewListeners != null) {
                    if (Prefs.getInt(PrefConstants.COMPANY_ID) == 1 || Prefs.getInt(PrefConstants.COMPANY_ID) == 4)
                        viewListeners.fetchGuardFromServer(String.valueOf(constraint).trim());
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
                    addAll((ArrayList<GuardSuggestionItem>) filterResults.values);
                } else {
                    // no filter, add entire original list back in
                    addAll(suggestions);
                }
                notifyDataSetChanged();
            }

            @Override
            public String convertResultToString(Object resultValue) {

                GuardSuggestionItem item = (GuardSuggestionItem) resultValue;

                return item.employeeName;
            }
        };
    }


    private static class GuardSuggestionViewHolder {
        private AcAdapterItemGuardSuggestionBinding binding;
        private View view;

        GuardSuggestionViewHolder(AcAdapterItemGuardSuggestionBinding binding) {
            this.binding = binding;
            this.view = binding.getRoot();
        }
    }

}
