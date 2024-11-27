package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.BillCenterEntity;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public abstract class BillCenterDao implements BaseDao<BillCenterEntity> {

    @Query("SELECT * from BillCenterEntity")
    public abstract Maybe<List<BillCenterEntity>> fetchAll();
}
