package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.models.poa.ClosePoaApiBodyMO
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.SiteRiskPoaEntity
import com.sisindia.ai.android.uimodels.collection.CollectionAttachmentMO
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 4/6/2020.
 */
@Dao
abstract class SiteRiskPoaDao : BaseDao<SiteRiskPoaEntity> {

    @Query("SELECT * from SiteRiskPoaEntity")
    abstract fun fetchAll(): Maybe<List<SiteRiskPoaEntity>>

    @Query("UPDATE SiteRiskPoaEntity SET closureProofAttachment=1, closureDateTime= :closureDate, poAStatus=:status,closingRemarks=:remarks WHERE id = :poaId")
    abstract fun updateOnClosingPOA(closureDate: String,
        remarks: String,
        status: Int,
        poaId: Int): Maybe<Int>

    @Query("SELECT id,siteRiskId,closureDateTime,closingRemarks,closureProofAttachment from SiteRiskPoaEntity where poAStatus=3 and isSynced=0")
    abstract fun fetchAllCompletedButNotSynced(): Maybe<List<ClosePoaApiBodyMO>>

    @Query("UPDATE SiteRiskPoaEntity SET isSynced=1 where id=:poaId")
    abstract fun updateSyncedStatus(poaId: Int): Maybe<Int>

    @Query("DELETE FROM SiteRiskPoaEntity")
    abstract fun deleteSiteRiskPoa(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllAtRiskPOA(list: List<SiteRiskPoaEntity>): Single<List<Long>>

    @Query("SELECT (:sourceTypeId||'_'||:siteId||'_'||:riskId||'_'||:poaId||'_'||:guid||'.jpg') AS fileName, ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||:siteId||'/'||:siteRiskId||'/'||:riskId AS storagePath FROM AIProfileEntity as ai")
    abstract fun getAttachmentDataForPoaClose(sourceTypeId: Int,
        siteId: Int,
        siteRiskId: Int,
        riskId: Int,
        poaId: Int,
        guid: String?): Single<CollectionAttachmentMO>
}