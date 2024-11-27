package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.features.units.entity.PendingQRCountsMO;
import com.sisindia.ai.android.models.site.AddEditPostsMO;
import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.SitePostEntity;
import com.sisindia.ai.android.uimodels.ScanPostQRModel;
import com.sisindia.ai.android.uimodels.SitePostModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class SitePostDao implements BaseDao<SitePostEntity> {

    @Query("SELECT * from SitePostEntity")
    public abstract Maybe<List<SitePostEntity>> fetchAll();

    @Query("SELECT * from SitePostEntity WHERE siteId=:siteId and isActive=1")
    public abstract Single<List<SitePostModel>> fetchAllPostsBySiteId(int siteId);

    @Query("SELECT * FROM SitePostEntity WHERE id=:id LIMIT 1")
    public abstract Maybe<SitePostEntity> getItemByPrimaryKeyId(int id);

    //    @Query("select sum(case when length(dutypostcode) > 0 Then 0 else 1 end) as pendingQRCount, count(*) as totalPost from SitePostEntity where isActive=1") //Pending
    @Query("select sum(case when length(dutypostcode) > 0 Then 1 else 0 end) as qrCount, count(*) as totalPost from SitePostEntity where isActive=1")
    public abstract Maybe<PendingQRCountsMO> getPendingQRCounts();

    @Query("SELECT sp.*,sp.id AS id, site.siteAddress AS siteAddress " +
            "FROM SitePostEntity AS sp INNER JOIN SiteEntity AS site ON site.id=sp.siteId " +
            "WHERE siteId = :siteId AND sp.id NOT IN (SELECT postId FROM CheckedPostEntity WHERE checkedPostStatus =:status AND taskId=:taskId)")
    public abstract Single<List<SitePostModel>> fetchPendingPostsForCheckIn(int siteId, int taskId, int status);

    //    @Query("SELECT sp.id FROM SitePostEntity AS sp INNER JOIN SiteEntity AS site ON site.id=sp.siteId WHERE siteId = :siteId AND dutypostcode LIKE '%' || :qrCode || '%' and sp.id NOT IN (SELECT postId FROM CheckedPostEntity WHERE checkedPostStatus =:status AND taskId=:taskId)")
    @Query("SELECT sp.id, (Select Count(Id) from CheckedPostEntity where postId =sp.Id and checkedPostStatus =:status and taskid=:taskId) checkedPost FROM SitePostEntity AS sp INNER JOIN SiteEntity AS site ON site.id=sp.siteId WHERE siteId = :siteId AND dutypostcode LIKE '%' || :qrCode|| '%'")
    public abstract Single<ScanPostQRModel> fetchPendingPostViaQR(int siteId, int taskId, int status, String qrCode);

    @Query("UPDATE SitePostEntity set isSynced=0, isActive=0, isDelete=1, isCreated=1 where id=:postId")
    public abstract Single<Integer> updatePostToDelete(int postId);

    @Query("Select id,postName,dutyPostCode,description,qRenabled as qrEnabled,isActive,isMainPost,siteId,isCreated,postGeoPointString,spiEnabled from SitePostEntity where isSynced=0")
    public abstract Single<List<AddEditPostsMO>> fetchAllCompletedButNotSynced();

    @Query("UPDATE SitePostEntity SET isSynced=1 where id=:postId")
    public abstract Maybe<Integer> updateSyncedStatus(int postId);

    //SitePost
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllSitePost(List<SitePostEntity> list);

    @Query("DELETE FROM SitePostEntity")
    public abstract Completable deleteSitePost();

    @Query("select postName from SitePostEntity where siteId=:siteID and isDelete=0")
    public abstract Single<List<String>> isDuplicatePost(int siteID);

    @Query("select id from SitePostEntity where siteId=:siteID and isMainPost=1")
    public abstract Single<Integer> isMainPostExist(int siteID);

    @Query("UPDATE SitePostEntity SET isMainPost=0,isSynced = 0 where id=:postId")
    public abstract Maybe<Integer> replaceAndUpdateMainPost(int postId);

    @Query("UPDATE SitePostEntity set id=:serverId, isCreated=1,isSynced=1 where id=:tableId")
    public abstract Single<Integer> updateOnServerSync(int tableId, int serverId);

    @Query("select count(*) from sitepostEntity where siteid=:siteId and postName=:postName")
    public abstract Single<Integer> isPostNameExist(int siteId, String postName);

    @Query("select count(*) from sitepostEntity where dutyPostCode LIKE '%' || :scannedQRCode || '%'")
    public abstract Single<Integer> isDuplicateQRExist(String scannedQRCode);
}
