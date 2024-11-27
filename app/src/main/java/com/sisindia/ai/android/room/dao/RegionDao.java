package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.RegionEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class RegionDao implements BaseDao<RegionEntity> {

    @Query("SELECT * from RegionEntity")
    public abstract Maybe<List<RegionEntity>> fetchAll();

    //Region
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllRegion(List<RegionEntity> list);

    @Query("DELETE FROM RegionEntity")
    public abstract Completable deleteRegion();
}
