package com.sisindia.ai.android.di.factories

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingViewModel
import com.sisindia.ai.android.di.components.ViewModelSubComponent
import com.sisindia.ai.android.features.addgrievances.AddGrievanceSelectGuardViewModel
import com.sisindia.ai.android.features.addgrievances.AddGrievancesViewModel
import com.sisindia.ai.android.features.addgrievances.AddedGrievanceViewModel
import com.sisindia.ai.android.features.addgrievances.GuardGrievanceDetailsViewModel
import com.sisindia.ai.android.features.addkitrequest.AddKitRequestViewModel
import com.sisindia.ai.android.features.addkitrequest.AddedKitRequestViewModel
import com.sisindia.ai.android.features.addtask.*
import com.sisindia.ai.android.features.ailocation.AILocationViewModel
import com.sisindia.ai.android.features.akr.AKRViewModel
import com.sisindia.ai.android.features.akr.details.KitAssignedDistributedViewModel
import com.sisindia.ai.android.features.akr.details.KitReplaceViewModel
import com.sisindia.ai.android.features.barracks.inspection.BarrackInspectionViewModel
import com.sisindia.ai.android.features.barracks.inspection.frags.*
import com.sisindia.ai.android.features.barracks.listing.BarrackListingViewModel
import com.sisindia.ai.android.features.barracks.listing.BarrackTaggingViewModel
import com.sisindia.ai.android.features.billcollection.BillCollectionViewModel
import com.sisindia.ai.android.features.billsubmit.BillSubmissionViewModel
import com.sisindia.ai.android.features.billsubmit.sheet.BillChecklistViewModel
//import com.sisindia.ai.android.features.chatbot.ChatBotViewModel
import com.sisindia.ai.android.features.clientcoordination.ClientCoordinationViewModel
import com.sisindia.ai.android.features.conveyance.ConveyanceViewModel
import com.sisindia.ai.android.features.dashboard.DashBoardViewModel
import com.sisindia.ai.android.features.dashboard.GenericDashboardViewModel
import com.sisindia.ai.android.features.disband.DisbandmentViewModel
import com.sisindia.ai.android.features.dynamictask.DynamicTaskViewModel
import com.sisindia.ai.android.features.imagecapture.CaptureImageViewModel
import com.sisindia.ai.android.features.improvementplans.CloseIPPoaViewModel
import com.sisindia.ai.android.features.improvementplans.ImprovePoaListViewModel
import com.sisindia.ai.android.features.improvementplans.ImprovementPlansViewModel
import com.sisindia.ai.android.features.issues.IssueManagementViewModel
import com.sisindia.ai.android.features.issues.complaints.ComplaintIssueManagementViewModel
import com.sisindia.ai.android.features.issues.complaints.ComplaintStatusViewModel
import com.sisindia.ai.android.features.issues.complaints.CreateComplaintIssueViewModel
import com.sisindia.ai.android.features.issues.complaints.IssueComplaintViewDetail
import com.sisindia.ai.android.features.issues.grievance.CreateGrievanceIssueViewModel
import com.sisindia.ai.android.features.issues.grievance.GrievanceIssueManagementViewModel
import com.sisindia.ai.android.features.issues.grievance.IssueGrievanceViewDetail
import com.sisindia.ai.android.features.issues.improvementplan.ImprovementPlanIssueManagementViewModel
import com.sisindia.ai.android.features.kpi.MyKpiViewModel
import com.sisindia.ai.android.features.livecamera.LiveImageCameraViewModel
import com.sisindia.ai.android.features.loadfactor.LoadFactorDWMViewModel
import com.sisindia.ai.android.features.loadfactor.LoadFactorViewModel
import com.sisindia.ai.android.features.login.number.LoginMobileNumberViewModel
import com.sisindia.ai.android.features.login.otp.EnterOtpViewModel
import com.sisindia.ai.android.features.mask.MaskDistributionViewModel
import com.sisindia.ai.android.features.moninput.MonInputViewModel
import com.sisindia.ai.android.features.nudges.NudgesDynamicViewModel
import com.sisindia.ai.android.features.nudges.NudgesViewModel
import com.sisindia.ai.android.features.onboard.OnBoardViewModel
import com.sisindia.ai.android.features.othertasks.OtherTaskViewModel
import com.sisindia.ai.android.features.performance.PerformanceViewModel
import com.sisindia.ai.android.features.performance.frags.PerformanceEffortsViewModel
import com.sisindia.ai.android.features.performance.frags.PerformanceResultsViewModel
import com.sisindia.ai.android.features.performance.frags.SiteTaskSummaryViewModel
import com.sisindia.ai.android.features.practo.PractoSheetViewModel
import com.sisindia.ai.android.features.practo.PractoTaskViewModel
import com.sisindia.ai.android.features.predashboard.EffortsViewModel
import com.sisindia.ai.android.features.predashboard.PreDashboardViewModel
import com.sisindia.ai.android.features.predashboard.ResultsViewModel
import com.sisindia.ai.android.features.recruitment.RecruitmentViewModel
import com.sisindia.ai.android.features.recruitment.sheet.AddRecruitmentViewModel
import com.sisindia.ai.android.features.register.DocumentCaptureViewModel
import com.sisindia.ai.android.features.register.RegisterCheckViewModel
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationDetailViewModel
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationViewModel
import com.sisindia.ai.android.features.rota.RotaViewModel
import com.sisindia.ai.android.features.rotacompliance.ComplianceDWMViewModel
import com.sisindia.ai.android.features.rotacompliance.RotaComplianceViewModel
import com.sisindia.ai.android.features.sales.SalesReferenceViewModel
import com.sisindia.ai.android.features.securityrisk.AddSecurityRiskViewModel
import com.sisindia.ai.android.features.selfie.SelfieViewModel
import com.sisindia.ai.android.features.selfservice.SelfServiceViewModel
import com.sisindia.ai.android.features.splash.SplashViewModel
import com.sisindia.ai.android.features.sync.ManualSyncViewModel
import com.sisindia.ai.android.features.taskcheck.DayNightCheckViewModel
import com.sisindia.ai.android.features.taskcheck.SiteCheckListViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientFeedbackViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientHandShakeAddComplaintViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.HandshakeFragViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.SummaryHandshakeViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.AddClientViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.FeedbackOtpSheetViewModel
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.NotMetReasonsViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckListViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.ScanGuardViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.available.GuardAvailableViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable.GuardNotAvailableViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardPhotoEvaluationResultViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardTurnOutViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.GuardCheckScanSuccessViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.PostGuardScanViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.postlist.SitePostListViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine.AddRewardFineViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine.GuardAddRewardFineViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.signature.AddSignatureViewModel
import com.sisindia.ai.android.features.taskcheck.postcheck.summary.GuardSummaryViewModel
import com.sisindia.ai.android.features.taskcheck.strengthcheck.StrengthCheckViewModel
import com.sisindia.ai.android.features.timline.TimeLineViewModel
import com.sisindia.ai.android.features.timline.TodayTimeLineViewModel
import com.sisindia.ai.android.features.timline.YesterDayTimeLineViewModel
import com.sisindia.ai.android.features.uar.UnitAtRiskViewModel
import com.sisindia.ai.android.features.uar.closepoa.ClosePOAViewModel
import com.sisindia.ai.android.features.uar.poa.POAViewModel
import com.sisindia.ai.android.features.units.DashBoardUnitsViewModel
import com.sisindia.ai.android.features.units.addedit.AddEditPostViewModel
import com.sisindia.ai.android.features.units.details.UnitDetailViewModel
import com.sisindia.ai.android.features.units.details.general.UnitGeneralViewModel
import com.sisindia.ai.android.features.units.details.posts.UnitPostsViewModel
import com.sisindia.ai.android.features.units.details.strength.UnitStrengthViewModel
import com.sisindia.ai.android.features.units.registermap.MapRegistersViewModel
import com.sisindia.ai.android.features.units.sheet.AddEquipmentViewModel
import com.sisindia.ai.android.features.webviews.EventsViewModel
import com.sisindia.ai.android.mlcore.QRScannerViewModel
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppViewModelFactory @Inject
constructor(viewModelSubComponent: ViewModelSubComponent) : ViewModelProvider.Factory {
    private val creators: ArrayMap<Class<*>, Callable<out ViewModel>> = ArrayMap()

    init {
        // View models cannot be injected directly because they won't be bound to the owner's view model scope.

        creators[OnBoardViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.onBoardViewModel() }

        creators[DashBoardViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.dashBoardViewModel() }

        creators[SplashViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.splashViewViewModel() }

        creators[LoginMobileNumberViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.loginMobileNumberViewModel() }

        creators[EnterOtpViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.enterOtpViewModel() }

        creators[RotaViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.dashBoardRotaViewModel() }

        creators[DashBoardUnitsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.dashBoardUnitsViewModel() }

        creators[PreDashboardViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.preDashboardViewModel() }

        creators[ResultsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.resultsViewModel() }

        creators[EffortsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.effortsViewModel() }

        creators[ReviewInformationViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.reviewInformationViewModel() }

        creators[DayNightCheckViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.dayCheckViewModel() }

        creators[SitePostListViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.prePostCheckViewModel() }

        creators[PostCheckViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.postCheckViewModel() }

        creators[ScanGuardViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.scanGuardViewModel() }

        creators[QRScannerViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.qRScannerViewModel() }

        creators[GuardNotAvailableViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.guardNotAvailableViewModel() }

        creators[PostGuardScanViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.postGuardScanViewModel() }

        creators[GuardCheckScanSuccessViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.guardCheckScanResultViewModel() }

        creators[GuardPhotoEvaluationResultViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmGuardPhotoEvaluationResultViewModel() }

        creators[AddTaskViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddTaskViewModel() }

        creators[GuardTurnOutViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmGuardTurnOutViewModel() }

        creators[GuardAddRewardFineViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmGuardDutyHistoryViewModel() }

        creators[AddSignatureViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddSignatureViewModel() }

        creators[RotaComplianceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmRotaComplianceViewModel() }

        creators[ComplianceDWMViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmRotaCompDWMViewModel() }

        creators[AddRewardFineViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddRewardFineViewModel() }

        creators[GuardSummaryViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddGuardSummaryViewModel() }


        creators[AddGrievancesViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddGrievancesViewModel() }

        creators[AddGrievanceSelectGuardViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddGrievancesGuardDetailsViewModel() }

        creators[GuardGrievanceDetailsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddGrievancesDetailsViewModel() }

        creators[RegisterCheckViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmRegisterCheckViewModel() }

        creators[LoadFactorViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmLoadFactorViewModel() }

        creators[LoadFactorDWMViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmLoadFactorDWMViewModel() }

        creators[OtherTaskViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmOtherTaskViewModel() }

        creators[BillSubmissionViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBSViewModel() }

        creators[BillCollectionViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBCViewModel() }

        creators[ClientCoordinationViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmCCRViewModel() }

        creators[AudioRecordingViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAudioRecordingViewModel() }

        creators[StrengthCheckViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmStrengthCheckViewModel() }

        creators[UnitDetailViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmUnitDetailViewModel() }

        creators[UnitGeneralViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmUnitGeneralViewModel() }

        creators[SelectSiteViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSelectSiteViewModel() }

        creators[SelectReasonViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSelectReasonViewModel() }

        creators[LiveImageCameraViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmLiveImageCameraViewModel() }

        creators[UnitStrengthViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmStrengthViewModel() }

        creators[UnitPostsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPostsViewModel() }

        creators[AddEditPostViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddEditViewModel() }

        creators[AddEquipmentViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddEquipmentViewModel() }

        creators[BillChecklistViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBillCheckListViewModel() }

        creators[AILocationViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAILocationViewModel() }

        creators[UnitAtRiskViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmUARViewModel() }

        creators[POAViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPOAViewModel() }

        creators[GuardAvailableViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmScanGuardQrViewModel() }

        creators[ClosePOAViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmClosePOAViewModel() }

        creators[AddedGrievanceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddedGrievanceViewModel() }

        creators[ReviewInformationDetailViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmReviewInformationDetailViewModel() }

        creators[AddSecurityRiskViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddSecurityRiskViewModel() }

        creators[ClientHandshakeViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmHandshakeViewModel() }

        creators[HandshakeFragViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmHandshakeFragViewModel() }

        creators[NotMetReasonsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmReasonViewModel() }

        creators[AddClientViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddClientViewModel() }

        creators[ClientFeedbackViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmClientFeedbackModel() }

        creators[FeedbackOtpSheetViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmFeedbackOTPModel() }

        creators[SummaryHandshakeViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSummaryHandshake() }


        creators[AddKitRequestViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddKitRequestViewModel() }

        creators[AddedKitRequestViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddedKitRequestViewModel() }

        creators[AKRViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAKRViewModel() }

        creators[KitAssignedDistributedViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAssignDistributedModel() }

        creators[KitReplaceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmKitReplaceViewModel() }

        creators[BarrackListingViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBarrackListingViewModel() }

        creators[BarrackInspectionViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBarrackInspectionViewModel() }

        creators[BarrackInspectionHomeViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBIHomeViewModel() }

        creators[BarrackStrengthViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBIStrengthViewModel() }

        creators[BarrackOthersViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBIOthersViewModel() }

        creators[BarrackMetLandlordViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBILandlordViewModel() }

        creators[BarrackSpaceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBISpaceViewModel() }

        creators[BarrackTaggingViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmBarrackTaggingViewModel() }

        creators[SiteCheckListViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSiteCheckListViewModel() }

        creators[PostCheckListViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPostCheckListViewModel() }

        creators[DocumentCaptureViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmDocumentCaptureViewModel() }

        creators[IssueManagementViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmIssueManagementViewModel() }

        creators[ComplaintIssueManagementViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmComplaintIssueManagementViewModel() }

        creators[GrievanceIssueManagementViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmGrievanceIssueManagementViewModel() }

        creators[ImprovementPlanIssueManagementViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmImprovementPlanIssueManagementViewModel() }

        creators[CreateGrievanceIssueViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmCreateGrievanceIssueViewModel() }

        creators[IssueGrievanceViewDetail::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmIssueGrievanceViewDetail() }

        creators[ClientHandShakeAddComplaintViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmClientHandShakeAddComplaint() }

        creators[CreateComplaintIssueViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmCreateComplaintIssueViewModel() }

        creators[IssueComplaintViewDetail::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmIssueComplaintViewDetail() }

        creators[ComplaintStatusViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmComplaintStatusViewModel() }

        creators[RecruitmentViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmRecruitment() }

        creators[AddRecruitmentViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmAddRecruitment() }

        creators[PerformanceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPerformance() }

        creators[PerformanceResultsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPerformanceResults() }

        creators[ManualSyncViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmManualSyncViewModel() }

        creators[CaptureImageViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmCaptureImageViewModel() }

        creators[MonInputViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmMonInputViewModel() }

        /*creators[UnitRaisingHomeViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmUnitRaisingHomeViewModel() }

        creators[UnitRaisingOptionsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmRaisingOptionsViewModel() }

        creators[UnitRaisingInsideOptionsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmRaisingInsideOptionViewModel() }*/

        creators[SelectTaskTypeViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSelectTaskTypeViewModel() }

        creators[CreateTaskViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmCreateTaskViewModel() }

        creators[SelectBarrackViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSelectBarrackViewModel() }

        creators[SelectSubTaskTypeViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSelectSubTaskTypeViewModel() }

        creators[TimeLineViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmTimeLineViewModel() }

        creators[YesterDayTimeLineViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmYesterDayTimeLineViewModel() }

        creators[TodayTimeLineViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmTodayTimeLineViewModel() }

        creators[PerformanceEffortsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPerformanceEffortsViewModel() }

        creators[MyKpiViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmMyKpiViewModel() }

        creators[ConveyanceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmConveyanceViewModel() }

        creators[MapRegistersViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmMapRegistersViewModel() }

        creators[SiteTaskSummaryViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSiteTaskSummaryViewModel() }

        creators[SelfieViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSelfieViewModel() }

        creators[ImprovementPlansViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmImprovementPlansViewModel() }

        creators[ImprovePoaListViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmIPListViewModel() }

        creators[CloseIPPoaViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmCloseIPViewModel() }

        creators[SelfServiceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSelfServiceViewModel() }

        /*creators[ChatBotViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmChatBotViewModel() }*/

        creators[SalesReferenceViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmSalesRefViewModel() }

        creators[DynamicTaskViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmDynamicViewModel() }

        creators[GenericDashboardViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmGenDashViewModel() }

        creators[MaskDistributionViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmMaskViewModel() }

        /*creators[VProtectAlarmViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmVProtectViewModel() }*/

        creators[EventsViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmEventsViewModel() }

        creators[DisbandmentViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmDisbandment() }

        creators[PractoTaskViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPractoTask() }

        creators[PractoSheetViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmPractoSheetTask() }

        creators[NudgesViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmNudges() }

        creators[NudgesDynamicViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.vmDynamicNudges() }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown model. class $modelClass")
        }
        try {
            return creator.call() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}