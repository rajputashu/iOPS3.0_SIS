package com.sisindia.ai.android.room.dao

import androidx.room.*
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.AIProfileEntity
import com.sisindia.ai.android.room.entities.CacheAIProfileEntity
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 4/7/2020.
 */
@Dao
abstract class AIProfileDao : BaseDao<CacheAIProfileEntity> {

    @Query("SELECT * from CacheAIProfileEntity")
    abstract fun fetchAIDetails(): Single<CacheAIProfileEntity>

    @Query("SELECT * from AIProfileEntity")
    abstract fun getAIProfile(): Single<AIProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAIDetails(aiProfileEntity: CacheAIProfileEntity): Maybe<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMasterAIDetails(aiProfileEntity: AIProfileEntity): Maybe<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateAIDetails(aiProfileEntity: CacheAIProfileEntity): Maybe<Int>

    //    @Query("SELECT aiId, altAddress, pinCode, latitude, longitude, altContactNo, createdDateTime, updatedDateTime from CacheAIProfileEntity where isSynced=0")
    @Query("SELECT * from CacheAIProfileEntity where isSynced=0")
    abstract fun fetchUnSyncedAIDetails(): Single<CacheAIProfileEntity>

    @Query("UPDATE CacheAIProfileEntity set isSynced=:syncStatus")
    abstract fun updateSyncStatus(syncStatus: Boolean = true): Single<Int>

    @Query("DELETE FROM AIProfileEntity")
    abstract fun deleteAiProfile(): Single<Int>
}