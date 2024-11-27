package com.sisindia.ai.android.di.modules;

import android.content.Context;

import com.droidcommons.base.viewpager.ViewPagerModel;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.commons.RCViewType;
import com.sisindia.ai.android.features.improvementplans.ImprovementPlansFragment;
import com.sisindia.ai.android.features.issues.grievance.GrievanceIssueManagementFragment;
import com.sisindia.ai.android.features.loadfactor.LoadFactorVPFragment;
import com.sisindia.ai.android.features.performance.frags.IncentiveFragment;
import com.sisindia.ai.android.features.performance.frags.PerformanceEffortsFragment;
import com.sisindia.ai.android.features.performance.frags.PerformanceResultsFragment;
import com.sisindia.ai.android.features.performance.frags.SiteTaskSummaryFragment;
import com.sisindia.ai.android.features.predashboard.EffortsFragment;
import com.sisindia.ai.android.features.predashboard.ResultsFragment;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationComplaintFragment;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationGrievanceFragment;
import com.sisindia.ai.android.features.rotacompliance.ComplianceDayWeekMonthFragment;
import com.sisindia.ai.android.features.timline.TodayTimeLineFragment;
import com.sisindia.ai.android.features.timline.YesterDayTimeLineFragment;
import com.sisindia.ai.android.features.uar.UnitAtRiskFragment;
import com.sisindia.ai.android.features.units.details.UnitVPViewType;
import com.sisindia.ai.android.features.units.details.general.UnitGeneralFragment;
import com.sisindia.ai.android.features.units.details.posts.UnitPostsFragment;
import com.sisindia.ai.android.features.units.details.strength.UnitStrengthFragment;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class IopsViewPagerModule {

    @Provides
    @Named("PreDashBoardViewPager")
    ArrayList<ViewPagerModel> getViewPagerModelsForPreDashBoard() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(EffortsFragment.newInstance(), "", R.layout.layout_tab_header_pre_dashboard, 0));
        viewPagers.add(new ViewPagerModel(ResultsFragment.newInstance(), "", R.layout.layout_tab_header_pre_dashboard, 0));
        return viewPagers;
    }

    @Provides
    @Named("RotaComplianceViewPager")
    ArrayList<ViewPagerModel> getViewPagerModelForRotaCompliance(Context context) {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(ComplianceDayWeekMonthFragment.Companion.newInstance(RCViewType.DAY.getViewType()), context.getString(R.string.string_day)));
        viewPagers.add(new ViewPagerModel(ComplianceDayWeekMonthFragment.Companion.newInstance(RCViewType.WEEKLY.getViewType()), context.getString(R.string.string_weekly)));
        viewPagers.add(new ViewPagerModel(ComplianceDayWeekMonthFragment.Companion.newInstance(RCViewType.MONTHLY.getViewType()), context.getString(R.string.string_monthly)));
        return viewPagers;
    }

    @Provides
    @Named("LoadFactorViewPager")
    ArrayList<ViewPagerModel> getViewPagerModelForLoadFactor(Context context) {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(LoadFactorVPFragment.Companion.newInstance(RCViewType.DAY.getViewType()), context.getString(R.string.string_day)));
        viewPagers.add(new ViewPagerModel(LoadFactorVPFragment.Companion.newInstance(RCViewType.WEEKLY.getViewType()), context.getString(R.string.string_weekly)));
        viewPagers.add(new ViewPagerModel(LoadFactorVPFragment.Companion.newInstance(RCViewType.MONTHLY.getViewType()), context.getString(R.string.string_monthly)));
        return viewPagers;
    }

    @Provides
    @Named("UnitDetailsViewPager")
    ArrayList<ViewPagerModel> getViewPagerModelForUnitDetails(Context context) {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(UnitGeneralFragment.Companion.newInstance(UnitVPViewType.GENERAL.getViewType()), context.getString(R.string.string_general)));
        viewPagers.add(new ViewPagerModel(UnitStrengthFragment.Companion.newInstance(), context.getString(R.string.string_strength)));
        viewPagers.add(new ViewPagerModel(UnitPostsFragment.Companion.newInstance(), context.getString(R.string.string_posts)));
        return viewPagers;
    }


    @Provides
    @Named("ReviewInformationComplaintsViewPager")
    ArrayList<ViewPagerModel> getReviewInformationComplaintsViewPager() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(ReviewInformationComplaintFragment.openInstance(), "Open", R.layout.layout_tab_header_pre_dashboard, 0));
        viewPagers.add(new ViewPagerModel(ReviewInformationComplaintFragment.closedInstance(), "Closed", R.layout.layout_tab_header_pre_dashboard, 0));
        return viewPagers;
    }

    @Provides
    @Named("ReviewInformationGrievanceViewPager")
    ArrayList<ViewPagerModel> getReviewInformationGrievanceViewPager() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(ReviewInformationGrievanceFragment.openInstance(), "Open", R.layout.layout_tab_header_pre_dashboard, 0));
        viewPagers.add(new ViewPagerModel(ReviewInformationGrievanceFragment.closedInstance(), "Closed", R.layout.layout_tab_header_pre_dashboard, 0));
        return viewPagers;
    }

    @Provides
    @Named("ReviewInformationPoaViewPager")
    ArrayList<ViewPagerModel> getReviewInformationPoaViewPager() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
