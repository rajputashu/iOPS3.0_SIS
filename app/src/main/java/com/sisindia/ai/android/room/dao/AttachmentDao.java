package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.uimodels.AttachmentModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class AttachmentDao implements BaseDao<AttachmentEntity> {

//    @Query("SELECT * from AttachmentEntity where isAttachmentSync=0")
    @Query("SELECT * from AttachmentEntity where isAttachmentSync=0 and ifnull(length(attachmentmetadata), 0) != 0")
    public abstract Single<List<AttachmentEntity>> fetchAll();

    @Query("UPDATE AttachmentEntity SET isAttachmentSync= 1 WHERE id = :attachmentId")
    public abstract Maybe<Integer> updateIdOfUploadedFile(int attachmentId);

    @Query("UPDATE AttachmentEntity SET isSelfieApiDone = :status WHERE id = :attachmentId")
    public abstract Maybe<Integer> updateStatusOfAttachmentAPI(int attachmentId, boolean status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllKitRequestAttachments(List<AttachmentEntity> list);

    @Query("SELECT * FROM AttachmentEntity WHERE attachmentGuid=:guid LIMIT 1")
    public abstract Single<AttachmentEntity> fetchAttachmentByGuid(String guid);

    @Query("DELETE FROM AttachmentEntity WHERE id=:id")
    public abstract Completable deleteAttachmentRecord(int id);

    // -> /{global-container}/{zone}/{region}/{branch}/{site}/task/{taskId}/{attachmentSourceTypeId_siteId_postId_taskId_uuid}.jpg //Attachment
    @Query("SELECT task.serverId as serverId,(:sourceType||'_'||site.id||'_'||:postId||'_'||task.serverId||'_'||:guid) AS fileName, " +
            "(ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||site.siteCode||'/'||type.name||'/'||task.serverId||'/'||:sourceType||'_'||site.id||'_'||:postId||'_'||task.serverId||'_'||:guid) AS storagePath " +
            "FROM TaskEntity as task LEFT JOIN AIProfileEntity as ai " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id=task.taskTypeId WHERE task.id= :taskId")
    public abstract Single<AttachmentModel> getAttachmentTypeForRegister(int sourceType, String guid, int taskId, int postId);

    //    @Query("SELECT count(*) as count from AttachmentEntity where isAttachmentSync=0 and attachmentsourcetype > 0")
    @Query("SELECT count(*) as count from AttachmentEntity where isAttachmentSync=0 and attachmentsourcetype > 0 and attachmentmetadata is not null")
    public abstract Single<Integer> fetchCountOfUnSyncedAttachments();

    //    @Query("select count(*) from AttachmentEntity where isSelfieApiDone = 0 order by 1 desc")
    @Query("select * from AttachmentEntity where isSelfieApiDone = 0 and attachmentSourceType=101 order by 1 desc limit 1")
    public abstract Single<List<AttachmentEntity>> fetchAttachmentWhenApiFailed();
}
