package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.DynamicFormEntity;
import com.sisindia.ai.android.room.entities.TaskTypeEntity;
import com.sisindia.ai.android.uimodels.TaskTypeModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class TaskTypeDao implements BaseDao<TaskTypeEntity> {

    @Query("SELECT * from TaskTypeEntity")
    public abstract Single<List<TaskTypeEntity>> fetchAll();

    @Query("SELECT * FROM TaskTypeEntity WHERE isActive =:isActive and (description!='MySIS' or description is null)")
    public abstract Single<List<TaskTypeModel>> fetchAllActiveTaskTypes(boolean isActive);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllTaskTypes(List<TaskTypeEntity> list);

    @Query("DELETE FROM TaskTypeEntity")
    public abstract Completable deleteTaskType();

    @Query("DELETE FROM DynamicFormEntity")
    public abstract Completable deleteDynamicForm();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllDynamicForms(List<DynamicFormEntity> list);
}
