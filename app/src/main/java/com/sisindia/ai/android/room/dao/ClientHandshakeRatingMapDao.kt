package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.ClientHandshakeRatingMapsEntity
import io.reactivex.Maybe

/**
 * Created by Ashu Rajput on 4/14/2020.
 */

@Dao
abstract class ClientHandshakeRatingMapDao : BaseDao<ClientHandshakeRatingMapsEntity> {

    @Query("SELECT * from ClientHandshakeRatingMapsEntity")
    abstract fun fetchAll(): Maybe<List<ClientHandshakeRatingMapsEntity>>
}