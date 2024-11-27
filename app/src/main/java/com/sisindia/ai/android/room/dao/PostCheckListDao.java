package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.entities.PostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class PostCheckListDao {

    @Query("DELETE FROM PostCheckListEntity")
    public abstract Completable deletePostCheckList();

    @Query("DELETE FROM PostCheckListOptionEntity")
    public abstract Completable deletePostCheckListOption();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertPostCheckList(PostCheckListEntity postCheckList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertPostCheckListOption(PostCheckListOptionEntity option);

}
