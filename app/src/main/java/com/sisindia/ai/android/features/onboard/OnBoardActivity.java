package com.sisindia.ai.android.features.onboard;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_LOGIN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_OTP_ENTER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.droidcommons.permissions.RunTimePermissions;
import com.droidcommons.preference.Prefs;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.commons.GroupCompany;
import com.sisindia.ai.android.commons.YesNoDialogFragment;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.databinding.ActivityOnboardBinding;
import com.sisindia.ai.android.features.dashboard.DashBoardActivity;
import com.sisindia.ai.android.features.dashboard.GenericDashboardActivity;
import com.sisindia.ai.android.features.login.number.LoginMobileNumberFragment;
import com.sisindia.ai.android.features.login.otp.EnterOtpFragment;
import com.sisindia.ai.android.features.splash.SplashFragment;
import com.sisindia.ai.android.features.uar.dialog.DialogListener;

import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import timber.log.Timber;

public class OnBoardActivity extends IopsBaseActivity {

    OnBoardViewModel viewModel = null;
    ActivityOnboardBinding binding = null;
    private boolean isReturnedFromSettings = false;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, OnBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {
                case OPEN_LOGIN:
                    openLoginMobileNumberFragment();
                    break;

                case OPEN_OTP_ENTER:
                    openEnterOtpFragment();
                    break;

                case OPEN_DASH_BOARD:
                    openDashBoardScreen();
                    break;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RunTimePermissions.RC_APP_PERMISSION)
            checkIsAllPermissionGranted();
        else if (requestCode == RunTimePermissions.BG_LOCATION_PERMISSION)
            checkBGLocationPermissionGranted();
        else if (requestCode == RunTimePermissions.POST_NOTIFICATION_PERMISSION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                checkPostNotificationPermissionGranted();
    }

    private void checkIsAllPermissionGranted() {
        if (RunTimePermissions.hasAppPermissions(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                onCreated();
            else
                enableLocationIfRequired();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String message = "Important Message!\n\niOPS2.0 needs all permission. Kindly allow required permission without denial";
                openLocationPermissionSettingDialog(false, message);
            } else {
                requestPermissions(RunTimePermissions.getAllAppPermissions(), RunTimePermissions.RC_APP_PERMISSION);
            }
        }
    }

    private void checkBGLocationPermissionGranted() {
        if (RunTimePermissions.hasBGLocationGranted(this)) {
//            enableLocationIfRequired();
            requestForPostNotification();
        } else {
            String message = "Important Message!\n\niOPS2.0 need full access of location service. Kindly choose\n\nPermission -> Location -> Allow all the time";
            openLocationPermissionSettingDialog(true, message);
        }
    }

