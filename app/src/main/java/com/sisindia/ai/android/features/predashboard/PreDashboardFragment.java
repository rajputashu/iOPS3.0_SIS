package com.sisindia.ai.android.features.predashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.droidcommons.base.viewpager.CustomViewPagerAdapter;
import com.droidcommons.base.viewpager.ViewPagerModel;
import com.google.android.material.tabs.TabLayout;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentPreDashboardBinding;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import static com.sisindia.ai.android.constants.IntentConstants.PRE_DASH_BOARD_EFFORTS_SIZE;
import static com.sisindia.ai.android.constants.IntentConstants.PRE_DASH_BOARD_RESULTS_SIZE;
import static com.sisindia.ai.android.constants.NavigationConstants.PRE_DASH_BOARD_TAB_DATA;

public class PreDashboardFragment extends IopsBaseFragment implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    @Inject
    @Named("PreDashBoardViewPager")
    ArrayList<ViewPagerModel> viewPagers;

    private FragmentPreDashboardBinding binding;
    private PreDashboardViewModel viewModel;

    private int tabPosition = 0;

    public static PreDashboardFragment newInstance() {
        return new PreDashboardFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (PreDashboardViewModel) getAndroidViewModel(PreDashboardViewModel.class);
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
            switch (message.what) {
                case PRE_DASH_BOARD_TAB_DATA:
                    Bundle bundle = message.getData();
                    if (bundle != null) {
                        /*int resultsCount = bundle.getInt(PRE_DASH_BOARD_RESULTS_SIZE);
                        int effortsCount = bundle.getInt(PRE_DASH_BOARD_EFFORTS_SIZE);
                        viewPagers.get(0).pageTitle = getString(R.string.string_results) +
                                getString(R.string.symbol_open_bracket) + resultsCount +
                                getString(R.string.symbol_close_bracket);
                        viewPagers.get(1).pageTitle = getString(R.string.string_efforts) +
                                getString(R.string.symbol_open_bracket) + effortsCount +
                                getString(R.string.symbol_close_bracket);*/
                        viewPagers.get(0).pageTitle = getString(R.string.string_efforts);
                        viewPagers.get(1).pageTitle = getString(R.string.string_results);
                        setUpTabLayout();
                    }
                    break;
            }
        });
    }

    @Override
    protected void onCreated() {

        initViewPager();

    }

    private void setUpTabLayout() {
        for (int i = 0; i < viewPagers.size(); i++) {
            ViewPagerModel model = viewPagers.get(i);
            View view = LayoutInflater.from(getActivity()).inflate(model.customTabView, null);
            if (view != null && binding.tlPreDashboard.getTabAt(i) != null) {
                binding.tlPreDashboard.getTabAt(i).setCustomView(view);
                ((AppCompatTextView) view.findViewById(R.id.tvTabTitle)).setText(model.pageTitle);
            }
        }
    }

    private void initViewPager() {
        CustomViewPagerAdapter viewPagerAdapter = new CustomViewPagerAdapter(getChildFragmentManager(), viewPagers);
        binding.vpPreDashboard.setAdapter(viewPagerAdapter);
        binding.vpPreDashboard.addOnPageChangeListener(this);
        binding.tlPreDashboard.setupWithViewPager(binding.vpPreDashboard);
        binding.tlPreDashboard.addOnTabSelectedListener(this);
        binding.vpPreDashboard.setCurrentItem(tabPosition);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pre_dashboard;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.tabPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        binding.vpPreDashboard.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
