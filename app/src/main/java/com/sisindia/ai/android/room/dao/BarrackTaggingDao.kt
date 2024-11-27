package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.BarrackTaggingEntity
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 4/22/2020.
 */
@Dao
abstract class BarrackTaggingDao : BaseDao<BarrackTaggingEntity> {
    @Query("SELECT * from BarrackTaggingEntity")
    abstract fun fetchAll(): Single<List<BarrackTaggingEntity>>
}