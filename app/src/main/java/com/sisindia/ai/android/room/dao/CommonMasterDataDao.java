package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;

@Dao
public abstract class CommonMasterDataDao {

//    //ActionPlan
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllActionPlan(List<ActionPlanEntity> list);
//
//    @Query("DELETE FROM ActionPlanEntity")
//    public abstract int nukeActionPlanTable();
//
//    //Branch
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllBranch(List<BranchEntity> list);
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<Integer> updateBranch(BranchEntity obj);
//
//    @Query("DELETE FROM BranchEntity")
//    public abstract int nukeBranchTable();
//
//    //BranchExtension
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<Long> insertBranchExtension(BranchExtensionEntity obj);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllBranchExtension(List<BranchExtensionEntity> list);
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<Integer> updateBranchExtension(BranchExtensionEntity obj);
//
//    @Query("DELETE FROM BranchExtensionEntity")
//    public abstract int nukeBranchExtensionTable();
//
//    //ClientHandShakeQuestion
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllClientHandShakeQuestion(List<ClientHandShakeQuestionEntity> list);
//
//    @Query("DELETE FROM ClientHandShakeQuestionEntity")
//    public abstract int nukeClientHandShakeQuestionTable();
//
//    //Company
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<Long> insertCompany(CompanyEntity obj);
//
//    @Query("DELETE FROM CompanyEntity")
//    public abstract int nukeCompanyTable();
//
//    //Country
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllCountry(List<CountryEntity> list);
//
//    @Query("DELETE FROM CountryEntity")
//    public abstract int nukeCountryTable();
//
//    //DeploymentType
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllDeploymentType(List<DeploymentTypeEntity> list);
//
//    @Query("DELETE FROM DeploymentTypeEntity")
//    public abstract int nukeDeploymentTypeTable();
//
//    //DeploymentType
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllTaskTypes(List<TaskTypeEntity> list);
//
//    @Query("DELETE FROM TaskTypeEntity")
//    public abstract int nukeTaskTypeTable();
//
//    //Holiday
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllHoliday(List<HolidayEntity> list);
//
//    @Query("DELETE FROM HolidayEntity")
//    public abstract int nukeHolidayTable();
//
//    //IndustrySector
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllIndustrySector(List<IndustrySectorEntity> list);
//
//    @Query("DELETE FROM IndustrySectorEntity")
//    public abstract int nukeIndustrySectorTable();
//
//    //Language
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllLanguage(List<LanguageEntity> list);
//
//    @Query("DELETE FROM LanguageEntity")
//    public abstract int nukeLanguageTable();
//
//    //LookUp
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllLookUp(List<LookUpEntity> list);
//
//    @Query("DELETE FROM LookUpEntity")
//    public abstract int nukeLookUpTable();
//
//    //Organization
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<Long> insertOrganization(OrganizationEntity obj);
//
//    @Query("DELETE FROM OrganizationEntity")
//    public abstract int nukeOrganizationTable();
//
//    //Rank
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllRanks(List<RankEntity> list);
//
//    @Query("DELETE FROM RankEntity")
//    public abstract int nukeRankTable();
//
//    //Region
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertAllRegion(List<RegionEntity> list);
//
//    @Query("DELETE FROM RegionEntity")
//    public abstract int nukeRegionTable();
//
//    //Region
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllSiteType(List<SiteTypeEntity> list);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllQuesRatingMap(List<ClientHandshakeRatingMapsEntity> list);
//
//    @Query("DELETE FROM ClientHandshakeRatingMapsEntity")
//    public abstract int nukeClientHandshakeRatingMapsTable();
//
//    //kitItems
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllKitItems(List<KitItemEntity> list);
//
//    @Query("DELETE FROM KitItemEntity")
//    public abstract int nukeKitItemTable();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllMetadataDefinitions(List<AttachmentMetadataDefinitionEntity> list);
//
//    @Query("DELETE FROM AttachmentMetadataDefinitionEntity")
//    public abstract int nukeAttachmentMetadataDefinitionTable();
//
//    //kitItems
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Maybe<List<Long>> insertAllKitItemSizes(List<KitItemSizeEntity> list);
//
//    @Query("DELETE FROM KitItemSizeEntity")
//    public abstract int nukeKitItemSizeTable();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public abstract Single<List<Long>> insertActionPlanMaps(List<ActionPlanMapEntity> actionPlanMaps);
//
//    @Query("DELETE FROM ActionPlanMapEntity")
//    public abstract int nukeActionPlanMapTable();


//    @Transaction
//    public void nukeCommonMasterData() {
//        nukeActionPlanTable();
//        nukeBranchTable();
//        nukeBranchExtensionTable();
//        nukeClientHandShakeQuestionTable();
//        nukeCompanyTable();
//        nukeCountryTable();
//        nukeDeploymentTypeTable();
//        nukeTaskTypeTable();
//        nukeHolidayTable();
//        nukeIndustrySectorTable();
//        nukeLanguageTable();
//        nukeLookUpTable();
//        nukeOrganizationTable();
//        nukeRankTable();
//        nukeRegionTable();
//        nukeClientHandshakeRatingMapsTable();
//        nukeKitItemTable();
//        nukeAttachmentMetadataDefinitionTable();
//        nukeKitItemSizeTable();
//        nukeActionPlanMapTable();
//    }
}
