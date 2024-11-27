package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.uimodels.EffortsModel;

import io.reactivex.Single;

@Dao
public abstract class PreDashBoardEffortsDao {

    //Bill Submission
    @Query("SELECT (SELECT COUNT(id) from TaskEntity WHERE taskTypeId=5 AND taskStatus in (1,2)) AS billSubmissionPending, " +
            "(SELECT COUNT(id) from TaskEntity WHERE taskTypeId=5 AND taskStatus =4) AS billSubmissionCompleted, " +
            "(SELECT '') As billSubmissionOverDue")
    public abstract Single<EffortsModel.EffortBillSubmission> fetchBillSubmission();

    //Mon Input
    /*@Query("SELECT (SELECT COUNT(id) FROM TaskEntity WHERE strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and taskStatus!=4 and otherTaskTypeLookUpIdentifier=8) AS monInputPending, (SELECT COUNT(id) " +
            "FROM TaskEntity WHERE strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and taskStatus=4 and otherTaskTypeLookUpIdentifier=8) AS monInputCompleted")*/
    @Query("SELECT (SELECT COUNT(id) FROM TaskEntity WHERE strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and taskStatus not in (3,4) and otherTaskTypeLookUpIdentifier=8) AS monInputPending, (SELECT COUNT(id) FROM TaskEntity WHERE strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and taskStatus=4 and otherTaskTypeLookUpIdentifier=8) AS monInputCompleted")
    public abstract Single<EffortsModel.EffortMonInput> fetchMonInput();

    //BILL COLLECTION
    @Query("select (select count(DISTINCT(billOutputCenterCode)) from BillCollectionsEntity) bills," +
            "(select count(billOutputCenterCode) from BillCollectionsEntity where collectionStatus=1 and " +
            "id not in (select billId from CacheBillCollectionEntity)) outstanding, " +
            "(select count(billOutputCenterCode) from BillCollectionsEntity where collectionStatus=3 " +
            "and id in (select billId from CacheBillCollectionEntity)) overdue")
    public abstract Single<EffortsModel.EffortBillCollection> fetchBillCollection();

    //DAY CHECK
    /*@Query("select (select Count(id) from TaskEntity where estimatedTaskExecutionStartDateTime BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') " +
            "and date() and taskTypeId=1) as target, count(id) as rota, (select count(id) from TaskEntity where taskTypeId=1 and taskStatus=4 and " +
            "estimatedTaskExecutionStartDateTime BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date()) as completed from TaskEntity t where taskTypeId=1 " +
            "and estimatedTaskExecutionStartDateTime BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date() and isAutoCreation=1")*/
    @Query("select (select Count(id) from TaskEntity where date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date() and taskTypeId=1) as target, count(id) as rota, (select count(id) from TaskEntity where taskTypeId=1 and taskStatus=4 and date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date()) as completed from TaskEntity t where taskTypeId=1 and date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date() and isAutoCreation=1")
    public abstract Single<EffortsModel.EffortsDayCheck> fetchDayCheck();

    //NIGHT CHECK
    /*@Query("select (select COunt(id) from TaskEntity where estimatedTaskExecutionStartDateTime BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') " +
            "and date() and taskTypeId=2) as target, count(id) as rota, (select count(id) from TaskEntity where taskTypeId=2 and taskStatus=4 " +
            "and estimatedTaskExecutionStartDateTime BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date()) as completed from TaskEntity t where taskTypeId=2 " +
            "and estimatedTaskExecutionStartDateTime BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date() and isAutoCreation=1")*/
    @Query("select (select Count(id) from TaskEntity where date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date() and taskTypeId=2) as target, count(id) as rota, (select count(id) from TaskEntity where taskTypeId=2 and taskStatus=4 and date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date()) as completed from TaskEntity t where taskTypeId=2 and date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date() and isAutoCreation=1")
    public abstract Single<EffortsModel.EffortsNightCheck> fetchNightCheck();

    //UNITS AT RISK
    @Query("Select (Select count(siteId) from SiteAtRiskEntity where riskStatus!=3) siteAtRisk, (Select count(siteId) from SiteRiskPoaEntity) poaCreated, " +
            "(Select count(siteId) from SiteRiskPoaEntity where poAStatus!=3) poaPending")
    public abstract Single<EffortsModel.EffortsSiteAtRisk> fetchUnitAtRisk();

    //OTHERS
    @Query("select count(id) as created, (select count(id) from TaskEntity where taskTypeId=7 and taskStatus=4 and date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date()) as completed from TaskEntity t where taskTypeId=7 and date(estimatedTaskExecutionStartDateTime) BETWEEN date('now', 'start of day', 'weekday 6', '-7 day') and date()")
    public abstract Single<EffortsModel.EffortsOthers> fetchOthers();

    //Units {Pending GeoTagging, Pending SPI}
    @Query("Select (Select count(id) from SitePostEntity where spiEnabled=0) pendingSPI, (Select count(id) from SitePostEntity where (length(postGeoPointString)<1 or postGeoPointString is null) ) pendingGeoTagging")
    public abstract Single<EffortsModel.EffortUnits> fetchUnits();
}
