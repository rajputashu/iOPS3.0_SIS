package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.EmployeeLeaveEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class EmployeeLeaveDao implements BaseDao<EmployeeLeaveEntity> {

    @Query("SELECT * from EmployeeLeaveEntity")
    public abstract Maybe<List<EmployeeLeaveEntity>> fetchAll();

    //EmployeeLeave
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllEmployeeLeave(List<EmployeeLeaveEntity> list);


    @Query("DELETE FROM EmployeeLeaveEntity")
    public abstract Completable deleteEmployeeLeave();

}
