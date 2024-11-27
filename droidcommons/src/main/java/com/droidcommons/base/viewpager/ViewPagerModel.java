package com.droidcommons.base.viewpager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

public class ViewPagerModel {

    public Fragment fragment;

    public String pageTitle;

    @LayoutRes
    public int customTabView;

    @IdRes
    public int iconResourceId;

    public ViewPagerModel(Fragment fragment, String pageTitle, int customView) {
        this.fragment = fragment;
        this.pageTitle = pageTitle;
        this.customTabView = customView;
    }

    public ViewPagerModel(Fragment fragment, String pageTitle) {
        this.fragment = fragment;
        this.pageTitle = pageTitle;
    }

    public ViewPagerModel(Fragment fragment, String pageTitle, int customTabView, int iconResourceId) {
        this.fragment = fragment;
        this.pageTitle = pageTitle;
        this.customTabView = customTabView;
        this.iconResourceId = iconResourceId;
    }
}
