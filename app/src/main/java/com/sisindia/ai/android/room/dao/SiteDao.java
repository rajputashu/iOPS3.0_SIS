package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.features.disband.DisbandmentSitesMO;
import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.uimodels.ReviewInformationModel;
import com.sisindia.ai.android.uimodels.mysite.SiteGeneralMO;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class SiteDao implements BaseDao<SiteEntity> {

    @Query("SELECT * from SiteEntity WHERE isActive = :isActive AND id > 0 order by sitename")
    public abstract Single<List<SiteEntity>> fetchAllActiveSites(boolean isActive);

    @Query("SELECT * from SiteEntity WHERE isActive = :isActive AND id > 0 and parentSiteId is null order by sitename")
    public abstract Single<List<SiteEntity>> fetchAllActiveSitesForAdHoc(boolean isActive);

    @Query("Select s.id,s.siteGeoPointString as siteGeoCode,s.siteName,s.siteAddress,s.siteCode,substr((select max(estimatedTaskExecutionStartDateTime) " +
            "from TaskEntity where siteid=s.id and taskTypeId=5),1,10) as billSubmission, " +
            "(select '0 days dues' As BillCollection) as billCollection,substr((select max(estimatedTaskExecutionStartDateTime) " +
            "from TaskEntity where siteid=s.id and taskTypeId=7),1,10) as wage from SiteEntity s where s.id=:siteId")
    public abstract Single<SiteGeneralMO> fetchSiteGeneralDetails(int siteId);

    @Query("SELECT max(task.actualTaskExecutionEndDateTime) as lastVisitDateTime, " +
            "task.id as taskId, task.taskTypeId as taskTypeId, tt.name as activityName, " +
            "(SELECT sum(actualStrength) FROM CacheStrengthEntity WHERE taskId = task.id)as checkedGuards, " +
            "(SELECT sum(authorizedStrength) FROM CacheStrengthEntity WHERE taskId = task.id)as authorizedGuards " +
            "FROM TaskEntity AS task  " +
            "LEFT JOIN CacheStrengthEntity AS cs ON cs.taskId = task.id  " +
            "INNER JOIN TaskTypeEntity AS tt ON tt.id = task.taskTypeId  " +
            "WHERE task.siteId = :siteId AND task.taskStatus = 4 AND taskTypeId IN (1,2)")
    public abstract Single<ReviewInformationModel.LastVisitDetail> fetchSiteLastVisitDetail(int siteId);

    @Query("SELECT s.* from BarrackEntity b Join SiteBarrackMapsEntity " +
            "sbm on sbm.barrackId=b.id Join SiteEntity s on s.id=sbm.siteId AND s.isActive=:isActive AND s.id > 0 GROUP by s.id")
    public abstract Single<List<SiteEntity>> fetchAllActiveSitesMappedWithBarrack(boolean isActive);

    /*@Query("Select b.* from BarrackEntity b Join SiteBarrackMapsEntity s on s.barrackId=b.id where s.siteid=:selectedSitId")
    public abstract Single<List<BarrackEntity>> fetchAllBarrackMappedWithSite(int selectedSitId);*/

    @Query("Select * from BarrackEntity ")
    public abstract Single<List<BarrackEntity>> fetchAllBarracks();

    //Sites
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllSites(List<SiteEntity> list);

    @Query("DELETE FROM SiteEntity")
    public abstract Completable deleteSite();

    /*@Query("Select * from SiteEntity  where siteStatus=4")
    public abstract Single<List<SiteRaisingCardsMO>> fetchSitesForRaising();*/

    //    @Query("select sitename || ' (' || sitecode || ')' as siteNameWithCode,id as siteId from siteEntity where id > 0 and isActive=1")
    /*@Query("select sitename as siteName, sitecode as siteCode, id as siteId from siteEntity where id > 0 and isActive=1")
    public abstract Single<List<SalesSitesMO>> fetchSitesWithCode();*/

    //    @Query("select cce.contactfullname from siteEntity se join CustomerContactEntity cce on se.id=cce.customerid where se.id > 0 and se.isActive=1 and se.id=:siteId")
    @Query("SELECT cc.contactfullname from CustomerContactEntity cc Join ContractsEntity c on cc.customerId=c.customerId join SiteEntity s on s.contractId=c.id where s.id=:siteId")
    public abstract Single<List<String>> fetchContacts(int siteId);

    @Query("Select id as siteId, sitename,sitecode,1 operationalSite FROM SiteEntity where sitecode=:siteCode UNION ALL Select id as siteId, sitename,sitecode,0 operationalSite FROM SiteEntity where parentsiteid =(Select Id from SiteEntity where  sitecode= :siteCode)")
    public abstract Single<List<DisbandmentSitesMO>> fetchDisbandSites(String siteCode);
}
