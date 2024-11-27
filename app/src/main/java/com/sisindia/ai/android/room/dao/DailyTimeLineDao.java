package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.DailyTimeLineEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class DailyTimeLineDao implements BaseDao<DailyTimeLineEntity> {

    @Query("SELECT * FROM DailyTimeLineEntity WHERE isSynced=:isSync")
    public abstract Single<List<DailyTimeLineEntity>> fetchAllNotSyncedItems(boolean isSync);

    @Query("UPDATE DailyTimeLineEntity SET serverId=:serverId, isSynced=:isSync WHERE localId=:localId")
    public abstract Single<Integer> updateItemOnServerSync(int serverId, String localId, boolean isSync);
}
