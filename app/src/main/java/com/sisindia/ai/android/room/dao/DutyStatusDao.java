package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.DutyStatusEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class DutyStatusDao implements BaseDao<DutyStatusEntity> {

    @Query("SELECT * FROM DutyStatusEntity")
    public abstract Single<List<DutyStatusEntity>> fetchAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertDutyOnRecord(DutyStatusEntity item);

    @Query("UPDATE DutyStatusEntity SET dutyOffDateTime=:dutyOffDateTime,dutyOffLocation=:offLocation,isSync=:isSync WHERE id=(SELECT max(id) FROM DutyStatusEntity)")
    public abstract Single<Integer> updateDutyOffToLastRecord(String dutyOffDateTime, String offLocation, boolean isSync);

//    @Query("SELECT * FROM DutyStatusEntity WHERE isSync=:isSync")
    @Query("select * from DutyStatusEntity where isSync = :isSync and dutyOnDateTime > DATETIME('now', '-7 day')")
    public abstract Single<List<DutyStatusEntity>> fetchAllNotSyncDutyStatus(boolean isSync);

    @Query("SELECT * FROM DutyStatusEntity WHERE isSync=:isSync AND id=(SELECT max(id) FROM DutyStatusEntity)")
//    @Query("SELECT * FROM DutyStatusEntity WHERE isSync=:isSync AND id=90")
    public abstract Single<DutyStatusEntity> fetchNotSyncDutyStatus(boolean isSync);

    @Query("UPDATE DutyStatusEntity SET serverId=:serverId,isSync=:isSync WHERE localId=:localId")
    public abstract Single<Integer> updateOnSyncToServer(String localId, int serverId, boolean isSync);

    //    @Query("SELECT * FROM DutyStatusEntity WHERE dutyOnDateTime >= :start AND dutyOffDateTime <= :end")
    @Query("SELECT * FROM DutyStatusEntity WHERE dutyOnDateTime BETWEEN :start  AND :end OR dutyOffDateTime BETWEEN :start  AND :end ")
    public abstract Single<List<DutyStatusEntity>> fetchDutyStatusBetween(String start, String end);
}
