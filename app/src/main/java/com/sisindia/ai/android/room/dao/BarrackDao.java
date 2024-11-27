package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.BarrackTaggingEntity;
import com.sisindia.ai.android.room.entities.CacheBarrackInspectionEntity;
import com.sisindia.ai.android.room.entities.SiteBarrackMapsEntity;
import com.sisindia.ai.android.uimodels.barracks.listing.BarrackListingMO;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class BarrackDao implements BaseDao<BarrackEntity> {

    @Query("SELECT * from BarrackEntity")
    public abstract Maybe<List<BarrackEntity>> fetchAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllBarrack(List<BarrackEntity> list);

    @Query("Select b.id,b.name,b.barrackCode,b.barrackAddress,b.srnumber,ifnull(bt.id,0) isTaggingDone ," +
            "(SELECT max(actualTaskExecutionEndDateTime) from TaskEntity where taskTypeId=3 and BarrackId=b.id) " +
            "as lastInspectionDate FROM BarrackEntity b left join BarrackTaggingEntity bt on bt.barrackId=b.id")
    public abstract Maybe<List<BarrackListingMO>> fetchBarrackListing();

    @Query("SELECT * from BarrackEntity where id=:barrackId")
    public abstract Single<BarrackEntity> fetchBarrackDetails(int barrackId);

    @Query("SELECT * from BarrackTaggingEntity where barrackId=:barrackId")
    public abstract Single<BarrackTaggingEntity> fetchBarrackTaggingDetails(int barrackId);

    //Below query is used to check whether any record is available against taskId
    @Query("SELECT COUNT(*) from CacheBarrackInspectionEntity where taskId=:taskId")
    public abstract Single<Integer> isBICacheAvailable(int taskId);

    //Below query is used {based on the result from 'isBICacheAvailable' query [if 0->executing below query]}
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertCacheBIData(CacheBarrackInspectionEntity obj);

    @Query("SELECT * from CacheBarrackInspectionEntity where taskId=:taskId")
    public abstract Single<CacheBarrackInspectionEntity> fetchCacheData(int taskId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Integer> updateBICache(CacheBarrackInspectionEntity obj);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Integer> updateBarrackTagging(BarrackTaggingEntity obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertBarrackTagging(BarrackTaggingEntity obj);


    @Query("DELETE FROM BarrackEntity")
    public abstract Completable deleteBarrack();


    @Query("DELETE FROM SiteBarrackMapsEntity")
    public abstract Completable deleteSiteBarrackMaps();

    //Site Barrack Mapping
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllSiteBarrackMaps(List<SiteBarrackMapsEntity> list);
}
