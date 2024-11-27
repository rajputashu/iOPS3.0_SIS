package com.sisindia.ai.android.features.login.number;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.databinding.AdapterSpinnerCountryItemBinding;
import com.sisindia.ai.android.models.SisCountry;

import java.util.ArrayList;

public class CountrySpinnerAdapter extends ArrayAdapter<SisCountry> {

    private final Context context;
    private final ArrayList<SisCountry> countries;
    private final @LayoutRes
    int resourceId;

    public CountrySpinnerAdapter(@NonNull Context context, int resource, ArrayList<SisCountry> countries) {
        super(context, resource);
        this.context = context;
        this.countries = countries;
        this.resourceId = resource;
    }


    @Nullable
    @Override
    public SisCountry getItem(int position) {
        return countries.get(position);
    }

    @Override
    public int getCount() {
        return countries.size();
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
            AdapterSpinnerCountryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), resourceId, null, false);
            rowView = binding.getRoot();
            rowView.setTag(binding);
        }
        SisCountry item = getItem(position);
        AdapterSpinnerCountryItemBinding binding = (AdapterSpinnerCountryItemBinding) rowView.getTag();
        binding.setItem(item);
        return rowView;
    }
}
