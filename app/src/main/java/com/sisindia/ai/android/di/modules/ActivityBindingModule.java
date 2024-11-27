package com.sisindia.ai.android.di.modules;

import android.app.Application;
import android.content.Context;

import com.droidcommons.dagger.qualifier.ActivityScoped;
import com.sisindia.ai.android.IopsApplication;
import com.sisindia.ai.android.features.addgrievances.AddGrievancesActivity;
import com.sisindia.ai.android.features.addkitrequest.AddKitRequestActivity;
import com.sisindia.ai.android.features.addtask.AddTaskActivity;
import com.sisindia.ai.android.features.akr.details.AKRDetailsActivity;
import com.sisindia.ai.android.features.barracks.inspection.BarrackInspectionActivity;
import com.sisindia.ai.android.features.barracks.listing.BarrackTaggingActivity;
import com.sisindia.ai.android.features.billcollection.BillCollectionActivity;
import com.sisindia.ai.android.features.billcollection.BillCollectionDetailsOnSites;
import com.sisindia.ai.android.features.billsubmit.BillSubmissionActivity;
import com.sisindia.ai.android.features.billsubmit.BillSubmissionCardsActivity;
import com.sisindia.ai.android.features.clientcoordination.ClientCoordinationActivity;
import com.sisindia.ai.android.features.conveyance.ConveyanceActivity;
import com.sisindia.ai.android.features.dashboard.DashBoardActivity;
import com.sisindia.ai.android.features.dashboard.GenericDashboardActivity;
import com.sisindia.ai.android.features.dynamictask.DynamicTaskActivity;
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2;
import com.sisindia.ai.android.features.improvementplans.CloseImprovePlansActivity;
import com.sisindia.ai.android.features.improvementplans.ImprovementPoaListActivity;
import com.sisindia.ai.android.features.issues.complaints.CreateComplaintIssueActivity;
import com.sisindia.ai.android.features.issues.complaints.IssueComplaintDetailActivity;
import com.sisindia.ai.android.features.issues.grievance.CreateGrievanceIssueActivity;
import com.sisindia.ai.android.features.issues.grievance.IssueGrievanceDetailActivity;
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity;
import com.sisindia.ai.android.features.loadfactor.LoadFactorActivity;
import com.sisindia.ai.android.features.location.UserLocationActivity;
import com.sisindia.ai.android.features.moninput.MonInputCardsActivity;
import com.sisindia.ai.android.features.nudges.NudgesDynamicActivity;
import com.sisindia.ai.android.features.onboard.IntroActivity;
import com.sisindia.ai.android.features.onboard.OnBoardActivity;
import com.sisindia.ai.android.features.othertasks.OtherTaskActivity;
import com.sisindia.ai.android.features.practo.PractoTaskActivity;
import com.sisindia.ai.android.features.register.DocumentCaptureActivity;
import com.sisindia.ai.android.features.register.RegisterCheckActivity;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationActivity;
import com.sisindia.ai.android.features.rotacompliance.RotaComplianceActivity;
import com.sisindia.ai.android.features.rotacompliance.RotaComplianceGraphActivity;
import com.sisindia.ai.android.features.securityrisk.AddSecurityRiskActivity;
import com.sisindia.ai.android.features.selfie.DutyOnOffSelfie;
import com.sisindia.ai.android.features.taskcheck.DayNightCheckActivity;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.ScanGuardActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.PostGuardScanActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.summary.GuardSummaryActivity;
import com.sisindia.ai.android.features.taskcheck.strengthcheck.StrengthCheckActivity;
import com.sisindia.ai.android.features.uar.closepoa.ClosePOAActivity;
import com.sisindia.ai.android.features.uar.poa.POAActivity;
import com.sisindia.ai.android.features.units.addedit.AddEditPostActivity;
import com.sisindia.ai.android.features.units.details.UnitDetailActivity;
import com.sisindia.ai.android.features.units.registermap.MapRegistersActivity;
import com.sisindia.ai.android.mlcore.ScanQRActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @Binds
    abstract Context bindContext(IopsApplication application);

    @ActivityScoped
    @ContributesAndroidInjector
    abstract OnBoardActivity bindOnBoardActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract DashBoardActivity bindDashBoardActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract GenericDashboardActivity bindGenDashBoardActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReviewInformationActivity bindReviewInformationActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract DayNightCheckActivity bindDayCheckActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract PostCheckActivity bindPostCheckActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ScanGuardActivity bindScanGuardActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract PostGuardScanActivity bindPostGuardScanActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ImageCaptureActivity bindBarcodeActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AddTaskActivity bindAddTaskActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RotaComplianceGraphActivity bindRotaComplianceGraphActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract GuardSummaryActivity bindGuardSummaryActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AddGrievancesActivity bindAddGrievancesActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RegisterCheckActivity bindRegisterCheckActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract LoadFactorActivity bindLoadFactorActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract OtherTaskActivity bindOtherTaskActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract BillSubmissionActivity bindBSTaskActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract BillCollectionActivity bindBillCollectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ClientCoordinationActivity bindCCRActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract StrengthCheckActivity bindStrengthCheckActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract UnitDetailActivity bindUnitDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AddEditPostActivity bindAddEditActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract UserLocationActivity bindUserLocationActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract POAActivity bindPOAActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ClosePOAActivity bindClosePOAActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ClientHandshakeActivity bindHandShakeActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AddSecurityRiskActivity bindAddSecurityRiskActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ScanQRActivity bindScanQRActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AddKitRequestActivity bindAddKitRequestActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AKRDetailsActivity bindAKRDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract BarrackInspectionActivity bindBarrackInspectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract BarrackTaggingActivity bindBarrackTaggingActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract DocumentCaptureActivity bindDocumentCaptureActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CreateGrievanceIssueActivity bindCreateGrievanceIssueActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract IssueGrievanceDetailActivity bindIssueGrievanceDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CreateComplaintIssueActivity bindCreateComplaintIssueActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract IssueComplaintDetailActivity bindIssueComplaintDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract BillCollectionDetailsOnSites bindCollectionAtSiteActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CaptureImageActivityV2 bindCaptureImageActivityV2();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RotaComplianceActivity bindRotaComplianceActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MonInputCardsActivity bindMonInputActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract BillSubmissionCardsActivity bindBillSubCardsActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MapRegistersActivity bindMapRegistersActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract IntroActivity bindIntroActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract DutyOnOffSelfie bindSelfieActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ImprovementPoaListActivity bindIPListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CloseImprovePlansActivity bindCloseIPActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ConveyanceActivity bindConveyanceActivity();

    /*@ActivityScoped
    @ContributesAndroidInjector
    abstract ChatBotActivity bindChatBotActivity();*/

    @ActivityScoped
    @ContributesAndroidInjector
    abstract DynamicTaskActivity bindDynamicActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract PractoTaskActivity bindPractoActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract NudgesDynamicActivity bindNudgesDynamic();

    @Binds
    abstract Application bindApplication(IopsApplication application);
}
