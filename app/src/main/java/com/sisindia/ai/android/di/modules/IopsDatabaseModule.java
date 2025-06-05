package com.sisindia.ai.android.di.modules;

import android.content.Context;

import androidx.room.Room;

import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.room.IopsDatabase;
import com.sisindia.ai.android.room.customdao.RawQueriesDao;
import com.sisindia.ai.android.room.customdao.SiteAndTasksDao;
import com.sisindia.ai.android.room.customdao.UarAndPoaDao;
import com.sisindia.ai.android.room.dao.AIProfileDao;
import com.sisindia.ai.android.room.dao.ActionPlanDao;
import com.sisindia.ai.android.room.dao.AddSecurityRiskDao;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.AttachmentMetadataDefinitionDao;
import com.sisindia.ai.android.room.dao.BarrackDao;
import com.sisindia.ai.android.room.dao.BarrackStrengthDao;
import com.sisindia.ai.android.room.dao.BarrackTaggingDao;
import com.sisindia.ai.android.room.dao.BillCenterDao;
import com.sisindia.ai.android.room.dao.BillCollectionDao;
import com.sisindia.ai.android.room.dao.BranchDao;
import com.sisindia.ai.android.room.dao.BranchExtensionDao;
import com.sisindia.ai.android.room.dao.ClientHandShakeQuestionDao;
import com.sisindia.ai.android.room.dao.ClientHandshakeRatingMapDao;
import com.sisindia.ai.android.room.dao.CommonMasterDataDao;
import com.sisindia.ai.android.room.dao.CompanyDao;
import com.sisindia.ai.android.room.dao.ComplaintDao;
import com.sisindia.ai.android.room.dao.ContractsDao;
import com.sisindia.ai.android.room.dao.CountryDao;
import com.sisindia.ai.android.room.dao.CustomerContactDao;
import com.sisindia.ai.android.room.dao.CustomerDao;
import com.sisindia.ai.android.room.dao.CustomerSiteContactDao;
import com.sisindia.ai.android.room.dao.DailyTimeLineDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.DeploymentTypeDao;
import com.sisindia.ai.android.room.dao.DutyStatusDao;
import com.sisindia.ai.android.room.dao.DynamicTaskDao;
import com.sisindia.ai.android.room.dao.EmployeeFineRewardDao;
import com.sisindia.ai.android.room.dao.EmployeeLeaveDao;
import com.sisindia.ai.android.room.dao.EmployeeSiteDao;
import com.sisindia.ai.android.room.dao.GeoLocationDao;
import com.sisindia.ai.android.room.dao.GrievanceDao;
import com.sisindia.ai.android.room.dao.GuardKitRequestDao;
import com.sisindia.ai.android.room.dao.HolidayDao;
import com.sisindia.ai.android.room.dao.ImprovementPoaDao;
import com.sisindia.ai.android.room.dao.IndustrySectorDao;
import com.sisindia.ai.android.room.dao.KitItemDao;
import com.sisindia.ai.android.room.dao.KitRequestItemDao;
import com.sisindia.ai.android.room.dao.LanguageDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.dao.MappedRegistersDao;
import com.sisindia.ai.android.room.dao.NotificationsDao;
import com.sisindia.ai.android.room.dao.OrganizationDao;
import com.sisindia.ai.android.room.dao.PostCheckListDao;
import com.sisindia.ai.android.room.dao.PostRegistersDao;
import com.sisindia.ai.android.room.dao.PreDashBoardEffortsDao;
import com.sisindia.ai.android.room.dao.RankDao;
import com.sisindia.ai.android.room.dao.RecruitmentDao;
import com.sisindia.ai.android.room.dao.RegionDao;
import com.sisindia.ai.android.room.dao.RegistersDao;
import com.sisindia.ai.android.room.dao.SiteAtRiskDao;
import com.sisindia.ai.android.room.dao.SiteBillingCheckListDao;
import com.sisindia.ai.android.room.dao.SiteCheckListDao;
import com.sisindia.ai.android.room.dao.SiteDao;
import com.sisindia.ai.android.room.dao.SiteExtensionDao;
import com.sisindia.ai.android.room.dao.SitePostDao;
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao;
import com.sisindia.ai.android.room.dao.SiteShiftDao;
import com.sisindia.ai.android.room.dao.SiteStrengthDao;
import com.sisindia.ai.android.room.dao.SiteTypeDao;
import com.sisindia.ai.android.room.dao.StateDistrictDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.dao.TaskTypeDao;
import com.sisindia.ai.android.room.dao.UserMasterDataDao;
import com.sisindia.ai.android.room.dao.WageCenterDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class IopsDatabaseModule {

    @Singleton
    @Provides
    IopsDatabase provideIopsDatabase(Context context) {
        return Room.databaseBuilder(context, IopsDatabase.class, BuildConfig.APPLICATION_ID)
                .fallbackToDestructiveMigration() // Use this for uninstall and then install
                .allowMainThreadQueries()
//                .addMigrations(MigrationFactory.INSTANCE.getMIGRATION_1_2()) // {While using this : change DB version as well and comment destructive migration}
                .build();
    }

    @Singleton
    @Provides
    SiteDao provideIopsSiteDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.iopsSiteDao();
    }

    @Singleton
    @Provides
    TaskDao provideIopsTaskDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.taskDao();
    }


    @Singleton
    @Provides
    ActionPlanDao provideActionPlanDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.actionPlanDao();
    }

    @Singleton
    @Provides
    BranchDao provideBranchDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.branchDao();
    }

    @Singleton
    @Provides
    BranchExtensionDao provideBranchExtensionDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.branchExtensionDao();
    }

    @Singleton
    @Provides
    ClientHandShakeQuestionDao provideClientHandShakeQuestionDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.clientHandShakeQuestionDao();
    }

    @Singleton
    @Provides
    CompanyDao provideCompanyDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.companyDao();
    }

    @Singleton
    @Provides
    CountryDao provideCountryDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.countryDao();
    }

    @Singleton
    @Provides
    DeploymentTypeDao provideDeploymentTypeDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.deploymentTypeDao();
    }

    @Singleton
    @Provides
    HolidayDao provideHolidayDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.holidayDao();
    }

    @Singleton
    @Provides
    IndustrySectorDao provideIndustrySectorDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.industrySectorDao();
    }

    @Singleton
    @Provides
    LanguageDao provideLanguageDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.languageDao();
    }

    @Singleton
    @Provides
    LookUpDao provideLookUpDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.lookUpDao();
    }

    @Singleton
    @Provides
    RankDao provideRankDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.rankDao();
    }

    @Singleton
    @Provides
    OrganizationDao provideOrganizationDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.organizationDao();
    }

    @Singleton
    @Provides
    RegionDao provideRegionDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.regionDao();
    }

    @Singleton
    @Provides
    SiteTypeDao provideSiteTypeDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteTypeDao();
    }

    @Singleton
    @Provides
    CommonMasterDataDao provideCommonMasterDataDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.commonMasterDataDao();
    }


    @Singleton
    @Provides
    EmployeeLeaveDao provideEmployeeLeaveDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.employeeLeaveDao();
    }

    @Singleton
    @Provides
    EmployeeSiteDao provideEmployeeSiteDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.employeeSiteDao();
    }

    @Singleton
    @Provides
    CustomerDao provideCustomerDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.customerDao();
    }

    @Singleton
    @Provides
    CustomerContactDao provideCustomerContactDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.customerContactDao();
    }

    @Singleton
    @Provides
    CustomerSiteContactDao provideCustomerSiteContactDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.customerSiteContactDao();
    }

    @Singleton
    @Provides
    BarrackDao provideBarrackDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.barrackDao();
    }

    @Singleton
    @Provides
    BillCenterDao provideBillCenterDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.billCenterDao();
    }

    @Singleton
    @Provides
    SiteExtensionDao provideSiteExtensionDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteExtensionDao();
    }

    @Singleton
    @Provides
    SiteShiftDao provideSiteShiftDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteShiftDao();
    }

    @Singleton
    @Provides
    SitePostDao provideSitePostDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.sitePostDao();
    }

    @Singleton
    @Provides
    WageCenterDao provideWageCenterDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.wageCenterDao();
    }

    @Singleton
    @Provides
    UserMasterDataDao provideUserMasterDataDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.userMasterDataDao();
    }

    @Singleton
    @Provides
    SiteStrengthDao provideSiteStrengthDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteStrengthDao();
    }

    @Singleton
    @Provides
    SiteAndTasksDao provideSiteAndTaskDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteAndTaskDao();
    }

    @Singleton
    @Provides
    DutyStatusDao provideDutyStatusDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.dutyStatusDao();
    }

    @Singleton
    @Provides
    DayCheckDao provideDayCheckDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.dayCheckDao();
    }

    @Singleton
    @Provides
    AttachmentDao provideAttachmentDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.attachmentDao();
    }

    @Singleton
    @Provides
    EmployeeFineRewardDao provideFineRewardDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.fineRewardDao();
    }

    @Singleton
    @Provides
    SiteAtRiskDao provideAtRiskDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteAtRiskDao();
    }

    @Singleton
    @Provides
    SiteRiskPoaDao provideRiskPOADao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteAtRiskPoaDao();
    }

    @Singleton
    @Provides
    UarAndPoaDao provideAtRiskAndPOADao(IopsDatabase iopsDatabase) {
        return iopsDatabase.uarAndPoaDao();
    }

    @Singleton
    @Provides
    AIProfileDao provideAIProfileDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.aiProfileDao();
    }

    @Singleton
    @Provides
    GrievanceDao provideGrievanceDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.grievanceDao();
    }

    @Singleton
    @Provides
    AddSecurityRiskDao provideAddSecurityRiskDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.addSecurityRiskDao();
    }

    @Singleton
    @Provides
    RegistersDao provideRegistersDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.registersDao();
    }

    @Singleton
    @Provides
    TaskTypeDao provideTaskTypeDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.taskTypeDao();
    }

    @Singleton
    @Provides
    GuardKitRequestDao provideGuardKitRequestDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.guardKitRequestDao();
    }

    @Singleton
    @Provides
    KitRequestItemDao provideKitRequestItemDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.kitRequestItemDao();
    }

    @Singleton
    @Provides
    ClientHandshakeRatingMapDao provideHandshakeQuesMapDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.clientHandshakeQuestionsMapDao();
    }

    @Singleton
    @Provides
    ContractsDao provideContractsDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.contractsDao();
    }

    @Singleton
    @Provides
    BarrackTaggingDao provideBarrackTaggingDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.barrackTaggingDao();
    }

    @Singleton
    @Provides
    GeoLocationDao provideGeoLocationDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.geoLocationDao();
    }

    @Singleton
    @Provides
    PreDashBoardEffortsDao provideEffortsDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.effortsDao();
    }

    @Singleton
    @Provides
    ComplaintDao provideComplaintDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.complaintDao();
    }

    @Singleton
    @Provides
    RecruitmentDao provideRecruitmentDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.recruitmentDao();
    }

    @Singleton
    @Provides
    AttachmentMetadataDefinitionDao provideMetadataDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.attachmentMetaDataDao();
    }

    @Singleton
    @Provides
    BillCollectionDao provideBillCollectionDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.billCollectionDao();
    }

    @Singleton
    @Provides
    KitItemDao provideKitItemDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.kitItemDao();
    }

    @Singleton
    @Provides
    SiteCheckListDao provideSiteCheckListDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteCheckListDao();
    }

    @Singleton
    @Provides
    PostCheckListDao providePostCheckListDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.postCheckListDao();
    }

    @Singleton
    @Provides
    PostRegistersDao providePostRegistersDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.postRegistersDao();
    }

    @Singleton
    @Provides
    SiteBillingCheckListDao provideSiteBillingCheckListDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.siteBillingCheckListDao();
    }

    @Singleton
    @Provides
    BarrackStrengthDao provideBarrackStrengthDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.barrackStrengthDao();
    }

    @Singleton
    @Provides
    DailyTimeLineDao provideDailyTimeLineDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.dailyTimeLineDao();
    }

    @Singleton
    @Provides
    MappedRegistersDao provideMapRegisterDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.mappedRegistersDao();
    }

    @Singleton
    @Provides
    ImprovementPoaDao provideImprovementPoaDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.improvementPoaDao();
    }

    @Singleton
    @Provides
    RawQueriesDao provideRawQueryDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.rawQueriesDao();
    }

    @Singleton
    @Provides
    NotificationsDao provideNotificationDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.notificationsDao();
    }

    @Singleton
    @Provides
    DynamicTaskDao provideDynamicTaskDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.dynamicTaskDao();
    }

    @Singleton
    @Provides
    StateDistrictDao provideStateDistrictDao(IopsDatabase iopsDatabase) {
        return iopsDatabase.stateDistrictDao();
    }
}