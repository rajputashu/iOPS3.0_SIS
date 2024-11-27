package com.droidcommons.base.viewpager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ViewPagerModel> pagerList;

    public CustomViewPagerAdapter(FragmentManager fm, List<ViewPagerModel> pagerList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.pagerList = pagerList;
    }

    @NotNull
    @Override
    public Fragment getItem(int i) {
        return pagerList.get(i).fragment;
    }

    @Override
    public int getCount() {
        return (pagerList == null) ? 0 : pagerList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pagerList.get(position).pageTitle;
    }
}