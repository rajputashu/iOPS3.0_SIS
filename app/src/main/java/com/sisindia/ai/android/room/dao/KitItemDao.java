package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.models.kits.KitDistributionItemsBodyMO;
import com.sisindia.ai.android.room.entities.KitDistributionItemsEntity;
import com.sisindia.ai.android.room.entities.KitDistributionListEntity;
import com.sisindia.ai.android.room.entities.KitItemEntity;
import com.sisindia.ai.android.room.entities.KitItemSizeEntity;
import com.sisindia.ai.android.uimodels.AttachmentModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class KitItemDao {

    //kitItems
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllKitItems(List<KitItemEntity> list);

    @Query("DELETE FROM KitItemEntity")
    public abstract Completable deleteKitItem();

    //kitItems
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllKitItemSizes(List<KitItemSizeEntity> list);

    @Query("DELETE FROM KitItemSizeEntity")
    public abstract Completable deleteKitItemSize();

    //Adding for Kit Distribution list and items
    /*@Query("DELETE FROM KitDistributionListEntity")
    public abstract Completable deleteKitDistributionList();

    @Query("DELETE FROM KitDistributionItemsEntity")
    public abstract Completable deleteKitDistributionItems();*/

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllKitDistributionList(List<KitDistributionListEntity> list);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllKitDistributionItems(List<KitDistributionItemsEntity> list);

//    @Query("select id from kitDistributionLIstEntity where distributionstatus not in (4,5)")
    @Query("select id from kitDistributionLIstEntity")
    public abstract Single<List<Integer>> fetchAllKitListIds();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertAkrKit(KitDistributionListEntity obj);

    @Query("SELECT * FROM KitDistributionListEntity WHERE distributionStatus in (3,4,5) and isAllItemsDistributed=0")
    public abstract Single<List<KitDistributionListEntity>> fetchAllKitsForDistribution();

    @Query("SELECT * FROM KitDistributionItemsEntity WHERE isIssued in (0,1) and kitDistributionId=:kitDistId and isSynced=0")
    public abstract Single<List<KitDistributionItemsBodyMO>> fetchAllKitItemsForDistribution(int kitDistId);

    @Query("UPDATE KitDistributionItemsEntity set isSynced=1 where id IN (:ids)")
    public abstract Single<Integer> updateSyncStatus(List<Integer> ids);

    @Query("UPDATE KitDistributionItemsEntity SET issuedDate=:updatedDateTime,isIssued = 1, isSynced=0 WHERE id IN (:ids)")
    public abstract Single<Integer> updateKitDistributedItems(String updatedDateTime, List<Integer> ids);

    @Query("UPDATE KitDistributionListEntity SET distributionDateTime=:updatedDateTime,updatedDateTime=:updatedDateTime, distributionStatus = :statusCode, isAllItemsDistributed = 0 WHERE id =:id")
    public abstract Single<Integer> updateDistributedKitStatus(String updatedDateTime, int id, int statusCode);

    //    @Query("UPDATE KitDistributionListEntity SET  distributionStatus = 3, isAllItemsDistributed = 1 WHERE id =:id")
    @Query("UPDATE KitDistributionListEntity SET isAllItemsDistributed = 1 WHERE id =:id")
    public abstract Single<Integer> updateDistributedKitStatus(int id);

    @Query("UPDATE KitDistributionItemsEntity SET issuedDate=:updatedDateTime, isIssued = 0, nonIssueReason=:reasonId, isSynced=0 WHERE id IN (:ids)")
    public abstract Single<Integer> updateUnPaidKitItems(String updatedDateTime, int reasonId, List<Integer> ids);

    @Query("SELECT task.serverId as serverId, (:sourceType||'_'||site.id||'_'||task.serverId||'_'||:employeeId||'_'||:guid) AS fileName, " +
            "(ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||site.siteCode||'/'||type.name||'/'||task.serverId||'/'||:sourceType||'_'||site.id||'_'||task.serverId||'_'||:employeeId||'_'||:guid) AS storagePath " +
            "FROM TaskEntity as task LEFT JOIN AIProfileEntity as ai " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id=task.taskTypeId WHERE task.id= :taskId")
    public abstract Single<AttachmentModel> attachmentKitDistributeSignature(int sourceType, int employeeId, String guid, int taskId);
}

/*
 * isIssued =0 : {Kit items is pending for distribution}
 * isIssued =1 : {Kit items is distributed to guards}
 * isIssued =2 : {Kit items is unpaid due to some reasons}
 * */

/*
 * distributionStatus =3,4 : {Kit Distributed}
 * distributionStatus =5 : {Kit Unpaid}
 * */
