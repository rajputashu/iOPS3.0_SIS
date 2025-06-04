package com.sisindia.ai.android.di.modules;

import com.droidcommons.dagger.bottomsheet.AndroidBottomSheetDialogInjectionModule;
import com.droidcommons.dagger.qualifier.BottomSheerScoped;
import com.droidcommons.dagger.qualifier.FragmentScoped;
import com.sisindia.ai.android.commons.CaptureSignatureBottomSheetFragment;
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingBottomSheetFragment;
import com.sisindia.ai.android.features.addkitrequest.SelectKitItemSizeBottomSheetFragment;
import com.sisindia.ai.android.features.addtask.SelectBarrackBottomSheetFragment;
import com.sisindia.ai.android.features.addtask.SelectReasonBottomSheetFragment;
import com.sisindia.ai.android.features.addtask.SelectSiteBottomSheetFragment;
import com.sisindia.ai.android.features.addtask.SelectSubTaskTypeBottomSheetFragment;
import com.sisindia.ai.android.features.akr.details.KitOtpBottomSheet;
import com.sisindia.ai.android.features.civil.AddCivilNominationBottomSheet;
import com.sisindia.ai.android.features.disband.AddDisbandmentBottomSheet;
import com.sisindia.ai.android.features.mask.AddMaskBottomSheet;
import com.sisindia.ai.android.features.practo.PractoQuestionsBottomSheet;
import com.sisindia.ai.android.features.recruitment.sheet.AddRecruitmentBottomSheet;
import com.sisindia.ai.android.features.sales.AddUpdateSalesReferenceBottomSheet;
import com.sisindia.ai.android.features.taskcheck.SiteCheckListBottomSheetFragment;
import com.sisindia.ai.android.features.taskcheck.SkipReasonBottomSheet;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.AddClientsBottomSheet;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.FeedbackOtpBottomSheet;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.NotMetReasonBottomSheet;
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckListBottomSheetFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardTurnOutEditBottomSheetFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.postlist.SitePostListBottomSheetFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine.RewardFineSelectBottomSheetFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = {AndroidBottomSheetDialogInjectionModule.class})
public abstract class BottomSheetDialogFragmentModule {

    @BottomSheerScoped
    @ContributesAndroidInjector
    abstract SitePostListBottomSheetFragment bindPrePostCheckBottomSheetFragment();

    @BottomSheerScoped
    @ContributesAndroidInjector
    abstract GuardTurnOutEditBottomSheetFragment bindGuardTurnOutEditBottomSheetFragment();

    @BottomSheerScoped
    @ContributesAndroidInjector
    abstract RewardFineSelectBottomSheetFragment bindAddRewardFineBottomSheetFragment();

    @BottomSheerScoped
    @ContributesAndroidInjector
    abstract AudioRecordingBottomSheetFragment bindAudioRecordingBottomSheetFragment();

    @BottomSheerScoped
    @ContributesAndroidInjector
    abstract SelectSiteBottomSheetFragment bindSelectSiteBottomSheetFragment();

    @BottomSheerScoped
    @ContributesAndroidInjector
    abstract SelectReasonBottomSheetFragment bindSelectReasonBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract NotMetReasonBottomSheet bindNotMetReasonFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddClientsBottomSheet bindAddClientFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract FeedbackOtpBottomSheet bindFeedbackOTPFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SelectKitItemSizeBottomSheetFragment bindSelectKitItemSizeBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract CaptureSignatureBottomSheetFragment bindCaptureSignatureBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SiteCheckListBottomSheetFragment bindSiteCheckListBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PostCheckListBottomSheetFragment bindPostCheckListBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddRecruitmentBottomSheet bindAddRecruitBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SelectBarrackBottomSheetFragment bindSelectBarrackBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SelectSubTaskTypeBottomSheetFragment bindSelectSubTaskTypeBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddUpdateSalesReferenceBottomSheet bindAddUpdateSalesBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddMaskBottomSheet bindAddMaskBottomSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddDisbandmentBottomSheet bindAddDisbandSheetFragment();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract KitOtpBottomSheet bindKitOtpSheet();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract PractoQuestionsBottomSheet bindPractoQuestionSheet();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract SkipReasonBottomSheet bindSkipReasonSheet();

    @ContributesAndroidInjector
    @FragmentScoped
    abstract AddCivilNominationBottomSheet bindNominationSheet();
}
