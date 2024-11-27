package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.SiteShiftEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class SiteShiftDao implements BaseDao<SiteShiftEntity> {

    @Query("SELECT * from SiteShiftEntity")
    public abstract Maybe<List<SiteShiftEntity>> fetchAll();

    //SiteShift
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllSiteShift(List<SiteShiftEntity> list);

    @Query("DELETE FROM SiteShiftEntity")
    public abstract Completable deleteSiteShift();
}
