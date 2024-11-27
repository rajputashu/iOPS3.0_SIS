package com.sisindia.ai.android.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.dao.TaskTypeDao;
import com.sisindia.ai.android.room.dao.UserMasterDataDao;
import com.sisindia.ai.android.room.dao.WageCenterDao;
import com.sisindia.ai.android.room.entities.AIProfileEntity;
import com.sisindia.ai.android.room.entities.ActionPlanEntity;
import com.sisindia.ai.android.room.entities.ActionPlanMapEntity;
import com.sisindia.ai.android.room.entities.AddRecruitmentEntity;
import com.sisindia.ai.android.room.entities.AddSecurityRiskEntity;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.AttachmentMetadataDefinitionEntity;
import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.BarrackStrengthEntity;
import com.sisindia.ai.android.room.entities.BarrackTaggingEntity;
import com.sisindia.ai.android.room.entities.BillCenterEntity;
import com.sisindia.ai.android.room.entities.BillCollectionsEntity;
import com.sisindia.ai.android.room.entities.BranchEntity;
import com.sisindia.ai.android.room.entities.BranchExtensionEntity;
import com.sisindia.ai.android.room.entities.CacheAIProfileEntity;
import com.sisindia.ai.android.room.entities.CacheBarrackInspectionEntity;
import com.sisindia.ai.android.room.entities.CacheBillCollectionEntity;
import com.sisindia.ai.android.room.entities.CacheClientHandshakeEntity;
import com.sisindia.ai.android.room.entities.CacheStrengthEntity;
import com.sisindia.ai.android.room.entities.CheckInOutEntity;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.room.entities.CheckedPostEntity;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;
import com.sisindia.ai.android.room.entities.ClientHandShakeQuestionEntity;
import com.sisindia.ai.android.room.entities.ClientHandshakeRatingMapsEntity;
import com.sisindia.ai.android.room.entities.ClosedImprovementPoaEntity;
import com.sisindia.ai.android.room.entities.CompanyEntity;
import com.sisindia.ai.android.room.entities.ComplaintEntity;
import com.sisindia.ai.android.room.entities.ContractsEntity;
import com.sisindia.ai.android.room.entities.CountryEntity;
import com.sisindia.ai.android.room.entities.CustomerContactEntity;
import com.sisindia.ai.android.room.entities.CustomerEntity;
import com.sisindia.ai.android.room.entities.CustomerSiteContactEntity;
import com.sisindia.ai.android.room.entities.DailyTimeLineEntity;
import com.sisindia.ai.android.room.entities.DeploymentTypeEntity;
import com.sisindia.ai.android.room.entities.DutyStatusEntity;
import com.sisindia.ai.android.room.entities.DynamicFormEntity;
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity;
import com.sisindia.ai.android.room.entities.EmployeeLeaveEntity;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;
import com.sisindia.ai.android.room.entities.GeoLocationEntity;
import com.sisindia.ai.android.room.entities.GrievanceEntity;
import com.sisindia.ai.android.room.entities.GuardKitRequestEntity;
import com.sisindia.ai.android.room.entities.HolidayEntity;
import com.sisindia.ai.android.room.entities.ImprovementPoaEntity;
import com.sisindia.ai.android.room.entities.IndustrySectorEntity;
import com.sisindia.ai.android.room.entities.KitDistributionItemsEntity;
import com.sisindia.ai.android.room.entities.KitDistributionListEntity;
import com.sisindia.ai.android.room.entities.KitItemEntity;
import com.sisindia.ai.android.room.entities.KitItemSizeEntity;
import com.sisindia.ai.android.room.entities.KitRequestItemEntity;
import com.sisindia.ai.android.room.entities.LanguageEntity;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.room.entities.MappedRegistersEntity;
import com.sisindia.ai.android.room.entities.NotificationDataEntity;
import com.sisindia.ai.android.room.entities.OrganizationEntity;
import com.sisindia.ai.android.room.entities.PostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.PostRegisterEntity;
import com.sisindia.ai.android.room.entities.PractoQuestionEntity;
import com.sisindia.ai.android.room.entities.RankEntity;
import com.sisindia.ai.android.room.entities.RegionEntity;
import com.sisindia.ai.android.room.entities.RegisterAttachmentEntity;
import com.sisindia.ai.android.room.entities.RotaTasksEntity;
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity;
import com.sisindia.ai.android.room.entities.SiteBarrackMapsEntity;
import com.sisindia.ai.android.room.entities.SiteBillingCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.room.entities.SiteExtensionEntity;
import com.sisindia.ai.android.room.entities.SitePostEntity;
import com.sisindia.ai.android.room.entities.SiteRiskPoaEntity;
import com.sisindia.ai.android.room.entities.SiteShiftEntity;
import com.sisindia.ai.android.room.entities.SiteStrengthEntity;
import com.sisindia.ai.android.room.entities.SiteTypeEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.room.entities.TaskTypeEntity;
import com.sisindia.ai.android.room.entities.WageCenterEntity;
import com.sisindia.ai.android.utils.GeoLocationConverter;
import com.sisindia.ai.android.utils.MetaDataConverter;

