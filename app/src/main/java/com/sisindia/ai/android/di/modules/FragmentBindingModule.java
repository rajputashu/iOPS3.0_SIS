package com.sisindia.ai.android.di.modules;

import com.droidcommons.dagger.qualifier.FragmentScoped;
import com.sisindia.ai.android.features.addgrievances.GuardGrievanceDetailFragment;
import com.sisindia.ai.android.features.addgrievances.SelectGuardForAddGrievanceFragment;
import com.sisindia.ai.android.features.addtask.CreateTaskFragment;
import com.sisindia.ai.android.features.addtask.SelectTaskFragment;
import com.sisindia.ai.android.features.ailocation.AILocationFragment;
import com.sisindia.ai.android.features.akr.AKRFragment;
import com.sisindia.ai.android.features.akr.details.KitAssignedDistributedFragment;
import com.sisindia.ai.android.features.akr.details.KitReplaceFragment;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackInspectionHomeFragment;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackMetLandlordFragment;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackOthersFragment;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackSpaceFragment;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackStrengthFragment;
import com.sisindia.ai.android.features.barracks.listing.BarrackListingFragment;
import com.sisindia.ai.android.features.billsubmit.sheet.BillChecklistBottomSheet;
import com.sisindia.ai.android.features.civil.CivilDefenceFragment;
import com.sisindia.ai.android.features.conveyance.ConveyanceFragment;
import com.sisindia.ai.android.features.conveyance.ConveyanceMonthlyFragment;
import com.sisindia.ai.android.features.disband.DisbandmentFragment;
import com.sisindia.ai.android.features.imagecapture.CaptureImageFragmentV2;
import com.sisindia.ai.android.features.imagecapture.PreviewImageFragmentV2;
import com.sisindia.ai.android.features.improvementplans.ImprovementPlansFragment;
import com.sisindia.ai.android.features.issues.IssueManagementFragment;
import com.sisindia.ai.android.features.issues.complaints.ComplaintIssueManagementFragment;
import com.sisindia.ai.android.features.issues.grievance.GrievanceIssueManagementFragment;
import com.sisindia.ai.android.features.issues.improvementplan.ImprovementPlanIssueManagementFragment;
import com.sisindia.ai.android.features.kpi.MyKpiFragment;
import com.sisindia.ai.android.features.livecamera.CaptureImageFragment;
import com.sisindia.ai.android.features.livecamera.LiveImageRenderFragment;
import com.sisindia.ai.android.features.loadfactor.LoadFactorVPFragment;
import com.sisindia.ai.android.features.login.number.LoginMobileNumberFragment;
import com.sisindia.ai.android.features.login.otp.EnterOtpFragment;
import com.sisindia.ai.android.features.mask.MaskDistributionFragment;
import com.sisindia.ai.android.features.nudges.NudgesDashboardFragment;
import com.sisindia.ai.android.features.performance.PerformanceFragment;
import com.sisindia.ai.android.features.performance.frags.IncentiveFragment;
import com.sisindia.ai.android.features.performance.frags.PerformanceEffortsFragment;
import com.sisindia.ai.android.features.performance.frags.PerformanceResultsFragment;
import com.sisindia.ai.android.features.performance.frags.SiteTaskSummaryFragment;
import com.sisindia.ai.android.features.poa.PlansOfActionFragment;
import com.sisindia.ai.android.features.predashboard.EffortsFragment;
import com.sisindia.ai.android.features.predashboard.PreDashboardFragment;
import com.sisindia.ai.android.features.predashboard.ResultsFragment;
import com.sisindia.ai.android.features.recruitment.RecruitmentFragment;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationComplaintFragment;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationGrievanceFragment;
import com.sisindia.ai.android.features.rota.RotaFragment;
import com.sisindia.ai.android.features.rotacompliance.ComplianceDayWeekMonthFragment;
import com.sisindia.ai.android.features.sales.SalesReferenceFragment;
import com.sisindia.ai.android.features.selfie.SelfiePreviewFragmentV2;
import com.sisindia.ai.android.features.selfservice.SelfServiceFragment;
import com.sisindia.ai.android.features.splash.SplashFragment;
import com.sisindia.ai.android.features.sync.ManualSyncFragment;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientFeedbackFragment;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientHandShakeAddComplaintFragment;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientHandshakeFragment;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.SummaryOfHandshakeFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.available.GuardAvailableFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable.GuardNotAvailableFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardPhotoEvaluationResultFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.GuardScanSuccessFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine.GuardAddRewardFineFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.signature.AddSignatureFragment;
import com.sisindia.ai.android.features.timline.TimeLineFragment;
import com.sisindia.ai.android.features.timline.TodayTimeLineFragment;
import com.sisindia.ai.android.features.timline.YesterDayTimeLineFragment;
import com.sisindia.ai.android.features.uar.UnitAtRiskFragment;
import com.sisindia.ai.android.features.units.DashBoardUnitsFragment;
import com.sisindia.ai.android.features.units.details.general.UnitGeneralFragment;
import com.sisindia.ai.android.features.units.details.posts.UnitPostsFragment;
import com.sisindia.ai.android.features.units.details.strength.UnitStrengthFragment;
import com.sisindia.ai.android.features.units.sheet.AddEquipmentBottomSheet;
import com.sisindia.ai.android.features.webviews.EventsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SplashFragment bindSplashFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract LoginMobileNumberFragment bindLoginMobileNumberFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract EnterOtpFragment bindEnterOtpFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RotaFragment bindDashBoardRotaFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract DashBoardUnitsFragment bindDashBoardUnitsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PreDashboardFragment bindPreDashboardFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract EffortsFragment bindEffortsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ResultsFragment bindResultsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract GuardScanSuccessFragment bindGuardCheckScanResultFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract GuardPhotoEvaluationResultFragment bindGuardPhotoEvaluationResultFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SelectTaskFragment bindGSelectTaskFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract CaptureImageFragment bindBarcodeDetectionFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract LiveImageRenderFragment bindLiveImageRenderFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract CreateTaskFragment bindAddDayCheckFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract GuardAddRewardFineFragment bindGuardDutyHistoryFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddSignatureFragment bindAddSignatureFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ComplianceDayWeekMonthFragment bindCompDayWeekMonthFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SelectGuardForAddGrievanceFragment bindAddGrievancesGuardDetailsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract GuardGrievanceDetailFragment bindAddGrievancesDetailsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract LoadFactorVPFragment bindLFDayWeekMonthFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract UnitGeneralFragment bindGeneralFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract UnitStrengthFragment bindStrengthFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract UnitPostsFragment bindPostsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddEquipmentBottomSheet bindAddEquipmentFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AILocationFragment bindAiLocationFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract BillChecklistBottomSheet bindBillChecklistFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract GuardAvailableFragment bindScanGuardQrFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract GuardNotAvailableFragment bindGuardNotAvailableFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract UnitAtRiskFragment bindUARFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ReviewInformationGrievanceFragment bindReviewInformationPagerFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ClientHandshakeFragment bindHandshakeFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ClientFeedbackFragment bindClientFeedbackFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SummaryOfHandshakeFragment bindSummaryHandshakeFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AKRFragment bindAKRFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract KitAssignedDistributedFragment bindAssignDistributeFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract KitReplaceFragment bindKitReplaceFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract BarrackListingFragment bindBarrackListingFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract BarrackInspectionHomeFragment bindBIHomeFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract BarrackStrengthFragment bindBIStrengthFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract BarrackOthersFragment bindBIOthersFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract BarrackMetLandlordFragment bindBIMetLandlordFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract BarrackSpaceFragment bindBISpaceFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract IssueManagementFragment issueManagementFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract GrievanceIssueManagementFragment grievanceIssueManagementFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ComplaintIssueManagementFragment complaintIssueManagementFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ImprovementPlanIssueManagementFragment improvementPlanIssueManagementFragmentI();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ClientHandShakeAddComplaintFragment clientHandShakeAddComplaintFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ReviewInformationComplaintFragment bindReviewInformationComplaintFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RecruitmentFragment recruitmentFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PerformanceFragment performanceFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PerformanceResultsFragment performanceResultsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PerformanceEffortsFragment performanceEffortsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ManualSyncFragment bindManualSyncFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract CaptureImageFragmentV2 bindCaptureImageFragmentV2();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PreviewImageFragmentV2 bindPreviewImageFragmentV2();

    /*@ContributesAndroidInjector
    @FragmentScoped
    abstract UnitRaisingHomeFragment bindUnitRaisingHomeFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RaisingKittingFragment bindKittingFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RaisingClientMeetingFragment bindRaisingClientMeetingFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RaisingMTrainerFragment bindRaisingMTrainerFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RaisingTakeoverFragment bindRaisingOvertakeFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RaisingRecruitmentFragment bindRaisingRecruitFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract RaisingBarrackFragment bindRaisingBarrackFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PreInductionTrainingFragment bindPreInductionFragment();*/

    @ContributesAndroidInjector
    @FragmentScoped
    abstract TimeLineFragment bindTimeLineFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract TodayTimeLineFragment bindTodayDayTimeLineFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract YesterDayTimeLineFragment bindYesterDayTimeLineFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract MyKpiFragment bindMyKpiFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ConveyanceFragment bindConveyanceFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SiteTaskSummaryFragment bindSiteTaskSummaryFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ImprovementPlansFragment bindImprovementPlansFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PlansOfActionFragment bindPOAFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract ConveyanceMonthlyFragment bindConveyanceMonthlyFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SelfServiceFragment bindSelfServiceFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract IncentiveFragment bindIncentiveFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SalesReferenceFragment bindSalesRefFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract MaskDistributionFragment bindMaskDistFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SelfiePreviewFragmentV2 bindSelfiePreviewFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract EventsFragment bindEventsFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract DisbandmentFragment bindDisbandFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract NudgesDashboardFragment bindNudgesDashFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract CivilDefenceFragment bindCivilDefFragment();
}