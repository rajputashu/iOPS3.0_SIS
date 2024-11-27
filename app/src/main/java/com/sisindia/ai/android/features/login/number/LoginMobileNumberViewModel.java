package com.sisindia.ai.android.features.login.number;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.login.LoginViewListeners;
import com.sisindia.ai.android.models.SisCountry;
import com.sisindia.ai.android.models.UserOnBoardModel;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_OTP_ENTER;

public class LoginMobileNumberViewModel extends IopsBaseViewModel {

    @Inject
    public ObservableField<ArrayList<SisCountry>> countries;

    public ObservableField<String> mobileNumber = new ObservableField<>("");

    public ObservableField<SisCountry> country = new ObservableField<>(new SisCountry());

    public LoginViewListeners loginViewListeners = new LoginViewListeners() {
        @Override
        public void onCountrySelected(SisCountry selectedCountry) {
            country.set(selectedCountry);
        }
    };


    @Inject
    public LoginMobileNumberViewModel(@NonNull Application application) {
        super(application);
    }


    public void onContinueBtnClick(View view) {

        String phoneNumber = mobileNumber.get();
        if (!TextUtils.isEmpty(phoneNumber) && country != null && country.get() != null && Objects.requireNonNull(country.get()).getCountryCode() != null) {
            setIsLoading(true);
            SisCountry selectedCountry = country.get();
            Prefs.putString(PrefConstants.COUNTRY_CODE, selectedCountry.getCountryCode());
            Prefs.putInt(PrefConstants.COUNTRY_ID, selectedCountry.getCountryId());
            Prefs.putString(PrefConstants.USER_MOBILE_NUMBER, phoneNumber);
            UserOnBoardModel request = new UserOnBoardModel(selectedCountry.getCountryCode(), selectedCountry.getCountryId(), phoneNumber, false);
            String preAuthToken = Prefs.getString(PrefConstants.PRE_AUTH_TOKEN_KEY);

            addDisposable(coreApi
                    .sendOtp(preAuthToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onMobileNumberSubmitSuccess, this::onApiError));
        } else {
            Toast.makeText(getApplication(), "Mobile number Or Country Invalid!!!.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onMobileNumberSubmitSuccess(BaseNetworkResponse response) {
        setIsLoading(false);
        if (response.statusCode == 200) {
            message.what = OPEN_OTP_ENTER;
            liveData.postValue(message);
        } else {
            Toast.makeText(getApplication(), response.statusMessage, Toast.LENGTH_LONG).show();
        }
    }
}
