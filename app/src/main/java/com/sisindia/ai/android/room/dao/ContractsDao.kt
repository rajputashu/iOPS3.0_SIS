package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.ContractsEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 4/14/2020.
 */
@Dao
abstract class ContractsDao : BaseDao<ContractsEntity> {

    @Query("SELECT * from ContractsEntity")
    abstract fun fetchAll(): Maybe<List<ContractsEntity>>

    @Query("DELETE FROM ContractsEntity")
    abstract fun deleteContracts(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllContracts(list: List<ContractsEntity>): Single<List<Long>>
}