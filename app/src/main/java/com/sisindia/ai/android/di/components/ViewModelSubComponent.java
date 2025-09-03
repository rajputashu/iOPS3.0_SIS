package com.sisindia.ai.android.di.components;

import com.sisindia.ai.android.commons.audiorecord.AudioRecordingViewModel;
import com.sisindia.ai.android.features.addgrievances.AddGrievanceSelectGuardViewModel;
import com.sisindia.ai.android.features.addgrievances.AddGrievancesViewModel;
import com.sisindia.ai.android.features.addgrievances.AddedGrievanceViewModel;
import com.sisindia.ai.android.features.addgrievances.GuardGrievanceDetailsViewModel;
import com.sisindia.ai.android.features.addkitrequest.AddKitRequestViewModel;
import com.sisindia.ai.android.features.addkitrequest.AddedKitRequestViewModel;
import com.sisindia.ai.android.features.addtask.AddTaskViewModel;
import com.sisindia.ai.android.features.addtask.CreateTaskViewModel;
import com.sisindia.ai.android.features.addtask.SelectBarrackViewModel;
import com.sisindia.ai.android.features.addtask.SelectReasonViewModel;
import com.sisindia.ai.android.features.addtask.SelectSiteViewModel;
import com.sisindia.ai.android.features.addtask.SelectSubTaskTypeViewModel;
import com.sisindia.ai.android.features.addtask.SelectTaskTypeViewModel;
import com.sisindia.ai.android.features.ailocation.AILocationViewModel;
import com.sisindia.ai.android.features.akr.AKRViewModel;
import com.sisindia.ai.android.features.akr.details.KitAssignedDistributedViewModel;
import com.sisindia.ai.android.features.akr.details.KitReplaceViewModel;
import com.sisindia.ai.android.features.barracks.inspection.BarrackInspectionViewModel;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackInspectionHomeViewModel;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackMetLandlordViewModel;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackOthersViewModel;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackSpaceViewModel;
import com.sisindia.ai.android.features.barracks.inspection.frags.BarrackStrengthViewModel;
import com.sisindia.ai.android.features.barracks.listing.BarrackListingViewModel;
import com.sisindia.ai.android.features.barracks.listing.BarrackTaggingViewModel;
import com.sisindia.ai.android.features.billcollection.BillCollectionViewModel;
import com.sisindia.ai.android.features.billsubmit.BillSubmissionViewModel;
import com.sisindia.ai.android.features.billsubmit.sheet.BillChecklistViewModel;
import com.sisindia.ai.android.features.civil.CivilDefenceViewModel;
import com.sisindia.ai.android.features.clientcoordination.ClientCoordinationViewModel;
import com.sisindia.ai.android.features.conveyance.ConveyanceViewModel;
import com.sisindia.ai.android.features.dashboard.DashBoardViewModel;
import com.sisindia.ai.android.features.dashboard.GenericDashboardViewModel;
import com.sisindia.ai.android.features.disband.DisbandmentViewModel;
import com.sisindia.ai.android.features.dynamictask.DynamicTaskViewModel;
import com.sisindia.ai.android.features.imagecapture.CaptureImageViewModel;
import com.sisindia.ai.android.features.improvementplans.CloseIPPoaViewModel;
import com.sisindia.ai.android.features.improvementplans.ImprovePoaListViewModel;
import com.sisindia.ai.android.features.improvementplans.ImprovementPlansViewModel;
import com.sisindia.ai.android.features.issues.IssueManagementViewModel;
import com.sisindia.ai.android.features.issues.complaints.ComplaintIssueManagementViewModel;
import com.sisindia.ai.android.features.issues.complaints.ComplaintStatusViewModel;
import com.sisindia.ai.android.features.issues.complaints.CreateComplaintIssueViewModel;
import com.sisindia.ai.android.features.issues.complaints.IssueComplaintViewDetail;
import com.sisindia.ai.android.features.issues.grievance.CreateGrievanceIssueViewModel;
import com.sisindia.ai.android.features.issues.grievance.GrievanceIssueManagementViewModel;
import com.sisindia.ai.android.features.issues.grievance.IssueGrievanceViewDetail;
import com.sisindia.ai.android.features.issues.improvementplan.ImprovementPlanIssueManagementViewModel;
import com.sisindia.ai.android.features.kpi.MyKpiViewModel;
import com.sisindia.ai.android.features.livecamera.LiveImageCameraViewModel;
import com.sisindia.ai.android.features.loadfactor.LoadFactorDWMViewModel;
import com.sisindia.ai.android.features.loadfactor.LoadFactorViewModel;
import com.sisindia.ai.android.features.login.number.LoginMobileNumberViewModel;
import com.sisindia.ai.android.features.login.otp.EnterOtpViewModel;
import com.sisindia.ai.android.features.mask.MaskDistributionViewModel;
import com.sisindia.ai.android.features.moninput.MonInputViewModel;
import com.sisindia.ai.android.features.nudges.NudgesDynamicViewModel;
import com.sisindia.ai.android.features.nudges.NudgesViewModel;
import com.sisindia.ai.android.features.onboard.OnBoardViewModel;
import com.sisindia.ai.android.features.othertasks.OtherTaskViewModel;
import com.sisindia.ai.android.features.performance.PerformanceViewModel;
import com.sisindia.ai.android.features.performance.frags.PerformanceEffortsViewModel;
import com.sisindia.ai.android.features.performance.frags.PerformanceResultsViewModel;
import com.sisindia.ai.android.features.performance.frags.SiteTaskSummaryViewModel;
import com.sisindia.ai.android.features.practo.PractoSheetViewModel;
import com.sisindia.ai.android.features.practo.PractoTaskViewModel;
import com.sisindia.ai.android.features.predashboard.EffortsViewModel;
import com.sisindia.ai.android.features.predashboard.PreDashboardViewModel;
import com.sisindia.ai.android.features.predashboard.ResultsViewModel;
import com.sisindia.ai.android.features.recruitment.RecruitmentViewModel;
import com.sisindia.ai.android.features.recruitment.sheet.AddRecruitmentViewModel;
import com.sisindia.ai.android.features.register.DocumentCaptureViewModel;
import com.sisindia.ai.android.features.register.RegisterCheckViewModel;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationDetailViewModel;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationViewModel;
import com.sisindia.ai.android.features.rota.RotaViewModel;
import com.sisindia.ai.android.features.rotacompliance.ComplianceDWMViewModel;
import com.sisindia.ai.android.features.rotacompliance.RotaComplianceViewModel;
import com.sisindia.ai.android.features.sales.SalesReferenceViewModel;
import com.sisindia.ai.android.features.securityrisk.AddSecurityRiskViewModel;
import com.sisindia.ai.android.features.selfie.SelfieViewModel;
import com.sisindia.ai.android.features.selfservice.SelfServiceViewModel;
import com.sisindia.ai.android.features.splash.SplashViewModel;
import com.sisindia.ai.android.features.sync.ManualSyncViewModel;
import com.sisindia.ai.android.features.taskcheck.DayNightCheckViewModel;
import com.sisindia.ai.android.features.taskcheck.SiteCheckListViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientFeedbackViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientHandShakeAddComplaintViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.HandshakeFragViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.SummaryHandshakeViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.AddClientViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.FeedbackOtpSheetViewModel;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.NotMetReasonsViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckListViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.ScanGuardViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.available.GuardAvailableViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable.GuardNotAvailableViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardPhotoEvaluationResultViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardTurnOutViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.GuardCheckScanSuccessViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.PostGuardScanViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.postlist.SitePostListViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine.AddRewardFineViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine.GuardAddRewardFineViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.signature.AddSignatureViewModel;
import com.sisindia.ai.android.features.taskcheck.postcheck.summary.GuardSummaryViewModel;
import com.sisindia.ai.android.features.taskcheck.strengthcheck.StrengthCheckViewModel;
import com.sisindia.ai.android.features.timline.TimeLineViewModel;
import com.sisindia.ai.android.features.timline.TodayTimeLineViewModel;
import com.sisindia.ai.android.features.timline.YesterDayTimeLineViewModel;
import com.sisindia.ai.android.features.uar.UnitAtRiskViewModel;
import com.sisindia.ai.android.features.uar.add.AddPoaAndIpViewModel;
import com.sisindia.ai.android.features.uar.closepoa.ClosePOAViewModel;
import com.sisindia.ai.android.features.uar.poa.POAViewModel;
import com.sisindia.ai.android.features.units.DashBoardUnitsViewModel;
import com.sisindia.ai.android.features.units.addedit.AddEditPostViewModel;
import com.sisindia.ai.android.features.units.details.UnitDetailViewModel;
import com.sisindia.ai.android.features.units.details.general.UnitGeneralViewModel;
import com.sisindia.ai.android.features.units.details.posts.UnitPostsViewModel;
import com.sisindia.ai.android.features.units.details.strength.UnitStrengthViewModel;
import com.sisindia.ai.android.features.units.registermap.MapRegistersViewModel;
import com.sisindia.ai.android.features.units.sheet.AddEquipmentViewModel;
import com.sisindia.ai.android.features.webviews.EventsViewModel;
import com.sisindia.ai.android.mlcore.QRScannerViewModel;

