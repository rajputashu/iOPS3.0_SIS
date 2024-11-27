package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.entities.PostRegisterEntity;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class PostRegistersDao {

    @Query("DELETE FROM PostRegisterEntity")
    public abstract Completable deletePostRegister();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertPostRegister(PostRegisterEntity register);
}
