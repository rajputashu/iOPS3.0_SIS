package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.CompanyEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class CompanyDao implements BaseDao<CompanyEntity> {

    @Query("SELECT * from CompanyEntity")
    public abstract Maybe<List<CompanyEntity>> fetchAll();

    //Company
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertCompany(CompanyEntity obj);


    @Query("DELETE FROM CompanyEntity")
    public abstract Completable deleteCompany();
}