import org.jetbrains.annotations.NotNull;

import dagger.Subcomponent;

//import com.sisindia.ai.android.features.chatbot.ChatBotViewModel;
;

@Subcomponent
public interface ViewModelSubComponent {

    @NotNull DashBoardViewModel dashBoardViewModel();

    @NotNull OnBoardViewModel onBoardViewModel();

    @NotNull SplashViewModel splashViewViewModel();

    @NotNull LoginMobileNumberViewModel loginMobileNumberViewModel();

    @NotNull EnterOtpViewModel enterOtpViewModel();

    @NotNull RotaViewModel dashBoardRotaViewModel();

    @NotNull DashBoardUnitsViewModel dashBoardUnitsViewModel();

    @NotNull PreDashboardViewModel preDashboardViewModel();

    @NotNull EffortsViewModel effortsViewModel();

    @NotNull ResultsViewModel resultsViewModel();

    @NotNull ReviewInformationViewModel reviewInformationViewModel();

    @NotNull DayNightCheckViewModel dayCheckViewModel();

    @NotNull SitePostListViewModel prePostCheckViewModel();

    @NotNull PostCheckViewModel postCheckViewModel();

    @NotNull ScanGuardViewModel scanGuardViewModel();

    @NotNull QRScannerViewModel qRScannerViewModel();