    private void checkPostNotificationPermissionGranted() {
        if (RunTimePermissions.hasPostNotificationGranted(this)) {
            enableLocationIfRequired();
        } else {
            String message = "Notification permission is required to mark your duty on and off, Kindly grant";
            showToast(message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                ActivityCompat.requestPermissions(this, RunTimePermissions.getPostNotificationPermissions(), RunTimePermissions.POST_NOTIFICATION_PERMISSION);
        }
    }

    private void openEnterOtpFragment() {
        loadFragment(R.id.flOnBoard, EnterOtpFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreated() {
        if (RunTimePermissions.hasAppPermissions(this)) {
            if (RunTimePermissions.hasBGLocationGranted(this))
                requestForPostNotification();
            else
                ActivityCompat.requestPermissions(this, RunTimePermissions.getBGLocationPermissions(), RunTimePermissions.BG_LOCATION_PERMISSION);
        } else
            ActivityCompat.requestPermissions(this, RunTimePermissions.getAllAppPermissions(), RunTimePermissions.RC_APP_PERMISSION);
    }

    private void openSplashFragment() {
        loadFragment(R.id.flOnBoard, SplashFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openLoginMobileNumberFragment() {
        loadFragment(R.id.flOnBoard, LoginMobileNumberFragment.newInstance(), FRAGMENT_REPLACE, false);
        openIntroScreens(PrefConstants.MODULE_LOGIN);
    }

    private void openDashBoardScreen() {
        removeModuleBasedPrefs();
        int companyId = Prefs.getInt(PrefConstants.COMPANY_ID, 1);
        if (companyId == GroupCompany.SIS.getCompanyId())
            startActivity(DashBoardActivity.newIntent(this));
        else if (companyId == GroupCompany.UNIQ.getCompanyId() || companyId == GroupCompany.SLV.getCompanyId())
            startActivity(GenericDashboardActivity.Companion.newIntent(this));
        else
            startActivity(DashBoardActivity.newIntent(this));
        finish();
//        hideAppIcon();
    }

    @Override
    protected void initViewBinding() {
        binding = (ActivityOnboardBinding) bindActivityView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (OnBoardViewModel) getAndroidViewModel(OnBoardViewModel.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_onboard;
    }

    public void enableLocationIfRequired() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(10000 / 2)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {

                    LocationParams params = (new LocationParams.Builder()).setAccuracy(LocationAccuracy.HIGH).setDistance(0.0F).setInterval(500L).build();
                    SmartLocation.with(this).location().config(params).start(location -> {
                        if (location != null) {
                            Prefs.putDouble(PrefConstants.LATITUDE, location.getLatitude());
                            Prefs.putDouble(PrefConstants.LONGITUDE, location.getLongitude());
                            SmartLocation.with(this).location().stop();
                        }
                    });
                    openSplashFragment();
                })
                .addOnFailureListener(this, ex -> {
                    if (ex instanceof ResolvableApiException) {
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(OnBoardActivity.this, IntentRequestCodes.REQUEST_CODE_RESOLUTION_REQUIRED);
                        } catch (IntentSender.SendIntentException sendEx) {
                            Timber.e(sendEx);
                            onBackPressed();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodes.REQUEST_CODE_RESOLUTION_REQUIRED) {
            if (resultCode == Activity.RESULT_OK)
                openSplashFragment();
            else
                onBackPressed();
        }
    }

    private void openLocationPermissionSettingDialog(boolean isForLocation, String message) {
        YesNoDialogFragment fragment = YesNoDialogFragment.newSingleButtonInstance(message);
        fragment.setCancelable(false);
        fragment.initDialogListener(new DialogListener() {
            @Override
            public void onCrossButtonClick() {

            }

            @Override
            public void onViewAllPOAClick() {

            }

            @Override
            public void onYesButtonClicked() {
                fragment.dismissAllowingStateLoss();
                if (isForLocation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ActivityCompat.checkSelfPermission(OnBoardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            openSettingScreen();
                        } else
                            openSettingScreen();
                    } else
                        openSettingScreen();
                } else {
                    ActivityCompat.requestPermissions(OnBoardActivity.this, RunTimePermissions.getAllAppPermissions(), RunTimePermissions.RC_APP_PERMISSION);
                }
            }

            @Override
            public void onNoButtonClicked() {

            }
        });
        fragment.show(getSupportFragmentManager(), YesNoDialogFragment.class.getSimpleName());
    }

    private void openSettingScreen() {
        isReturnedFromSettings = true;
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReturnedFromSettings) {
            isReturnedFromSettings = false;
            checkIsAllPermissionGranted();
        }
    }

    //REMOVING THOSE PREFS WHO ARE USED TO HOLD TEMP VALUES FOR PARTICULAR TASK ONLY
    private void removeModuleBasedPrefs() {
        try {
            Prefs.edit().remove(PrefConstants.SELECTED_EMPLOYEE_ID)
                    .remove(PrefConstants.CURRENT_TASK)
                    .remove(PrefConstants.CURRENT_POST)
                    .remove(PrefConstants.CURRENT_SITE)
                    .remove(PrefConstants.SELECTED_EMPLOYEE_NO)
                    .remove(PrefConstants.SELECTED_CLIENT_ID)
                    .remove(PrefConstants.IS_CREATING_BARRACK_TASK)
                    .remove(PrefConstants.CURRENT_BARRACK_ID)
                    .remove(PrefConstants.TASK_SERVER_ID)
                    .remove(PrefConstants.CURRENT_POST_ID_FOR_EDIT)
                    .remove(PrefConstants.POA_ID_FOR_ATTACHMENT)
                    .remove(PrefConstants.SELECTED_CLIENT_CUSTOMER_ID)
                    .remove(PrefConstants.AKR_KIT_ISSUE_NO)
                    .remove(PrefConstants.AKR_GUARD_ID)
                    .remove(PrefConstants.AKR_SITE_ID)
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestForPostNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (RunTimePermissions.hasPostNotificationGranted(this))
                enableLocationIfRequired();
            else
                ActivityCompat.requestPermissions(this, RunTimePermissions.getPostNotificationPermissions(), RunTimePermissions.POST_NOTIFICATION_PERMISSION);
        } else
            enableLocationIfRequired();
    }

    /*void hideAppIcon() {
//        boolean isAppInstalled = appInstalledOrNot("com.sisindia.ai.android.dev");
        if (isAppInstalled) {
            //This intent will help you to launch if the package is already installed
            Timber.d("SampleLog Application is already installed.");
//            PackageManager p = getPackageManager();
            // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
            ComponentName componentName = new ComponentName(this, com.sisindia.ai.android.features.onboard.OnBoardActivity.class);
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        } else {
            // Do whatever we want to do if application not installed
            Timber.d("SampleLog Application is not currently installed.");
        }
    }*/
}