@Database(entities = {
        ActionPlanEntity.class,
        BranchEntity.class,
        BranchExtensionEntity.class,
        ClientHandShakeQuestionEntity.class,
        CompanyEntity.class,
        CountryEntity.class,
        DeploymentTypeEntity.class,
        HolidayEntity.class,
        IndustrySectorEntity.class,
        LanguageEntity.class,
        LookUpEntity.class,
        OrganizationEntity.class,
        RankEntity.class,
        RegionEntity.class,
        SiteTypeEntity.class,
        BarrackEntity.class,
        BillCenterEntity.class,
        CustomerEntity.class,
        CustomerContactEntity.class,
        CustomerSiteContactEntity.class,
        EmployeeLeaveEntity.class,
        EmployeeSiteEntity.class,
        SiteExtensionEntity.class,
        SitePostEntity.class,
        SiteShiftEntity.class,
        WageCenterEntity.class,
        SiteStrengthEntity.class,
        SiteEntity.class,
        TaskEntity.class,
        DutyStatusEntity.class,
        CheckedPostEntity.class,
        CheckedGuardEntity.class,
        AttachmentEntity.class,
        EmployeeFineRewardEntity.class,
        CacheAIProfileEntity.class,
        SiteAtRiskEntity.class,
        SiteRiskPoaEntity.class,
        GrievanceEntity.class,
        AddSecurityRiskEntity.class,
        CheckedRegisterEntity.class,
        PostRegisterEntity.class,
        RegisterAttachmentEntity.class,
        TaskTypeEntity.class,
        KitItemSizeEntity.class,
        KitItemEntity.class,
        KitRequestItemEntity.class,
        GuardKitRequestEntity.class,
        ClientHandshakeRatingMapsEntity.class,
        ContractsEntity.class,
        ActionPlanMapEntity.class,
        PostCheckListEntity.class,
        PostCheckListOptionEntity.class,
        SiteCheckListEntity.class,
        SiteCheckListOptionEntity.class,
        BarrackTaggingEntity.class,
        CacheBarrackInspectionEntity.class,
        CheckedSiteCheckListEntity.class,
        CheckedPostCheckListEntity.class,
        CacheClientHandshakeEntity.class,
        CacheStrengthEntity.class,
        GeoLocationEntity.class,
        ComplaintEntity.class,
        AddRecruitmentEntity.class,
        AIProfileEntity.class,
        AttachmentMetadataDefinitionEntity.class,
        SiteBarrackMapsEntity.class,
        BillCollectionsEntity.class,
        CacheBillCollectionEntity.class,
        SiteBillingCheckListEntity.class,
        BarrackStrengthEntity.class,
        DailyTimeLineEntity.class,
        KitDistributionListEntity.class,
        KitDistributionItemsEntity.class,
        MappedRegistersEntity.class,
        ImprovementPoaEntity.class,
        RotaTasksEntity.class,
        ClosedImprovementPoaEntity.class,
        NotificationDataEntity.class,
        DynamicFormEntity.class,
        PractoQuestionEntity.class,
        CheckInOutEntity.class,
}, version = 1, exportSchema = false)
@TypeConverters({MetaDataConverter.class, GeoLocationConverter.class})
public abstract class IopsDatabase extends RoomDatabase {

