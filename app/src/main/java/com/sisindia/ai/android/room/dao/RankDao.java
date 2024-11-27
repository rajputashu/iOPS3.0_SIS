package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.RankEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class RankDao implements BaseDao<RankEntity> {

    @Query("SELECT * from RankEntity")
    public abstract Maybe<List<RankEntity>> fetchAll();

    //Rank
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllRanks(List<RankEntity> list);

    @Query("DELETE FROM RankEntity")
    public abstract Completable deleteRank();
}
