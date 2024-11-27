package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.LanguageEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class LanguageDao implements BaseDao<LanguageEntity> {

    @Query("SELECT * from LanguageEntity")
    public abstract Single<List<LanguageEntity>> fetchAll();

    //Language
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllLanguage(List<LanguageEntity> list);

    @Query("DELETE FROM LanguageEntity")
    public abstract Completable deleteLanguage();

}