    @NotNull GuardNotAvailableViewModel guardNotAvailableViewModel();

    @NotNull PostGuardScanViewModel postGuardScanViewModel();

    @NotNull GuardCheckScanSuccessViewModel guardCheckScanResultViewModel();

    @NotNull GuardPhotoEvaluationResultViewModel vmGuardPhotoEvaluationResultViewModel();

    @NotNull LiveImageCameraViewModel vmLiveImageCameraViewModel();

    @NotNull AddTaskViewModel vmAddTaskViewModel();

    @NotNull GuardTurnOutViewModel vmGuardTurnOutViewModel();

    @NotNull GuardAddRewardFineViewModel vmGuardDutyHistoryViewModel();

    @NotNull AddSignatureViewModel vmAddSignatureViewModel();

    @NotNull RotaComplianceViewModel vmRotaComplianceViewModel();

    @NotNull ComplianceDWMViewModel vmRotaCompDWMViewModel();

    @NotNull AddRewardFineViewModel vmAddRewardFineViewModel();

    @NotNull GuardSummaryViewModel vmAddGuardSummaryViewModel();

    @NotNull LoadFactorViewModel vmLoadFactorViewModel();

    @NotNull LoadFactorDWMViewModel vmLoadFactorDWMViewModel();

    @NotNull OtherTaskViewModel vmOtherTaskViewModel();

