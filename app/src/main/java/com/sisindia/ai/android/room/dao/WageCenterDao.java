package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.WageCenterEntity;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public abstract class WageCenterDao implements BaseDao<WageCenterEntity> {

    @Query("SELECT * from WageCenterEntity")
    public abstract Maybe<List<WageCenterEntity>> fetchAll();
}
