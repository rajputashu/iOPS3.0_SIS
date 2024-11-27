package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.OrganizationEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class OrganizationDao implements BaseDao<OrganizationEntity> {

    @Query("SELECT * from OrganizationEntity")
    public abstract Maybe<List<OrganizationEntity>> fetchAll();

    //Organization
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertOrganization(OrganizationEntity obj);


    @Query("DELETE FROM OrganizationEntity")
    public abstract Completable deleteOrganization();
}
