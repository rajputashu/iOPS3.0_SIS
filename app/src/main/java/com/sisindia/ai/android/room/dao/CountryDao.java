package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.CountryEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class CountryDao implements BaseDao<CountryEntity> {

    @Query("SELECT * from CountryEntity")
    public abstract Maybe<List<CountryEntity>> fetchAll();

    //Country
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllCountry(List<CountryEntity> list);

    @Query("DELETE FROM CountryEntity")
    public abstract Completable deleteCountry();
}
