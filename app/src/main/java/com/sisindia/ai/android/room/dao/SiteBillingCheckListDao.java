package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.entities.SiteBillingCheckListEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class SiteBillingCheckListDao {

    @Query("DELETE FROM SiteBillingCheckListEntity")
    public abstract Completable deleteSiteBillingCheckList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllSiteBillingCheckList(List<SiteBillingCheckListEntity> list);
}
