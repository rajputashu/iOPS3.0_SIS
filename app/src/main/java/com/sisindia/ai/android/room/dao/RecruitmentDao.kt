package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.sisindia.ai.android.models.recruit.RecruitmentApiBodyMO
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.AddRecruitmentEntity
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 5/7/2020.
 */
@Dao
abstract class RecruitmentDao : BaseDao<AddRecruitmentEntity> {

    @Query("select count(*) from AddRecruitmentEntity where recruitPhoneNumber=:phoneNo")
    abstract fun isMobileNumberExists(phoneNo: String?): Single<Int?>?

    /*@Query("SELECT * from AddRecruitmentEntity where isSynced = 0")
    abstract fun fetchUnSyncedRecruitmentDetails(): Single<AddRecruitmentEntity>*/

    @Query("SELECT recruitGuid,recruitName,dateOfBirth,recruitPhoneNumber,referredById,recruitStatus,referredDateTime, referredByEmployeeNo as aadharNo from AddRecruitmentEntity where isSynced = 0")
    abstract fun fetchUnSyncedRecruitmentDetails(): Single<List<RecruitmentApiBodyMO>>

    @Query("UPDATE AddRecruitmentEntity set isSynced=:syncStatus where recruitGuid=:recruitGuid")
    abstract fun updateSyncStatus(recruitGuid: String, syncStatus: Boolean = true): Single<Int>
}