package com.sisindia.ai.android.room;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;

public interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insert(T obj);

    /**
     * Insert an array of objects in the database.
     *
     * @param list the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<List<Long>> insertAll(List<T> list);

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Integer> update(T obj);

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    Maybe<Integer> delete(T obj);

}
