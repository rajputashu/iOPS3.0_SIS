package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.NotificationDataEntity
import com.sisindia.ai.android.room.entities.NudgesMasterEntity
import com.sisindia.ai.android.room.entities.TaskTypeEntity
import io.reactivex.Completable
import io.reactivex.Single


/**
 * Created by Ashu Rajput on 1/18/2021.z
 */
@Dao
abstract class NotificationsDao : BaseDao<NotificationDataEntity> {

    @Query("SELECT * from NotificationDataEntity where isSynced=0")
//    @Query("SELECT * FROM NotificationDataEntity WHERE isSynced = 0 AND createdDateTime LIKE :datePattern")
//    abstract fun fetchAll(datePattern: String): Single<List<NotificationDataEntity>>
    abstract fun fetchAll(): Single<List<NotificationDataEntity>>

//    @Query("SELECT * FROM NotificationDataEntity WHERE isSynced = 0 AND createdDateTime LIKE :date")
    @Query("SELECT * FROM NotificationDataEntity")
    abstract fun fetchAllNudges(): Single<List<NotificationDataEntity>>

    //    @Query("SELECT * from NotificationDataEntity order by ids DESC limit 1")
    @Query("SELECT * from NotificationDataEntity where moduleName='NUDGES' and isSynced=0 order by ids DESC")
    abstract fun fetchNudge(): Single<List<NotificationDataEntity>>

    @Query("Update NotificationDataEntity set isSynced = 1 where notificationId=:notificationId")
    abstract fun updateNudge(notificationId: String): Single<Int>

    @Query("select isSynced from NotificationDataEntity where notificationId=:notificationId")
    abstract fun isTaskAlreadyCompleted(notificationId: String?): Single<Int>

    @Query("DELETE FROM NudgesMasterEntity")
    abstract fun deleteNudgesMaster(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllNudgesMaster(list: List<NudgesMasterEntity>): Single<List<Long>>

}