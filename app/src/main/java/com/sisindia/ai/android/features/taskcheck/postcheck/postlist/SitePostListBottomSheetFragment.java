package com.sisindia.ai.android.features.taskcheck.postcheck.postlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.databinding.BottomSheetSitePostListBinding;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_SITE_POST_CLICK;

public class SitePostListBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private BottomSheetSitePostListBinding binding;
    private SitePostListViewModel viewModel;

    public static SitePostListBottomSheetFragment newInstance() {
        return new SitePostListBottomSheetFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    protected void initViewModel() {
        viewModel = (SitePostListViewModel) getAndroidViewModel(SitePostListViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == ON_SITE_POST_CLICK) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_site_post_list;
    }
}
