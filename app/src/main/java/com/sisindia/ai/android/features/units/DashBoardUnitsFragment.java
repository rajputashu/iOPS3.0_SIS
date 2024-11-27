package com.sisindia.ai.android.features.units;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentDashBoardUnitsBinding;
import com.sisindia.ai.android.features.units.details.UnitDetailActivity;

import java.util.Objects;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_UNITS_DETAILS;

public class DashBoardUnitsFragment extends IopsBaseFragment implements Toolbar.OnMenuItemClickListener {

    private DashBoardUnitsViewModel viewModel;

    public static DashBoardUnitsFragment newInstance() {
        return new DashBoardUnitsFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_dash_board_units;
    }

    @Override
    protected void extractBundle() {
    }

    @Override
    protected void initViewModel() {
        viewModel = (DashBoardUnitsViewModel) getAndroidViewModel(DashBoardUnitsViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentDashBoardUnitsBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == OPEN_UNITS_DETAILS) {
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), UnitDetailActivity.class));
            }
        });
    }

    @Override
    protected void onCreated() {
        viewModel.getUnitsDetailsFromDB();
        viewModel.getQRCountsFromDB();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
