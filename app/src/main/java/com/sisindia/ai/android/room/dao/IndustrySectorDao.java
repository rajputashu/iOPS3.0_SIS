package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.IndustrySectorEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class IndustrySectorDao implements BaseDao<IndustrySectorEntity> {

    @Query("SELECT * from IndustrySectorEntity")
    public abstract Maybe<List<IndustrySectorEntity>> fetchAll();

    //IndustrySector
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllIndustrySector(List<IndustrySectorEntity> list);


    @Query("DELETE FROM IndustrySectorEntity")
    public abstract Completable deleteIndustrySector();
}
