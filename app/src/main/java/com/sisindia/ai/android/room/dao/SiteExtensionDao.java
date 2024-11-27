package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.SiteExtensionEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class SiteExtensionDao implements BaseDao<SiteExtensionEntity> {

    @Query("SELECT * from SiteExtensionEntity")
    public abstract Maybe<List<SiteExtensionEntity>> fetchAll();

    //SiteExtension
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertSiteExtension(SiteExtensionEntity obj);

    @Query("DELETE FROM SiteExtensionEntity")
    public abstract Completable deleteSiteExtension();
}
