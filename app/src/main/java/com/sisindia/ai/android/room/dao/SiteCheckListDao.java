package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.entities.SiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class SiteCheckListDao {

    @Query("DELETE FROM SiteCheckListEntity")
    public abstract Completable deleteSiteCheckList();

    @Query("DELETE FROM SiteCheckListOptionEntity")
    public abstract Completable deleteSiteCheckListOption();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertSiteCheckList(SiteCheckListEntity siteCheckList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertSiteChecklistOption(SiteCheckListOptionEntity option);

}
