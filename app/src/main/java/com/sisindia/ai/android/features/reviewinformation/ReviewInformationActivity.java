package com.sisindia.ai.android.features.reviewinformation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.droidcommons.base.viewpager.CustomViewPagerAdapter;
import com.droidcommons.base.viewpager.ViewPagerModel;
import com.google.android.material.tabs.TabLayout;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.databinding.ActivityReviewInformationBinding;
import com.sisindia.ai.android.features.taskcheck.DayNightCheckActivity;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_FINISH_ACTIVITY;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DAY_CHECK_SCREEN;

public class ReviewInformationActivity extends IopsBaseActivity {

    @Inject
    @Named("ReviewInformationComplaintsViewPager")
    public ArrayList<ViewPagerModel> complaintsViewPagerList;

    @Inject
    @Named("ReviewInformationGrievanceViewPager")
    public ArrayList<ViewPagerModel> grievancesViewPagerList;

    @Inject
    @Named("ReviewInformationPoaViewPager")
    public ArrayList<ViewPagerModel> poasViewPagerList;

    private ActivityReviewInformationBinding binding;
    private ReviewInformationViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, ReviewInformationActivity.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_review_information;
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == OPEN_DAY_CHECK_SCREEN)
                openDayCheckScreen();
            else if (message.what == ON_FINISH_ACTIVITY)
                finish();
        });
    }

    private void openDayCheckScreen() {
        startActivityForResult(DayNightCheckActivity.newIntent(this), IntentRequestCodes.REQUEST_CODE_START_DAY_CHECK);
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbReviewInformation);
        viewModel.initViewModel();
        int complaintTabPosition = 0;
        initViewPager(complaintsViewPagerList, binding.vpComplaints, binding.tlComplaints, complaintTabPosition);
        int grievanceTabPosition = 0;
        initViewPager(grievancesViewPagerList, binding.vpGrievance, binding.tlGrievances, grievanceTabPosition);
        int poaTabPosition = 0;
        initViewPager(poasViewPagerList, binding.vpPoAs, binding.tlPoAs, poaTabPosition);
    }

    private void initViewPager(ArrayList<ViewPagerModel> list, ViewPager viewPager, TabLayout tabLayout, int tabPosition) {
        CustomViewPagerAdapter viewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(tabPosition);
        initTabLayout(list, tabLayout);
    }

    private void initTabLayout(ArrayList<ViewPagerModel> viewPagers, TabLayout tabLayout) {
        for (int i = 0; i < viewPagers.size(); i++) {
            ViewPagerModel model = viewPagers.get(i);
            if (model.customTabView != 0) {
                View view = getLayoutInflater().inflate(model.customTabView, null);
                if (view != null && tabLayout.getTabAt(i) != null) {
                    Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(view);
                    ((AppCompatTextView) view.findViewById(R.id.tvTabTitle)).setText(model.pageTitle);
                }
            }
        }
    }

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (ReviewInformationViewModel) getAndroidViewModel(ReviewInformationViewModel.class);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_review_information, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodes.REQUEST_CODE_START_DAY_CHECK) {
            if (resultCode == RESULT_OK) {
                setResult(resultCode);
                this.finish();
            } else
                onCreated();
        }
    }
}