    @NotNull BillSubmissionViewModel vmBSViewModel();

    @NotNull AddGrievancesViewModel vmAddGrievancesViewModel();

    @NotNull AddGrievanceSelectGuardViewModel vmAddGrievancesGuardDetailsViewModel();

    @NotNull GuardGrievanceDetailsViewModel vmAddGrievancesDetailsViewModel();

    @NotNull RegisterCheckViewModel vmRegisterCheckViewModel();

    @NotNull BillCollectionViewModel vmBCViewModel();

    @NotNull ClientCoordinationViewModel vmCCRViewModel();

    @NotNull AudioRecordingViewModel vmAudioRecordingViewModel();

    @NotNull StrengthCheckViewModel vmStrengthCheckViewModel();

    @NotNull UnitDetailViewModel vmUnitDetailViewModel();

    @NotNull UnitGeneralViewModel vmUnitGeneralViewModel();

    @NotNull SelectSiteViewModel vmSelectSiteViewModel();

    @NotNull SelectReasonViewModel vmSelectReasonViewModel();

    @NotNull UnitStrengthViewModel vmStrengthViewModel();

    @NotNull UnitPostsViewModel vmPostsViewModel();

    @NotNull AddEditPostViewModel vmAddEditViewModel();

    @NotNull AddEquipmentViewModel vmAddEquipmentViewModel();

    @NotNull AILocationViewModel vmAILocationViewModel();

    @NotNull BillChecklistViewModel vmBillCheckListViewModel();

    @NotNull UnitAtRiskViewModel vmUARViewModel();

    @NotNull POAViewModel vmPOAViewModel();

    @NotNull GuardAvailableViewModel vmScanGuardQrViewModel();

    @NotNull ClosePOAViewModel vmClosePOAViewModel();

    @NotNull AddedGrievanceViewModel vmAddedGrievanceViewModel();

    @NotNull ReviewInformationDetailViewModel vmReviewInformationDetailViewModel();

    @NotNull AddSecurityRiskViewModel vmAddSecurityRiskViewModel();

    @NotNull ClientHandshakeViewModel vmHandshakeViewModel();

    @NotNull HandshakeFragViewModel vmHandshakeFragViewModel();

    @NotNull NotMetReasonsViewModel vmReasonViewModel();

    @NotNull AddClientViewModel vmAddClientViewModel();

    @NotNull ClientFeedbackViewModel vmClientFeedbackModel();

    @NotNull FeedbackOtpSheetViewModel vmFeedbackOTPModel();

    @NotNull SummaryHandshakeViewModel vmSummaryHandshake();

    @NotNull AddKitRequestViewModel vmAddKitRequestViewModel();

    @NotNull AddedKitRequestViewModel vmAddedKitRequestViewModel();

    @NotNull AKRViewModel vmAKRViewModel();

    @NotNull KitAssignedDistributedViewModel vmAssignDistributedModel();

    @NotNull KitReplaceViewModel vmKitReplaceViewModel();

    @NotNull BarrackListingViewModel vmBarrackListingViewModel();

    @NotNull BarrackInspectionViewModel vmBarrackInspectionViewModel();

    @NotNull BarrackInspectionHomeViewModel vmBIHomeViewModel();

    @NotNull BarrackStrengthViewModel vmBIStrengthViewModel();

    @NotNull BarrackOthersViewModel vmBIOthersViewModel();

    @NotNull BarrackMetLandlordViewModel vmBILandlordViewModel();

    @NotNull BarrackSpaceViewModel vmBISpaceViewModel();

    @NotNull BarrackTaggingViewModel vmBarrackTaggingViewModel();

    @NotNull SiteCheckListViewModel vmSiteCheckListViewModel();

    @NotNull PostCheckListViewModel vmPostCheckListViewModel();

    @NotNull DocumentCaptureViewModel vmDocumentCaptureViewModel();

    @NotNull IssueManagementViewModel vmIssueManagementViewModel();

