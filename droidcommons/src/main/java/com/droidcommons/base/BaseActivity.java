package com.droidcommons.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.droidcommons.R;
import com.droidcommons.dagger.bottomsheet.HasBottomSheetDialogFragmentInjector;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity implements HasBottomSheetDialogFragmentInjector {

    public static int FRAGMENT_REPLACE = 1;

//    protected Message vMessage = new Message();

    @Inject
    DispatchingAndroidInjector<BottomSheetDialogFragment> dialogFragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.portrait_only))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initViewModel();

        extractBundle();

        initViewBinding();

        onCreated();

        initViewState();
    }

    /*protected void setupToolBarForDrawer(Toolbar toolbar, @DrawableRes int drawableId, boolean isTitleEnable) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(drawableId);
            getSupportActionBar().setDisplayShowTitleEnabled(isTitleEnable);
        }
    }*/

    protected void setupToolBarForBackArrow(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
    }

    protected void toolbarWithCustomTitle(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected abstract void extractBundle();

    protected abstract void initViewState();

    protected abstract void onCreated();

    protected abstract void initViewBinding();

    protected abstract void initViewModel();

    protected abstract @LayoutRes
    int getLayoutResource();

    protected void loadFragment(int viewId, Fragment fragment, int transactionType, boolean isAllowBackStack) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (isAllowBackStack) {
            transaction.add(viewId, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.replace(viewId, fragment, fragment.getClass().getSimpleName());
        }
        transaction.commitAllowingStateLoss();
    }

    protected ViewDataBinding bindActivityView(Activity activity, @LayoutRes int layoutResource) {
        return DataBindingUtil.setContentView(activity, layoutResource);
    }

    protected void showToast(String message) {
        Toast.makeText(this, TextUtils.isEmpty(message) ? "" : message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public AndroidInjector<BottomSheetDialogFragment> bottomSheetDialogFragmentAndroidInjector() {
        return dialogFragmentDispatchingAndroidInjector;
    }
}
