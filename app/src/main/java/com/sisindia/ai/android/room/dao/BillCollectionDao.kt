package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.models.BillCollectionApiBodyMO
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.BillCollectionsEntity
import com.sisindia.ai.android.room.entities.CacheBillCollectionEntity
import com.sisindia.ai.android.uimodels.collection.CollectionCardDetailsMO
import com.sisindia.ai.android.uimodels.collection.CollectionsCountMO
import com.sisindia.ai.android.uimodels.collection.DueBillCollectionMO
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 5/21/2020.
 */
@Dao
abstract class BillCollectionDao : BaseDao<BillCollectionsEntity> {

    @Query("SELECT * from BillCollectionsEntity")
    abstract fun fetchAll(): Single<List<BillCollectionsEntity>>

    //    @Query("SELECT count(b.id) as dueCollection,s.siteName,s.siteCode,s.id as siteId,sum(b.paidAmount) as paidAmount,sum(b.outstandingAmount) as outstandingAmount from BillCollectionsEntity b join SiteEntity s on s.siteCode=b.billOutputCenterCode where collectionStatus=1 group by billOutputCenterCode")
    @Query("SELECT count(b.id) as dueCollection,s.siteName,s.siteCode,s.id as siteId,sum(b.paidAmount) as paidAmount, sum(b.outstandingAmount) as outstandingAmount from BillCollectionsEntity b join SiteEntity s on s.siteCode=b.billOutputCenterCode where b.Id not in (select billId from CacheBillCollectionEntity) group by billOutputCenterCode")
    abstract fun fetchAllDueBillCollections(): Single<List<DueBillCollectionMO>>

    @Query("select bc.*,s.siteName,s.id as siteId from BillCollectionsEntity bc join siteEntity s on bc.billOutputCenterCode=s.siteCode where s.id=:siteId")
    abstract fun fetchDueBillCollectionViaSiteId(siteId: Int): Single<List<BillCollectionsEntity>>

    @Query("SELECT * from CacheBillCollectionEntity where isSynced=0")
    abstract fun fetchUnSyncedDetails(): Single<List<BillCollectionApiBodyMO>>

    //    @Query("SELECT (Select count(Id) from BillCollectionsEntity where collectionStatus=1 and Id not in (select id from CacheBillCollectionEntity))as billDuesCount, (Select count(Id) from BillCollectionsEntity where collectionStatus=1 and outstandingAmount>100000 and Id not in (select id from CacheBillCollectionEntity))as lacsOutstandingCount, (Select count(billOutputCenterCode) from BillCollectionsEntity where Id not in (select id from CacheBillCollectionEntity) group by billOutputCenterCode )as unitsDueCount")
    @Query("SELECT (Select count(Id) from BillCollectionsEntity where collectionStatus=1 and Id not in (select billid from CacheBillCollectionEntity))as billDuesCount, (Select count(Id) from BillCollectionsEntity where collectionStatus=1 and outstandingAmount>100000 and Id not in (select billid from CacheBillCollectionEntity))as lacsOutstandingCount, (Select count(DISTINCT(billOutputCenterCode)) from BillCollectionsEntity where Id not in (select billId from CacheBillCollectionEntity)) as unitsDueCount")
    abstract fun fetchCollectionsCount(): Single<CollectionsCountMO>

    @Query("SELECT s.siteName,s.siteCode,s.siteAddress,s.id as siteId, 1 as isPending, (select count(id) from BillCollectionsEntity where billOutputCenterCode=b.billOutputCenterCode group by billOutputCenterCode) as billDuesCount from BillCollectionsEntity b join SiteEntity s on s.siteCode=b.billOutputCenterCode where b.Id not in (select billId from CacheBillCollectionEntity) group by billOutputCenterCode")
    abstract fun fetchPendingCollections(): Single<List<CollectionCardDetailsMO>>

    @Query("SELECT s.siteName,s.siteCode,s.siteAddress,s.id as siteId,0 as isPending, (select count(id) from BillCollectionsEntity where billOutputCenterCode=b.billOutputCenterCode group by billOutputCenterCode ) as billDuesCount from BillCollectionsEntity b join SiteEntity s on s.siteCode=b.billOutputCenterCode where b.Id in (select billId from CacheBillCollectionEntity) group by billOutputCenterCode")
    abstract fun fetchCompletedCollections(): Single<List<CollectionCardDetailsMO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllCacheBillCollection(obj: List<CacheBillCollectionEntity>): Single<List<Long>>

    @Query("UPDATE CacheBillCollectionEntity set isSynced=:syncStatus where billId=:id")
    abstract fun updateSyncStatus(id: Int, syncStatus: Boolean = true): Single<Int>

    //Bill Collections
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllBillCollections(list: List<BillCollectionsEntity>): Single<List<Long>>

    @Query("DELETE FROM BillCollectionsEntity")
    abstract fun deleteBillCollections(): Completable

    @Query("SELECT * from BillCollectionsEntity")
    abstract fun fetchAllAutoBCRotas(): Single<List<BillCollectionsEntity>>
}