package com.sisindia.ai.android.features.login;

import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.features.login.number.CountrySpinnerAdapter;
import com.sisindia.ai.android.models.SisCountry;

import java.util.ArrayList;

import timber.log.Timber;

public class LoginViewBindings {


    @BindingAdapter(value = {"countryItems", "countrySpinnerListener"})
    public static void bindCountrySpinner(AppCompatSpinner spinner, ArrayList<SisCountry> list, LoginViewListeners listeners) {

        if (list != null && !list.isEmpty()) {
            CountrySpinnerAdapter adapter = new CountrySpinnerAdapter(spinner.getContext(), R.layout.adapter_spinner_country_item, list);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    SisCountry item = (SisCountry) spinner.getAdapter().getItem(i);
                    listeners.onCountrySelected(item);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Timber.e("");
                }
            });
        }
    }


    @BindingAdapter(value = {"selectedCountry"})
    public static void bindCountryLengthValidation(AppCompatEditText editText, SisCountry country) {
        editText.setText("");
        if (country.getMinLength() != 0 && country.getMaxLength() != 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(country.getMaxLength())});
        }
    }
}
