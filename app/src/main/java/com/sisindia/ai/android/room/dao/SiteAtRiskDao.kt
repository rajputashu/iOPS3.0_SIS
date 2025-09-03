package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity
import com.sisindia.ai.android.room.entities.SiteRiskReasonsEntity
import io.reactivex.Completable
import io.reactivex.Maybe

/**
 * Created by Ashu Rajput on 4/6/2020.
 */
@Dao
abstract class SiteAtRiskDao : BaseDao<SiteAtRiskEntity> {

    @Query("SELECT * from SiteAtRiskEntity")
    abstract fun fetchAll(): Maybe<List<SiteAtRiskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllSiteAtRisk(list: List<SiteAtRiskEntity>): Maybe<List<Long>>

    @Query("DELETE FROM SiteAtRiskEntity")
    abstract fun deleteSiteAtRisk(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSiteRiskReason(entity: SiteRiskReasonsEntity): Maybe<Long>
}