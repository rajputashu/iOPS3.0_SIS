package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.HolidayEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class HolidayDao implements BaseDao<HolidayEntity> {

    @Query("SELECT * from HolidayEntity")
    public abstract Maybe<List<HolidayEntity>> fetchAll();

    //Holiday
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllHoliday(List<HolidayEntity> list);

    @Query("DELETE FROM HolidayEntity")
    public abstract Completable deleteHoliday();
}
