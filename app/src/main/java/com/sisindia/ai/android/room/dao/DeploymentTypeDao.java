package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.DeploymentTypeEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class DeploymentTypeDao implements BaseDao<DeploymentTypeEntity> {

    @Query("SELECT * from DeploymentTypeEntity")
    public abstract Single<List<DeploymentTypeEntity>> fetchAll();

    //DeploymentType
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllDeploymentType(List<DeploymentTypeEntity> list);

    @Query("DELETE FROM DeploymentTypeEntity")
    public abstract Completable deleteDeploymentType();
}
