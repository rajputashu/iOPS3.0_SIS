package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.BranchEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public abstract class BranchDao implements BaseDao<BranchEntity> {

    @Query("SELECT * from BranchEntity")
    public abstract Maybe<List<BranchEntity>> fetchAll();

    //Branch
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllBranch(List<BranchEntity> list);


    @Query("DELETE FROM BranchEntity")
    public abstract Completable deleteBranch();

}
