package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.room.entities.RegisterAttachmentEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class RegistersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllRegisterAttachments(List<RegisterAttachmentEntity> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertRegisterAttachments(RegisterAttachmentEntity item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertOrUpdateCheckedRegister(CheckedRegisterEntity obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllCheckedRegister(List<CheckedRegisterEntity> items);

    @Query("SELECT * FROM CheckedRegisterEntity WHERE siteId = :siteId AND postId = :postId AND taskId = :taskId ORDER BY registerTypeId")
    public abstract Single<List<CheckedRegisterEntity>> fetchAllRegistersBySitePostId(int siteId, int postId, int taskId);

    @Query("SELECT ra.*,a.localFilePath AS localPath FROM RegisterAttachmentEntity AS ra " +
            "INNER JOIN AttachmentEntity AS a ON a.id =ra.registerAttachmentId " +
            "WHERE ra.taskId = :taskId AND ra.siteId = :siteId AND ra.postId = :postId AND ra.registerTypeId = :registerTypeId")
    public abstract Single<List<RegisterAttachmentEntity>> fetAllRegistersByTaskSitePostRegisterId(int taskId, int siteId, int postId, int registerTypeId);

    @Query("SELECT * FROM RegisterAttachmentEntity AS ra " +
            "INNER JOIN AttachmentEntity AS a ON a.id =ra.registerAttachmentId " +
            "WHERE ra.taskId = :taskId AND ra.siteId = :siteId AND ra.postId = :postId AND ra.registerTypeId = :registerTypeId LIMIT 2")
    public abstract Single<List<RegisterAttachmentEntity>> fetchLastTwoRegisterDocuments(int taskId, int siteId, int postId, int registerTypeId);

    @Delete
    public abstract Single<Integer> deleteRegisterAttachment(RegisterAttachmentEntity entity);

    @Query("SELECT * from CheckedRegisterEntity WHERE  (isMissing = 1 OR noOfPages > 0) AND taskId = :taskId AND postId = :postId AND siteId = :siteId")
    public abstract Single<List<CheckedRegisterEntity>> fetchAllRegistersForPost(int taskId, int siteId, int postId);

    @Query("SELECT * FROM RegisterAttachmentEntity WHERE taskId = :taskId AND siteId = :siteId AND postId = :postId")
    public abstract Single<List<RegisterAttachmentEntity>> fetchAllRegisterAttachments(int taskId, int siteId, int postId);

    @Query("SELECT * FROM RegisterAttachmentEntity WHERE taskId = :taskId ")
    public abstract Single<List<RegisterAttachmentEntity>> fetchAllRegisterAttachmentsByTask(int taskId);
}