    @NotNull ComplaintIssueManagementViewModel vmComplaintIssueManagementViewModel();

    @NotNull GrievanceIssueManagementViewModel vmGrievanceIssueManagementViewModel();

    @NotNull ImprovementPlanIssueManagementViewModel vmImprovementPlanIssueManagementViewModel();

    @NotNull CreateGrievanceIssueViewModel vmCreateGrievanceIssueViewModel();

    @NotNull IssueGrievanceViewDetail vmIssueGrievanceViewDetail();

    @NotNull ClientHandShakeAddComplaintViewModel vmClientHandShakeAddComplaint();

    @NotNull CreateComplaintIssueViewModel vmCreateComplaintIssueViewModel();

    @NotNull IssueComplaintViewDetail vmIssueComplaintViewDetail();

    @NotNull ComplaintStatusViewModel vmComplaintStatusViewModel();

    @NotNull RecruitmentViewModel vmRecruitment();

    @NotNull AddRecruitmentViewModel vmAddRecruitment();

    @NotNull PerformanceViewModel vmPerformance();

    @NotNull PerformanceResultsViewModel vmPerformanceResults();

    @NotNull ManualSyncViewModel vmManualSyncViewModel();

    @NotNull CaptureImageViewModel vmCaptureImageViewModel();

    @NotNull MonInputViewModel vmMonInputViewModel();

    /*@NotNull UnitRaisingHomeViewModel vmUnitRaisingHomeViewModel();

    @NotNull UnitRaisingOptionsViewModel vmRaisingOptionsViewModel();

    @NotNull UnitRaisingInsideOptionsViewModel vmRaisingInsideOptionViewModel();*/

    @NotNull SelectTaskTypeViewModel vmSelectTaskTypeViewModel();

    @NotNull CreateTaskViewModel vmCreateTaskViewModel();

    @NotNull SelectBarrackViewModel vmSelectBarrackViewModel();

    @NotNull SelectSubTaskTypeViewModel vmSelectSubTaskTypeViewModel();

    @NotNull TimeLineViewModel vmTimeLineViewModel();

    @NotNull TodayTimeLineViewModel vmTodayTimeLineViewModel();

    @NotNull YesterDayTimeLineViewModel vmYesterDayTimeLineViewModel();

    @NotNull PerformanceEffortsViewModel vmPerformanceEffortsViewModel();

    @NotNull MyKpiViewModel vmMyKpiViewModel();

    @NotNull ConveyanceViewModel vmConveyanceViewModel();

    @NotNull MapRegistersViewModel vmMapRegistersViewModel();

    @NotNull SiteTaskSummaryViewModel vmSiteTaskSummaryViewModel();

    @NotNull SelfieViewModel vmSelfieViewModel();

    @NotNull ImprovementPlansViewModel vmImprovementPlansViewModel();

    @NotNull ImprovePoaListViewModel vmIPListViewModel();

    @NotNull CloseIPPoaViewModel vmCloseIPViewModel();

    @NotNull SelfServiceViewModel vmSelfServiceViewModel();

//    @NotNull ChatBotViewModel vmChatBotViewModel();

    @NotNull SalesReferenceViewModel vmSalesRefViewModel();

    @NotNull DynamicTaskViewModel vmDynamicViewModel();

    @NotNull GenericDashboardViewModel vmGenDashViewModel();

    @NotNull MaskDistributionViewModel vmMaskViewModel();

//    @NotNull VProtectAlarmViewModel vmVProtectViewModel();

    @NotNull EventsViewModel vmEventsViewModel();

    @NotNull DisbandmentViewModel vmDisbandment();

    @NotNull PractoTaskViewModel vmPractoTask();

    @NotNull PractoSheetViewModel vmPractoSheetTask();

    @NotNull NudgesViewModel vmNudges();

    @NotNull NudgesDynamicViewModel vmDynamicNudges();

    @NotNull CivilDefenceViewModel vmCivilDef();

    @NotNull AddPoaAndIpViewModel vmAddPoaIP();

    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }
}
