package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.entities.BarrackStrengthEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public abstract class BarrackStrengthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllBarrackStrength(List<BarrackStrengthEntity> list);

    @Query("DELETE FROM BarrackStrengthEntity")
    public abstract Completable deleteBarrackStrength();

}
