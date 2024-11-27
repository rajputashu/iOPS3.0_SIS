package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.sisindia.ai.android.models.geolocation.GPSLocationMO
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.GeoLocationEntity
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 4/28/2020.
 */
@Dao
abstract class GeoLocationDao : BaseDao<GeoLocationEntity> {

//    @Query("select id as pingId, geoLatitude ||','|| geoLongitude as geoLocation,batteryPercentage,capturedDateTime from GeoLocationEntity where isSynced=0 LIMIT 50")
    @Query("select id as pingId, geoLatitude ||','|| geoLongitude as geoLocation,batteryPercentage,capturedDateTime from GeoLocationEntity where isSynced=0 order by id LIMIT 50")
    abstract fun fetchAllGeoPings(): Single<List<GPSLocationMO>>

    @Query("Delete from GeoLocationEntity where id in (:idList)")
    abstract fun deleteUploadedPings(idList: List<Int>): Single<Int>

}