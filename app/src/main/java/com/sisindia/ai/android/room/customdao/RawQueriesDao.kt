package com.sisindia.ai.android.room.customdao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sisindia.ai.android.room.entities.TaskEntity
import io.reactivex.Maybe

/**
 * Created by Ashu Rajput on 1/9/2021.
 */
@Dao
abstract class RawQueriesDao {

    @RawQuery(observedEntities = [TaskEntity::class])
    abstract fun getQueryResult(query: SupportSQLiteQuery): Maybe<TaskEntity>

    @RawQuery
    abstract fun deleteOrUpdateViaRawQuery(query: SupportSQLiteQuery): Maybe<Int>
}