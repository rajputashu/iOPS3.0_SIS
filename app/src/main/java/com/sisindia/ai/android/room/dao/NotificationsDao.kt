package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.NotificationDataEntity
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 1/18/2021.
 */
@Dao
abstract class NotificationsDao : BaseDao<NotificationDataEntity> {

    @Query("SELECT * from NotificationDataEntity")
    abstract fun fetchAll(): Single<List<NotificationDataEntity>>

}