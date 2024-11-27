package com.sisindia.ai.android.features.addgrievances;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.databinding.AdapterActionPlanItemBinding;
import com.sisindia.ai.android.uimodels.ActionPlanModel;

import java.util.List;

public class ActionPlanSpinnerAdapter extends ArrayAdapter<ActionPlanModel> {

    private final Context context;
    private final List<ActionPlanModel> actionPlans;
    private final @LayoutRes
    int resourceId;

    public ActionPlanSpinnerAdapter(@NonNull Context context, int resource, List<ActionPlanModel> actionPlans) {
        super(context, resource);
        this.context = context;
        this.actionPlans = actionPlans;
        this.resourceId = resource;
    }


    @Nullable
    @Override
    public ActionPlanModel getItem(int position) {
        return actionPlans.get(position);
    }

    @Override
    public int getCount() {
        return actionPlans.size();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return rowView(convertView, position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return rowView(convertView, position);
    }

    private View rowView(View convertView, int position) {

        View rowView = convertView;
        if (rowView == null) {
            AdapterActionPlanItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), resourceId, null, false);
            rowView = binding.getRoot();
            rowView.setTag(binding);
        }
        ActionPlanModel item = getItem(position);
        AdapterActionPlanItemBinding binding = (AdapterActionPlanItemBinding) rowView.getTag();
        binding.setItem(item);
        return rowView;
    }
}
