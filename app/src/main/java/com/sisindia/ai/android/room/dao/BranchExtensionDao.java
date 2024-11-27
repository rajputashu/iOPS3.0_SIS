package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.BranchExtensionEntity;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public abstract class BranchExtensionDao implements BaseDao<BranchExtensionEntity> {

    @Query("SELECT * from BranchExtensionEntity")
    public abstract Maybe<List<BranchExtensionEntity>> fetchAll();
}
