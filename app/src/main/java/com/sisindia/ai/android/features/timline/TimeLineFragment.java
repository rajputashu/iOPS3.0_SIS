package com.sisindia.ai.android.features.timline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.droidcommons.base.viewpager.CustomViewPagerAdapter;
import com.droidcommons.base.viewpager.ViewPagerModel;
import com.google.android.material.tabs.TabLayout;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentTimeLineBinding;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

public class TimeLineFragment extends IopsBaseFragment implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    @Inject
    @Named("TimelineViewPagerModel")
    public ArrayList<ViewPagerModel> viewPagers;
    private FragmentTimeLineBinding binding;
    private TimeLineViewModel viewModel;
    private int tabPosition = 1;

    public static TimeLineFragment newInstance() {
        return new TimeLineFragment();
    }

    @Override
    protected void extractBundle() {

    }

    private void initViewPager() {
        CustomViewPagerAdapter viewPagerAdapter = new CustomViewPagerAdapter(getChildFragmentManager(), viewPagers);
        binding.vpTimeLine.setAdapter(viewPagerAdapter);
        binding.vpTimeLine.addOnPageChangeListener(this);
        binding.tlTimeLine.setupWithViewPager(binding.vpTimeLine);
        binding.tlTimeLine.addOnTabSelectedListener(this);
        binding.vpTimeLine.setCurrentItem(tabPosition);
    }

    @Override
    protected void initViewModel() {
        viewModel = (TimeLineViewModel) getAndroidViewModel(TimeLineViewModel.class);
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

    }

    @Override
    protected void onCreated() {
        initViewPager();
        binding.swipeLayout.setOnRefreshListener(() -> {
            initViewPager();
            binding.swipeLayout.setRefreshing(false);
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_time_line;
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
        binding.vpTimeLine.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
