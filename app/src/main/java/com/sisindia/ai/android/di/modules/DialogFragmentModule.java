package com.sisindia.ai.android.di.modules;

import com.droidcommons.dagger.bottomsheet.AndroidBottomSheetDialogInjectionModule;
import com.droidcommons.dagger.qualifier.DialogScoped;
import com.sisindia.ai.android.features.addgrievances.AddedGrievanceDialogFragment;
import com.sisindia.ai.android.features.addkitrequest.AddedKitRequestDialogFragment;
import com.sisindia.ai.android.features.issues.complaints.AddedComplaintDialogFragment;
import com.sisindia.ai.android.features.issues.complaints.ClosedComplaintDialogFragment;
import com.sisindia.ai.android.features.issues.grievance.ClosedGrievanceDialogFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.GuardConfirmDialogFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = {AndroidBottomSheetDialogInjectionModule.class})
public abstract class DialogFragmentModule {

    @DialogScoped
    @ContributesAndroidInjector
    abstract AddedGrievanceDialogFragment bindAddedGrievanceDialogFragment();

    @DialogScoped
    @ContributesAndroidInjector
    abstract GuardConfirmDialogFragment bindGuardConfirmDialogFragment();

    @DialogScoped
    @ContributesAndroidInjector
    abstract AddedKitRequestDialogFragment bindAddedKitRequestDialogFragment();

    @DialogScoped
    @ContributesAndroidInjector
    abstract ClosedGrievanceDialogFragment bindClosedGrievanceDialogFragment();

    @DialogScoped
    @ContributesAndroidInjector
    abstract AddedComplaintDialogFragment bindAddedComplaintDialogFragment();

    @DialogScoped
    @ContributesAndroidInjector
    abstract ClosedComplaintDialogFragment bindClosedComplaintDialogFragment();

}
