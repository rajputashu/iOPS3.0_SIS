package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.DistrictsEntity
import com.sisindia.ai.android.room.entities.StatesEntity
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 1/18/2021.z
 */
@Dao
abstract class StateDistrictDao : BaseDao<StatesEntity> {

    @Query("SELECT * from StatesEntity")
    abstract fun fetchAllStates(): Single<List<StatesEntity>>

    @Query("SELECT * FROM DistrictsEntity")
    abstract fun fetchAllDistricts(): Single<List<DistrictsEntity>>

    @Query("SELECT * FROM DistrictsEntity where stateId=:stateId")
    abstract fun fetchDistrictsViaState(stateId: Int): Single<List<DistrictsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllDistricts(list: List<DistrictsEntity>): Single<List<Long>>
}