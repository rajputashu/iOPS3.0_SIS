package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.DynamicFormEntity;
import com.sisindia.ai.android.uimodels.DynamicFormMO;

import io.reactivex.Maybe;

/**
 * Created by Ashu_Rajput on 6/26/2021.
 */
@Dao
public abstract class DynamicTaskDao implements BaseDao<DynamicFormEntity> {

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllDynamicTaskTypes(List<DynamicTaskTypeEntity> list);

    @Query("DELETE FROM DynamicTaskTypeEntity")
    public abstract Completable deleteDynamicTaskTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllDynamicForms(List<DynamicFormEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllTaskTypes(List<TaskTypeEntity> list);

    @Query("DELETE FROM DynamicFormEntity")
    public abstract Completable deleteDynamicForms();*/

    @Query("SELECT df.*,dt.name as taskName from DynamicFormEntity df inner join TaskTypeEntity dt on df.dynamicTaskTypeId=dt.id where dynamicTaskTypeId=:taskTypeId")
    public abstract Maybe<DynamicFormMO> fetchDynamicForm(int taskTypeId);

    /*@Query("SELECT * from NotificationDataEntity where ids=:notificationId")
    public abstract Maybe<NotificationDataEntity> fetchDynamicNudgesForm(int notificationId);*/

    //    @Query("SELECT * from NotificationDataEntity where notificationId =:id ORDER BY ids DESC LIMIT 1")
//    @Query("SELECT nme.jsonData from NotificationDataEntity nde join NudgesMasterEntity nme on nde.notificationMasterId=nme.id where notificationId = :id ORDER BY ids DESC LIMIT 1")
    @Query("SELECT jsonData from NudgesMasterEntity where id = :id")
    public abstract Maybe<String> fetchDynamicNudgesForm(String id);
}
