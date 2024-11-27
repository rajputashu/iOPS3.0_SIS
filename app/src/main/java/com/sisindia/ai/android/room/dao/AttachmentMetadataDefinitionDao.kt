package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.AttachmentMetadataDefinitionEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 5/12/2020.
 */
@Dao
abstract class AttachmentMetadataDefinitionDao : BaseDao<AttachmentMetadataDefinitionEntity> {

    /*@Query("SELECT * from AttachmentMetadataDefinitionEntity")
    abstract fun fetchAIDetails(): Single<String>*/

    @Query("SELECT metadataJsonFormat from AttachmentMetadataDefinitionEntity where attachmentSourceTypeId=:sourceTypeId")
    abstract fun fetchMetaDataJsonFormat(sourceTypeId: Int): Single<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllMetadataDefinitions(list: List<AttachmentMetadataDefinitionEntity>): Single<List<Long>>


    @Query("DELETE FROM AttachmentMetadataDefinitionEntity")
    abstract fun deleteAttachmentMetadataDefinition(): Completable
}