    public abstract SiteDao iopsSiteDao();

    public abstract TaskDao taskDao();

    public abstract ActionPlanDao actionPlanDao();

    public abstract BranchDao branchDao();

    public abstract BranchExtensionDao branchExtensionDao();

    public abstract ClientHandShakeQuestionDao clientHandShakeQuestionDao();

    public abstract CompanyDao companyDao();

    public abstract CountryDao countryDao();

    public abstract DeploymentTypeDao deploymentTypeDao();

    public abstract HolidayDao holidayDao();

    public abstract IndustrySectorDao industrySectorDao();

    public abstract LookUpDao lookUpDao();

    public abstract LanguageDao languageDao();

    public abstract OrganizationDao organizationDao();

    public abstract RankDao rankDao();

    public abstract RegionDao regionDao();

    public abstract SiteTypeDao siteTypeDao();

    public abstract CommonMasterDataDao commonMasterDataDao();

    public abstract EmployeeLeaveDao employeeLeaveDao();

    public abstract EmployeeSiteDao employeeSiteDao();

    public abstract CustomerDao customerDao();

    public abstract CustomerContactDao customerContactDao();

    public abstract CustomerSiteContactDao customerSiteContactDao();

    public abstract BarrackDao barrackDao();

    public abstract BillCenterDao billCenterDao();

    public abstract SiteExtensionDao siteExtensionDao();

    public abstract SiteShiftDao siteShiftDao();

    public abstract SitePostDao sitePostDao();

    public abstract WageCenterDao wageCenterDao();

    public abstract UserMasterDataDao userMasterDataDao();

    public abstract SiteStrengthDao siteStrengthDao();

    public abstract SiteAndTasksDao siteAndTaskDao();

    public abstract DutyStatusDao dutyStatusDao();

    public abstract DayCheckDao dayCheckDao();

    public abstract AttachmentDao attachmentDao();

    public abstract EmployeeFineRewardDao fineRewardDao();

    public abstract SiteAtRiskDao siteAtRiskDao();

    public abstract SiteRiskPoaDao siteAtRiskPoaDao();

    public abstract UarAndPoaDao uarAndPoaDao();

    public abstract AIProfileDao aiProfileDao();

    public abstract GrievanceDao grievanceDao();

    public abstract AddSecurityRiskDao addSecurityRiskDao();

    public abstract RegistersDao registersDao();

    public abstract TaskTypeDao taskTypeDao();

    public abstract ClientHandshakeRatingMapDao clientHandshakeQuestionsMapDao();

    public abstract ContractsDao contractsDao();

    public abstract GuardKitRequestDao guardKitRequestDao();

    public abstract KitRequestItemDao kitRequestItemDao();

    public abstract BarrackTaggingDao barrackTaggingDao();

    public abstract GeoLocationDao geoLocationDao();

    public abstract PreDashBoardEffortsDao effortsDao();

    public abstract ComplaintDao complaintDao();

    public abstract RecruitmentDao recruitmentDao();

    public abstract AttachmentMetadataDefinitionDao attachmentMetaDataDao();

    public abstract BillCollectionDao billCollectionDao();

    public abstract KitItemDao kitItemDao();

    public abstract SiteCheckListDao siteCheckListDao();

    public abstract PostRegistersDao postRegistersDao();

    public abstract PostCheckListDao postCheckListDao();

    public abstract SiteBillingCheckListDao siteBillingCheckListDao();

    public abstract BarrackStrengthDao barrackStrengthDao();

    public abstract DailyTimeLineDao dailyTimeLineDao();

    public abstract MappedRegistersDao mappedRegistersDao();

    public abstract ImprovementPoaDao improvementPoaDao();

    public abstract RawQueriesDao rawQueriesDao();

    public abstract NotificationsDao notificationsDao();

    public abstract DynamicTaskDao dynamicTaskDao();
}