//        viewPagers.add(new ViewPagerModel(ResultsFragment.newInstance(), "", R.layout.layout_tab_header_pre_dashboard, 0));
//        viewPagers.add(new ViewPagerModel(EffortsFragment.newInstance(), "", R.layout.layout_tab_header_pre_dashboard, 0));
        return viewPagers;
    }


    @Provides
    @Named("IssueManagement")
    ArrayList<ViewPagerModel> getIssueManagementViewPager() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(GrievanceIssueManagementFragment.Companion.newInstance(), "Grievance"));
        // commenting complaint as per requirement
//        viewPagers.add(new ViewPagerModel(ComplaintIssueManagementFragment.Companion.newInstance(), "Complaints"));

//        viewPagers.add(new ViewPagerModel(ImprovementPlanIssueManagementFragment.Companion.newInstance(), "Improvement Plan"));
        return viewPagers;
    }

    @Provides
    @Named("PerformanceViewPager")
    ArrayList<ViewPagerModel> getPerformanceViewPager() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(PerformanceResultsFragment.Companion.newInstance(), "Results", R.layout.layout_tab_header_pre_dashboard));
        viewPagers.add(new ViewPagerModel(PerformanceEffortsFragment.Companion.newInstance(), "Efforts", R.layout.layout_tab_header_pre_dashboard));
        viewPagers.add(new ViewPagerModel(SiteTaskSummaryFragment.Companion.newInstance(), "Summary", R.layout.layout_tab_header_pre_dashboard));
        viewPagers.add(new ViewPagerModel(IncentiveFragment.Companion.newInstance(), "Incentive", R.layout.layout_tab_header_pre_dashboard));
        return viewPagers;
    }

    //TimeLine
    @Provides
    @Named("TimelineViewPagerModel")
    ArrayList<ViewPagerModel> getTimelineViewPagerModel() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(YesterDayTimeLineFragment.newInstance(), "Yesterday"));
        viewPagers.add(new ViewPagerModel(TodayTimeLineFragment.newInstance(), "Today"));
        return viewPagers;
    }

    @Provides
    @Named("PlanOfActions")
    ArrayList<ViewPagerModel> getPOAViewPager() {
        ArrayList<ViewPagerModel> viewPagers = new ArrayList<>();
        viewPagers.add(new ViewPagerModel(UnitAtRiskFragment.Companion.newInstance(), "Unit At Risk"));
        viewPagers.add(new ViewPagerModel(ImprovementPlansFragment.Companion.newInstance(), "Improvement Plans"));
        return viewPagers;
    }

}
