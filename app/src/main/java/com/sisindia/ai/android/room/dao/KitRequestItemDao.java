package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.KitRequestItemEntity;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class KitRequestItemDao implements BaseDao<KitRequestItemEntity> {

    @Query("SELECT * from KitRequestItemEntity")
    public abstract Maybe<List<KitRequestItemEntity>> fetchAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllKitRequestItems(List<KitRequestItemEntity> items);

    @Query("UPDATE KitRequestItemEntity SET updatedDateTime=:updatedDateTime,replaceStatus = 3 WHERE id IN (:ids)")
    public abstract Single<Integer> updateKitRequestedItems(String updatedDateTime, List<Integer> ids);

}
