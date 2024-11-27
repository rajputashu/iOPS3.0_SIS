package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.ActionPlanEntity;
import com.sisindia.ai.android.room.entities.ActionPlanMapEntity;
import com.sisindia.ai.android.uimodels.ActionPlanModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class ActionPlanDao implements BaseDao<ActionPlanEntity> {

    @Query("SELECT * from ActionPlanEntity")
    public abstract Maybe<List<ActionPlanEntity>> fetchAll();

    @Query("SELECT  * FROM ActionPlanEntity as ap INNER JOIN actionplanmapentity amp ON ap.id = amp.actionPlanId " +
            "WHERE amp.actionPlanMapTypeId = 1 AND amp.categoryId = :categoryId")
    public abstract Single<List<ActionPlanModel>> fetchAllForGrievance(int categoryId);

    @Query("SELECT * FROM ActionPlanEntity as ap " +
            "INNER JOIN ActionPlanMapEntity as amp on ap.id = amp.actionPlanId " +
            "WHERE amp.actionPlanMapTypeId = 2 AND amp.categoryId=:typeId AND amp.subCategoryId=:natureId")
    public abstract Single<List<ActionPlanModel>> fetchAllForComplaint(int typeId, int natureId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertActionPlanMaps(List<ActionPlanMapEntity> actionPlanMaps);

    //ActionPlan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllActionPlan(List<ActionPlanEntity> list);

    @Query("DELETE FROM ActionPlanEntity")
    public abstract Completable deleteActionPlan();

    @Query("DELETE FROM ActionPlanMapEntity")
    public abstract Completable deleteActionPlanMap();

}
