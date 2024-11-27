package com.sisindia.ai.android.features.addgrievances;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.FragmentSelectGuardForAddGrievanceBinding;
import com.sisindia.ai.android.mlcore.ScanQRActivity;

import static android.app.Activity.RESULT_OK;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_GUARD_SCAN;

public class SelectGuardForAddGrievanceFragment extends IopsBaseFragment {

    private AddGrievanceSelectGuardViewModel viewModel;

    public static SelectGuardForAddGrievanceFragment newInstance() {
        return new SelectGuardForAddGrievanceFragment();
    }

    @Override
    protected void extractBundle() {

    }

    private void openScanQrScreen() {
        startActivityForResult(ScanQRActivity.newIntent(requireActivity()), IntentRequestCodes.REQUEST_CODE_OPEN_GUARD_SCAN);
    }

    @Override
    protected void initViewModel() {
        viewModel = (AddGrievanceSelectGuardViewModel) getAndroidViewModel(AddGrievanceSelectGuardViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentSelectGuardForAddGrievanceBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == NavigationConstants.ON_SCAN_GUARD_QR_CLICK)
                openScanQrScreen();
        });
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GUARD_SCAN) {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null &&
                    data.getExtras().containsKey(IntentConstants.ON_QR_SCANNED)) {
                String qrRawData = data.getExtras().getString(IntentConstants.ON_QR_SCANNED);
                viewModel.onGuardQrScanned(qrRawData);
            } else {
                Toast.makeText(requireActivity(), "Error in Qr Scan.. Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_select_guard_for_add_grievance;
    }
}
