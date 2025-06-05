package com.sisindia.ai.android.di.modules;

import com.droidcommons.dagger.qualifier.WorkerScoped;
import com.droidcommons.dagger.worker.AndroidWorkerInjectionModule;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.workers.AIUpdateProfileWorker;
import com.sisindia.ai.android.workers.AIUserProfileWorker;
import com.sisindia.ai.android.workers.AddRecruitmentWorker;
import com.sisindia.ai.android.workers.AddRewardFineWorker;
import com.sisindia.ai.android.workers.AtRiskPoaWorker;
import com.sisindia.ai.android.workers.AttachmentsUploadWorker;
import com.sisindia.ai.android.workers.AzureFileStorageWorker;
import com.sisindia.ai.android.workers.ClosedImprovePlanWorker;
import com.sisindia.ai.android.workers.CommonMasterDataWorker;
import com.sisindia.ai.android.workers.ComplaintWorker;
import com.sisindia.ai.android.workers.DutyStatusWorker;
import com.sisindia.ai.android.workers.EmployeesRewardFineWorker;
import com.sisindia.ai.android.workers.GrievanceWorker;
import com.sisindia.ai.android.workers.KitDistributionWorker;
import com.sisindia.ai.android.workers.KitRequestWorker;
import com.sisindia.ai.android.workers.LocationWatcherWorker;
import com.sisindia.ai.android.workers.LocationWorker;
import com.sisindia.ai.android.workers.RotaTaskWorker;
import com.sisindia.ai.android.workers.SiteConfigurationWorker;
import com.sisindia.ai.android.workers.SitePostsWorker;
import com.sisindia.ai.android.workers.StateDistrictWorker;
import com.sisindia.ai.android.workers.SyncPoaWorker;
import com.sisindia.ai.android.workers.UpdateBillCollectionWorker;
import com.sisindia.ai.android.workers.UserMasterDataWorkerV2;
import com.sisindia.ai.android.workers.WeekRotaTaskWorker;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = {AndroidWorkerInjectionModule.class})
public abstract class WorkerModule {

    @WorkerScoped
    @ContributesAndroidInjector
    abstract LocationWorker testWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract BaseWorker baseWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract AzureFileStorageWorker storageWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract CommonMasterDataWorker commonMasterDataWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract AttachmentsUploadWorker attachmentUploadWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract RotaTaskWorker rotaTaskWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract SiteConfigurationWorker checkListWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract AtRiskPoaWorker atRiskPoaWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract SitePostsWorker sitePostWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract DutyStatusWorker dutyStatusWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract AddRewardFineWorker addRewardFineWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract GrievanceWorker addGrievanceWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract KitRequestWorker kitRequestWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract ComplaintWorker addComplaintWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract AIUserProfileWorker aiUserProfileWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract AIUpdateProfileWorker aiUpdateProfileWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract AddRecruitmentWorker addRecruitmentWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract UpdateBillCollectionWorker billCollectionWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract UserMasterDataWorkerV2 userMasterDataWorkerV2();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract LocationWatcherWorker locWatcherWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract KitDistributionWorker kitDistributionWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract ClosedImprovePlanWorker closeIPWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract WeekRotaTaskWorker weekRotaTaskWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract SyncPoaWorker syncPoaWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract EmployeesRewardFineWorker empRewardFineWorker();

    @WorkerScoped
    @ContributesAndroidInjector
    abstract StateDistrictWorker stateDistrictWorker();
}
