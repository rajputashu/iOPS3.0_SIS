package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityScanGuardBinding;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.available.GuardAvailableFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable.GuardNotAvailableFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.PostGuardScanActivity;

public class ScanGuardActivity extends IopsBaseActivity {

    private ActivityScanGuardBinding binding;
    private ScanGuardViewModel viewModel;

    private boolean isPractoTask = false;

    public static Intent newIntent(Activity activity, boolean isPractoTask) {
        return new Intent(activity, ScanGuardActivity.class)
                .putExtra(IntentConstants.IS_PRACTO_TASK, isPractoTask);
    }

    @Override
    protected void extractBundle() {
        isPractoTask = getIntent().getBooleanExtra(IntentConstants.IS_PRACTO_TASK, false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_scan_guard;
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_GUARD_AVAILABLE:
                    onGuardAvailable();
                    break;

                case NavigationConstants.ON_GUARD_SLEEPING:
                    onGuardSleeping();
                    break;

                case NavigationConstants.ON_OPEN_GUARD_NOT_AVAILABLE:
                    openGuardNotAvailableScreen();
                    break;

                case NavigationConstants.ON_GUARD_NOT_AVAILABLE_CHECK:
                    onGuardNotAvailableCheck();
                    break;
            }
        });
    }

    private void onGuardNotAvailableCheck() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void openGuardNotAvailableScreen() {
        loadFragment(R.id.flScanGuardId, GuardNotAvailableFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    /*
     *  USE BELOW METHOD TO OPEN FRAGMENT HAVING {SCANNER, MANUAL_GUARD_TYPE,GUARD_NA,GUARD_SLEEPING }
     */
    private void openScanGuardQrScreen() {
        loadFragment(R.id.flScanGuardId, GuardAvailableFragment.newInstance(isPractoTask), FRAGMENT_REPLACE, false);
    }

    /*private void openSleepingGuardPreviewScreen(Intent data) {
        loadFragment(R.id.flScanGuardId, SleepingGuardPreviewFragment.newInstance(data), FRAGMENT_REPLACE, false);
    }*/

    /**
     * on guard selected from suggestion
     */
    private void onGuardAvailable() {
        startActivity(PostGuardScanActivity.newIntent(this));
        this.finish();
    }

    /**
     * on guard sleeping
     */
    private void onGuardSleeping() {
        if (isPractoTask)
            setResult(Activity.RESULT_OK);
        else
            startActivity(PostGuardScanActivity.newIntent(this));
        this.finish();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_scan_guard, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (ScanGuardViewModel) getAndroidViewModel(ScanGuardViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreated() {

        setupToolBarForBackArrow(binding.tbScanGuard);
        openScanGuardQrScreen();
        /*if (RunTimePermissions.checkCameraPermissions(this)) {
            openScanGuardQrScreen();
        } else {
            requestPermissions(RunTimePermissions.getCameraPermissions(), RunTimePermissions.RC_CAMERA_PERMISSION);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openScanGuardQrScreen();
        /*if (requestCode == RunTimePermissions.RC_CAMERA_PERMISSION) {
            if (RunTimePermissions.checkCameraPermissions(this))
                openScanGuardQrScreen();
            else
                Toast.makeText(this, "U need to enable Permissions to open Camera", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.stopTimer();
    }
}
