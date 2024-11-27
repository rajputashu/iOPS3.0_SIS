package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.SiteTypeEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public abstract class SiteTypeDao implements BaseDao<SiteTypeEntity> {

    @Query("SELECT * from SiteTypeEntity")
    public abstract Maybe<List<SiteTypeEntity>> fetchAll();

    //Region
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSiteType(List<SiteTypeEntity> list);

    @Query("DELETE FROM SiteTypeEntity")
    public abstract Completable deleteSiteType();
}
