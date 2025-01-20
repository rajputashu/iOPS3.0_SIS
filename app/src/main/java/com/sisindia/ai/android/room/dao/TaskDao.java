package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.models.practo.PractoCheckedGuardsMO;
import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.CheckInOutEntity;
import com.sisindia.ai.android.room.entities.PractoQuestionEntity;
import com.sisindia.ai.android.room.entities.RotaTasksEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.uimodels.AttachmentModel;
import com.sisindia.ai.android.uimodels.DayNightCheckModel;
import com.sisindia.ai.android.uimodels.PostCheckModel;
import com.sisindia.ai.android.uimodels.ReviewInformationModel;
import com.sisindia.ai.android.uimodels.TimeLineModel;
import com.sisindia.ai.android.uimodels.WeekRotaCompliance;
import com.sisindia.ai.android.uimodels.akr.AKRAttachmentMO;
import com.sisindia.ai.android.uimodels.attachments.StoragePathAddPostMO;
import com.sisindia.ai.android.uimodels.attachments.StoragePathEditPostsMO;
import com.sisindia.ai.android.uimodels.attachments.StoragePathTasksMO;
import com.sisindia.ai.android.uimodels.attachments.TaskStoragePathMO;
import com.sisindia.ai.android.uimodels.billsubmit.BillSubmissionCardDetailsMO;
import com.sisindia.ai.android.uimodels.billsubmit.BillSubmissionCountMO;
import com.sisindia.ai.android.uimodels.collection.CollectionAttachmentMO;
import com.sisindia.ai.android.uimodels.moninput.MonInputCardDetailMO;
import com.sisindia.ai.android.uimodels.moninput.MonInputCountMO;
import com.sisindia.ai.android.uimodels.tasks.TimeElapsedMO;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class TaskDao implements BaseDao<TaskEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAll(List<TaskEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllRotaTasks(List<RotaTasksEntity> list);

    @Query("DELETE FROM RotaTasksEntity")
    public abstract Single<Integer> deleteRotaTasks();

    @Query("SELECT * from TaskEntity")
    public abstract Single<List<TaskEntity>> fetchAll();

    @Query("SELECT count(*) FROM TaskEntity AS task JOIN SiteEntity AS site ON task.siteId = site.id LEFT JOIN BarrackEntity AS barrack ON barrack.id = task.barrackId Left JOIN RotaTasksEntity as rota on rota.siteTaskId=task.id INNER JOIN TaskTypeEntity AS type ON type.id = task.taskTypeId WHERE date(estimatedTaskExecutionStartDateTime)=date(:start) and taskStatus in (1,2) AND tasktypeid not in (5)")
    public abstract Single<Integer> getPendingCount(String start);

    @Query("SELECT * FROM TaskEntity WHERE id=:id LIMIT 1")
    public abstract Maybe<TaskEntity> getItemByPrimaryKeyId(int id);

    //    @Query("SELECT Count(*) From TaskEntity where siteId=:siteId and :estimatedStartDateTime  between estimatedTaskExecutionStartDateTime and estimatedTaskExecutionEndDateTime")
    @Query("select count(*) from TaskEntity where siteId= :siteId and strftime('%d-%m-%Y %H:%M',:estimatedStartDateTime) BETWEEN strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionStartDateTime) and strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionEndDateTime)")
    public abstract Single<Integer> checkSiteDuplicateTask(int siteId, String estimatedStartDateTime);

    //    @Query("SELECT Count(*) From TaskEntity where barrackId=:barrackId and :estimatedStartDateTime  between estimatedTaskExecutionStartDateTime and estimatedTaskExecutionEndDateTime")
    @Query("select count(*) from TaskEntity where barrackId=:barrackId and strftime('%d-%m-%Y %H:%M',:estimatedStartDateTime) BETWEEN strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionStartDateTime) and strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionEndDateTime)")
    public abstract Single<Integer> checkBarrackDuplicateTask(int barrackId, String estimatedStartDateTime);

    //    @Query("select count(*) from TaskEntity where siteId= :siteId and strftime('%d-%m-%Y %H:%M',:estimatedStartDateTime) BETWEEN strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionStartDateTime) and strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionEndDateTime)")
    @Query("select count(*) from TaskEntity where siteId= :siteId and strftime('%d-%m-%Y %H:%M',:estimatedStartDateTime) BETWEEN strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionStartDateTime) and strftime('%d-%m-%Y %H:%M',estimatedTaskExecutionEndDateTime) And taskTypeId not in (15, 16)")
    public abstract Single<Integer> checkOtherTypeDuplicateTask(int siteId, String estimatedStartDateTime);

    @Query("SELECT site.id AS siteId,type.name AS taskName, site.siteName,task.id AS taskId, task.tasktypeid, task.serverId AS serverId, task.taskStatus AS taskStatus, task.checkInStatus," +
            "(SELECT COUNT(id) FROM GrievanceEntity WHERE siteid=task.siteId AND grievanceStatus IN (1,2,3)) as pendingGrievances," +
            "(SELECT COUNT(id) FROM ComplaintEntity WHERE siteid=task.siteId AND status IN (1,2)) as pendingComplaints, task.estimatedTaskExecutionStartDatetime as taskDate " +
            "FROM TaskEntity AS task " +
            "INNER JOIN TaskTypeEntity AS type ON type.id = task.taskTypeId " +
            "INNER JOIN SiteEntity AS site ON site.id=task.siteId WHERE task.id=:taskId")
    public abstract Single<ReviewInformationModel> fetchReviewInformationByTaskId(int taskId);

    @Query("SELECT t.*,s.*, t.id AS taskId, " +
            "(SELECT COUNT(id) FROM SitePostEntity  WHERE siteId = t.siteId) AS availablePosts, " +
            "(SELECT COUNT(id) FROM SiteStrengthEntity  WHERE siteId = s.id) AS availableStrength, " +
            "(SELECT COUNT(id) FROM SiteCheckListEntity  WHERE siteId = s.id) AS availableSiteCheckList, " +
            "(SELECT COUNT(id) From CheckedPostEntity WHERE taskId = t.id AND checkedPostStatus = 2) As noOfCheckedPosts, " +
            "(SELECT Count(id) FROM CacheStrengthEntity WHERE taskId = t.id AND actualStrength IS NOT NULL ) AS noOfCheckedStrength, " +
            "(SELECT Count(id) FROM CheckedSiteCheckListEntity WHERE taskId = t.id AND isedited =1) AS noOfSiteCheckListDone, " +
            "(SELECT name FROM TaskTypeEntity WHERE id =t.taskTypeId) As taskName, " +
            "(SELECT taskStatus FROM CacheClientHandshakeEntity WHERE taskId = t.id ) As clientHandshakeIsDone " +
            "FROM TaskEntity t LEFT JOIN SiteEntity AS s ON s.id = t.siteId LEFT JOIN SitePostEntity AS sp ON sp.siteId = s.id   WHERE t.id = :taskId")
    public abstract Single<DayNightCheckModel> fetchStartDayCheckModelByTaskId(int taskId);

    @Query("SELECT t.*,s.*, sp.postName, (SELECT COUNT(id) FROM CheckedGuardEntity  WHERE taskId = t.id AND postId=sp.id AND checkedGuardStatus=2) AS noOfCheckedGuards,  " +
            "(SELECT COUNT(id) FROM PostCheckListEntity  WHERE siteId = s.id AND postId=sp.id) AS availablePostCheckList,  " +
            "(SELECT COUNT(id) from CheckedRegisterEntity WHERE  (isMissing=1 OR noOfPages > 0) AND taskId = t.id AND postId = sp.id AND siteId = s.id) AS noOfCheckedRegisters, " +
            "(SELECT COUNT(id) FROM PostRegisterEntity WHERE postId=sp.id) AS availableRegisters, " +
            "(SELECT Count(id) FROM CheckedPostCheckListEntity WHERE taskId = t.id AND isedited =1 AND postId=sp.id) AS postCheckListDone " +
            "FROM TaskEntity t LEFT JOIN SiteEntity AS s ON s.id = t.siteId LEFT JOIN SitePostEntity AS sp ON sp.siteId = s.id WHERE t.id = :taskId AND sp.id = :postId")
    public abstract Single<PostCheckModel> fetchPostCheckModel(int taskId, int postId);

    @Query("UPDATE TaskEntity SET actualTaskExecutionStartDateTime=:actualTaskExecutionStartDateTime, taskStatus=:taskStatus, isSynced=0 WHERE id=:taskId")
    public abstract Single<Integer> updateTaskOnStartDayCheck(int taskId, String actualTaskExecutionStartDateTime, int taskStatus);

    @Query("SELECT * FROM TaskEntity WHERE isSynced=:isSync")
    public abstract Single<List<TaskEntity>> fetchAllNotSyncedTask(boolean isSync);

    @Query("UPDATE TaskEntity SET serverId=:serverId, isSynced=:isSync WHERE localId=:localId")
    public abstract Single<Integer> updateTaskOnServerSync(int serverId, String localId, boolean isSync);

    @Query("SELECT * FROM TaskEntity AS task  WHERE task.id = :taskId")
    public abstract Single<TaskEntity> fetchOtherTaskDetails(int taskId);

    @Query("UPDATE TaskEntity SET actualTaskExecutionEndDateTime=:actualTaskEndDT, taskStatus=4, taskExecutionResult=:taskExecutionResult, taskGeoLocation=:localtion, isSynced=0 WHERE id=:taskId")
    public abstract Single<Integer> updateTaskOnFinish(int taskId, String actualTaskEndDT, String taskExecutionResult, String localtion);

    @Query("SELECT 'This Week' AS weekName, " +
            "(SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=1 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) <= DATE('now', 'weekday 0', '0 days') AND date(estimatedTaskExecutionStartDateTime)>= DATE('now', 'weekday 0', '-6 days') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null) ) AS addRota, " +
            "(SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=0 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) <= DATE('now', 'weekday 0', '0 days') AND date(estimatedTaskExecutionStartDateTime)>= DATE('now', 'weekday 0', '-6 days') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null) ) AS adhoc, " +
            "(SELECT COUNT(id) FROM TaskEntity WHERE taskStatus=4 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) <= DATE('now', 'weekday 0', '0 days') AND date(estimatedTaskExecutionStartDateTime)>= DATE('now', 'weekday 0', '-6 days') and otherTaskTypeLookUpIdentifier!=8) AS completed")
    public abstract Single<WeekRotaCompliance> fetchThisWeekRotaCompliance();

    @Query("SELECT 'Last Week' AS weekName, " +
            "(SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=1 AND tasktypeid !=5 and TaskStatus !=3 AND DATE(estimatedTaskExecutionStartDateTime) <= DATE('now', 'weekday 0', '-7 days') AND DATE(estimatedTaskExecutionStartDateTime) >= DATE('now', 'weekday 0', '-13 days') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null) ) AS addRota, " +
            "(SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=0 AND tasktypeid not in (5) and TaskStatus not in (3) AND DATE(estimatedTaskExecutionStartDateTime) <= DATE('now', 'weekday 0', '-7 days') AND DATE(estimatedTaskExecutionStartDateTime)>= DATE('now', 'weekday 0', '-13 days') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null) ) AS adhoc, " +
            "(SELECT COUNT(id) FROM TaskEntity WHERE taskStatus=4 AND tasktypeid not in (5) and TaskStatus not in (3) AND DATE(estimatedTaskExecutionStartDateTime) <= DATE('now', 'weekday 0', '-7 days') AND DATE(estimatedTaskExecutionStartDateTime)>= DATE('now', 'weekday 0', '-13 days') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null) ) AS completed")
    public abstract Single<WeekRotaCompliance> fetchLastWeekRotaCompliance();

    @Query("SELECT 'Today' AS weekName, (SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=1 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) = DATE('now','0 day') and ( otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null)) AS addRota, (SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=0 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) = DATE('now','0 day') and ( otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null)) AS adhoc, (SELECT COUNT(id) FROM TaskEntity WHERE taskStatus=4 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) = DATE('now','0 day') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null) ) AS completed")
    public abstract Single<WeekRotaCompliance> fetchTodayRotaCompliance();

    @Query("SELECT 'Yesterday' AS weekName, (SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=1 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) = DATE('now','-1 day') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null) ) AS addRota, (SELECT COUNT(id) FROM TaskEntity WHERE isAutoCreation=0 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) = DATE('now','-1 day') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null)) AS adhoc, (SELECT COUNT(id) FROM TaskEntity WHERE taskStatus=4 AND tasktypeid not in (5) and TaskStatus not in (3) AND date(estimatedTaskExecutionStartDateTime) = DATE('now','-1 day') and (otherTaskTypeLookUpIdentifier!=8 OR otherTaskTypeLookUpIdentifier is null)) AS completed")
    public abstract Single<WeekRotaCompliance> fetchYesterdayRotaCompliance();

    /*
     * QUERIES FOR ATTACHMENTS
     * 1."storagePath(BS)": "{zone}/{region}/{branch}/{site}/task/{taskId}/{attachmentSourceTypeId_siteId_taskId_uuid}.jpg"
     * 2."storagePath(ADD POST or SPI)": "{region}/{branch}/{site}/post/{attachmentSourceTypeId_siteId_uuid}.jpg"
     * 2."storagePath(EDIT POST or SPI)":
     * 2."storagePath(Barracks[Bed,Kit,Mess,Outside])":
     */
    @Query("SELECT zoneCode||'/'||regionCode||'/'||branchCode||'/'||s.siteCode||'/'||tte.name||'/'||t.serverId As storagePath, " +
            "t.siteId ,t.serverId as taskId from AIProfileEntity ai join SiteEntity s on s.branchId=ai.branchId " +
            "join TaskEntity t on s.id=t.siteId join TaskTypeEntity tte on tte.id=t.tasktypeid WHERE t.id=:taskId")
    public abstract Single<StoragePathTasksMO> getFilePathValuesViaTaskId(int taskId);

    @Query("SELECT zoneCode,regionCode,branchCode,s.siteCode,s.id as siteId from AIProfileEntity ai join SiteEntity s on s.branchId=ai.branchId left join SitePostEntity sp on s.id=sp.siteId WHERE s.id=:siteId group by sp.siteId")
    public abstract Single<StoragePathAddPostMO> getFilePathForAddPostAndSpi(int siteId);

    @Query("SELECT zoneCode||'/'||regionCode||'/'||branchCode||'/'||s.siteCode||'/'||sp.postName As storagePath, sp.siteId ,sp.Id as postId " +
            "from AIProfileEntity ai join SiteEntity s on s.branchId=ai.branchId " +
            "join SitePostEntity sp on s.id=sp.siteId where sp.siteId=:siteId and sp.id=:postId")
    public abstract Single<StoragePathEditPostsMO> getFilePathForEditPostAndSPI(int siteId, int postId);
//    public abstract Single<EditPostAndSpiStoragePath> getFilePathForEditPostAndSPI(int siteId, int postId);

    @Query("select ai.zoneCode,ai.regionCode,ai.branchCode,be.barrackCode, t.barrackId,t.id as taskId, tte.name as taskName, " +
            "t.siteId from TaskEntity t join TaskTypeEntity tte on tte.id=t.tasktypeid join barrackEntity be on be.id=t.barrackId " +
            "left join AIProfileEntity ai where t.id=:taskId")
    public abstract Single<TaskStoragePathMO> getFilePathForBarracksImages(int taskId);

    /*@Query("SELECT COUNT(id) FROM TaskEntity WHERE (estimatedTaskExecutionStartDateTime BETWEEN :startDateTime AND :endDateTime) OR (estimatedTaskExecutionEndDateTime BETWEEN :startDateTime AND :endDateTime)")
    public abstract Single<Integer> checkTimeBeforeTaskCreation(String startDateTime, String endDateTime);*/

    /*@Query("select count(*) from TaskEntity where siteId=:siteId and DATE(estimatedTaskExecutionStartDateTime) = DATE(:startDateTime) and tasktypeid=:taskTypeId")
    public abstract Single<Integer> isLocalTaskAlreadyExist(int siteId, int taskTypeId, String startDateTime);*/

    @Query("SELECT task.serverId as serverId, site.siteCode as siteCode, type.name as taskType, (:sourceType||'_'||site.id||'_'||:postId||'_'||task.serverId||'_'||:employeeId||'_'||:guid) AS fileName, " +
            "(ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||site.siteCode||'/'||type.name||'/'||task.serverId||'/'||:sourceType||'_'||site.id||'_'||:postId||'_'||task.serverId||'_'||:employeeId||'_'||:guid) AS storagePath " +
            "FROM TaskEntity as task LEFT JOIN AIProfileEntity as ai " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id=task.taskTypeId WHERE task.id= :taskId")
    public abstract Single<AttachmentModel> getAttachmentTypeForGuard(int sourceType, int employeeId, String guid, int taskId, int postId);

    @Query("SELECT (:sourceTypeId||'_'||:billOutputCenterId||'_'||:billId||'_'||:billMonth||'_'||:billYear||'_'||:guid||'.jpg') AS fileName, " +
            "ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||:billOutputCenterId||'/'||:billYear||'/'||:billMonth" +
            "||'/'||:billId AS storagePath FROM AIProfileEntity as ai")
    public abstract Single<CollectionAttachmentMO> getAttachmentTypeForBillCollection(int sourceTypeId, int billOutputCenterId, int billId,
                                                                                      int billMonth, int billYear, String guid);

    //-----------------Below query is used in Bill Submission Details Card Screen----------------------------//
    @Query("select (select count(t.id) from TaskEntity t join SiteEntity s on s.id=t.siteId where taskTypeId=5 and taskStatus!=4 and t.taskStatus!=4 and date(t.estimatedTaskExecutionStartDateTime)and date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and estimatedTaskExecutionEndDateTime<date()) as overdueCount, (select count(distinct(t.id)) from TaskEntity t join SiteEntity s on s.id=t.siteId where taskTypeId=5 and date(t.estimatedTaskExecutionStartDateTime)and date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and taskStatus not in (3,4)) as pendingCount, (select count(t.id) from TaskEntity t join SiteEntity s on s.id=t.siteId where taskTypeId=5 and date(t.estimatedTaskExecutionStartDateTime)and date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and taskStatus=4) as completedCount")
    public abstract Single<BillSubmissionCountMO> fetchBillSubmissionCount();

    //    @Query("select s.siteName,s.siteAddress,s.siteCode,s.id as siteId,t.id as taskId, 1 as isPending, Cast (round(julianday('now')-julianday(estimatedTaskExecutionEndDateTime))as INTEGER) overDueBy from TaskEntity t join SiteEntity s on s.id=t.siteId where taskTypeId=5 and t.taskStatus not in (3,4) and date(t.estimatedTaskExecutionStartDateTime) between date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day')")
    @Query("select t.isSynced, s.siteName,s.siteAddress,s.siteCode,s.id as siteId,t.id as taskId, 1 as isPending, Cast (round(julianday('now')-julianday(estimatedTaskExecutionEndDateTime))as INTEGER) overDueBy from TaskEntity t join SiteEntity s on s.id=t.siteId where taskTypeId=5 and t.taskStatus not in (3,4) and date(t.estimatedTaskExecutionStartDateTime) between date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day')")
    public abstract Single<List<BillSubmissionCardDetailsMO>> fetchPendingBillDetails();

    //    @Query("select s.siteName,s.siteAddress,s.siteCode,s.id as siteId,t.id as taskId, 0 as isPending, Cast (round(julianday('now')-julianday(estimatedTaskExecutionEndDateTime))as INTEGER) overDueBy from TaskEntity t join SiteEntity s on s.id=t.siteId where taskTypeId=5 and t.taskStatus=4 and date(t.estimatedTaskExecutionStartDateTime) between date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day')")
    @Query("select s.siteName,s.siteAddress,s.siteCode,s.id as siteId,t.id as taskId, 0 as isPending, t.isSynced, Cast (round(julianday('now')-julianday(estimatedTaskExecutionEndDateTime))as INTEGER) overDueBy,substr(t.actualtaskexecutionenddatetime,1,10) as submittedOnDate from TaskEntity t join SiteEntity s on s.id=t.siteId where taskTypeId=5 and t.taskStatus=4 and date(t.estimatedTaskExecutionStartDateTime) between date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day')")
    public abstract Single<List<BillSubmissionCardDetailsMO>> fetchCompletedBillDetails();
    //------------------------------------------END----------------------------------------------//

    //-----------------Below query is used in MON INPUT Details Card Screen----------------------------//
    @Query("select (select count(t.id) from TaskEntity t join SiteEntity s on s.id=t.siteId where otherTaskTypeLookUpIdentifier=8 and taskStatus!=4 and strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') " +
            "and date('now','start of month','+1 month','-1 day') and estimatedTaskExecutionEndDateTime<date()) as overdueCount, (select count(distinct(t.id)) from TaskEntity t join SiteEntity s on s.id=t.siteId where otherTaskTypeLookUpIdentifier=8 " +
            "and strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') and date('now','start of month','+1 month','-1 day') and taskStatus!=4) as pendingCount, " +
            "(select count(t.id) from TaskEntity t join SiteEntity s on s.id=t.siteId where otherTaskTypeLookUpIdentifier=8 and strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') " +
            "and date('now','start of month','+1 month','-1 day') and taskStatus=4) as completedCount")
    public abstract Single<MonInputCountMO> fetchMonInputCount();

    @Query("select s.siteName,s.siteAddress,s.siteCode,s.id as siteId,t.id as taskId, 1 as isPending, Cast (round(julianday('now')-julianday(estimatedTaskExecutionEndDateTime))as INTEGER) overDueBy " +
            "from TaskEntity t join SiteEntity s on s.id=t.siteId where otherTaskTypeLookUpIdentifier=8 and strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') " +
            "and date('now','start of month','+1 month','-1 day') and taskStatus!=4")
    public abstract Single<List<MonInputCardDetailMO>> fetchPendingMonInputDetails();

    @Query("select s.siteName,s.siteAddress,s.siteCode,s.id as siteId, substr(t.actualtaskexecutionenddatetime,1,10) as submittedOnDate, 0 as isPending, t.id as taskId from TaskEntity t " +
            "join SiteEntity s on t.siteId=s.id where otherTaskTypeLookUpIdentifier=8 and t.taskStatus=4 and strftime('%Y-%m-%d', estimatedTaskExecutionStartDateTime) BETWEEN date('now','start of month','0 month','0 day') " +
            "and date('now','start of month','+1 month','-1 day')")
    public abstract Single<List<MonInputCardDetailMO>> fetchCompletedMonInputDetails();

    //------------------------------------------END----------------------------------------------//
    /*@Query("SELECT t.serverId as taskId,t.siteId,zoneCode||'/'||regionCode||'/'||branchCode||'/'||s.siteCode||'/'||tte.name||'/'||t.serverId As storagePath, " +
            ":sourceTypeId||'_'||t.siteId ||'_'||t.serverId||'_'||:guid||'.jpg' as fileName from " +
            "AIProfileEntity ai join SiteEntity s on s.branchId=ai.branchId join TaskEntity t on s.id=t.siteId " +
            "join TaskTypeEntity tte on tte.id=t.tasktypeid WHERE t.id=:taskId")
    public abstract Single<OtherTaskAttachmentMO> getAttachmentTypeForOtherTask(int sourceTypeId, int taskId, String guid);*/

    //---------------------- For Add Security Risk ------------------------//
    //{attachmentSourceTypeId_siteId_postId_taskId_uuid} -> fileName
    //{zone}/{region}/{branch}/{site}/task/{taskId}/{Filename.jpg}" -> storagePath
    @Query("SELECT task.serverId as serverId,(:sourceType||'_'||site.id||'_'||:postId||'_'||task.serverId||'_'||:guid) AS fileName, " +
            "(ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||site.siteCode||'/'||type.name||'/'||task.serverId||'/'||:sourceType||'_'||site.id||'_'||:postId||'_'||task.serverId||'_'||:guid) AS storagePath " +
            "FROM TaskEntity as task LEFT JOIN AIProfileEntity as ai " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id=task.taskTypeId WHERE task.id= :taskId")
    public abstract Single<AttachmentModel> getAttachmentTypeForAddSecurityRisk(int sourceType, String guid, int taskId, int postId);

    /// {global-container}/{zone}/{region}/{branch}/{site}/task/{taskId}/{attachmentSourceTypeId_siteId_taskId_uuid}.jpg
    @Query("SELECT task.serverId as serverId,(:sourceType||'_'||site.id||'_'||task.serverId||'_'||:guid) AS fileName, " +
            "(ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||site.siteCode||'/'||type.name||'/'||task.serverId||'/'||:sourceType||'_'||site.id||'_'||task.serverId||'_'||:guid) AS storagePath " +
            "FROM TaskEntity as task LEFT JOIN AIProfileEntity as ai " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id=task.taskTypeId WHERE task.id= :taskId")
    public abstract Single<AttachmentModel> getAttachmentTypeForOther(int sourceType, String guid, int taskId);

    @Query("SELECT site.siteName AS siteName,type.name AS taskType, task.actualTaskExecutionStartDateTime AS taskStartDateTime, task.actualTaskExecutionEndDateTime AS taskEndDateTime,site.siteCode AS siteCode " +
            "FROM TaskEntity AS task " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id = task.taskTypeId" +
            " WHERE actualTaskExecutionEndDateTime BETWEEN :startTime  AND :endTime")
    public abstract Single<List<TimeLineModel.CompletedTask>> fetchAllCompletedTasksBetween(String startTime, String endTime);

    @Query("SELECT timeElapsed, lastElapsedTime FROM TaskEntity WHERE id = :taskId")
    public abstract Single<TimeElapsedMO> fetchTimeSpentViaTaskId(int taskId);

    @Query("UPDATE TaskEntity SET timeElapsed=:timeInMillis WHERE id = :taskId")
    public abstract Single<Integer> updateTimeSpentByTaskId(int taskId, long timeInMillis);

    @Query("UPDATE TaskEntity SET timeElapsed=:timeInMillis,lastElapsedTime=:lastTimeInSec WHERE id = :taskId")
    public abstract Single<Integer> updateTimeSpentByTaskId(int taskId, long timeInMillis, long lastTimeInSec);

    @Query("SELECT * from taskentity where taskTypeId=5 and isAutoCreation=1")
    public abstract Single<List<TaskEntity>> fetchAllAutoBSRotas();

    @Query("SELECT * from taskentity where taskTypeId=7 and isAutoCreation=1")
    public abstract Single<List<TaskEntity>> fetchAllAutoMonInputRotas();

    @Query("SELECT count(*) as count from TaskEntity where isSynced=0 and taskStatus=4")
    public abstract Single<Integer> fetchCountOfUnSyncedTasks();

    @Query("UPDATE TaskEntity SET taskStatus=:updatedTaskStatus WHERE id = :taskId")
    public abstract Single<Integer> updateTaskStatus(int updatedTaskStatus, int taskId);

    @Query("UPDATE TaskEntity SET approvedDateTime = :approvedDateTime, approvedBy = :approvedBy, taskStatus =:taskStatus WHERE serverId = :taskId")
    public abstract Single<Integer> updateAdHocTask(int taskId, String approvedDateTime, int approvedBy, int taskStatus);

    @Query("UPDATE TaskEntity SET checkInStatus=:status, checkInDateTime=:dateTime WHERE id = :taskId")
    public abstract Single<Integer> updateCheckInStatus(int status, String dateTime, int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertCheckInOutData(CheckInOutEntity entity);

    @Query("UPDATE CheckInOutEntity SET checkOutQrCode =:qrCode, checkOutSkipReason=:reason,checkOutDateTime=:dateTime WHERE taskId = :taskId")
    public abstract Single<Integer> updateCheckOutStatus(String qrCode, String reason, String dateTime, int taskId);

    //    @Query("select dutyPostCode from sitepostEntity where siteid=:siteId AND dutyPostCode IS NOT NULL AND dutyPostCode!= '' ")
    @Query("select dutyPostCode from sitepostEntity where siteid=:siteId AND dutyPostCode IS NOT NULL AND length(dutyPostCode)>0")
    public abstract Single<List<String>> getAllQRCodesAtSite(int siteId);

    @Query("Delete from TaskEntity where serverId in (:ids) and taskStatus!=4")
    public abstract Single<Integer> deleteInActiveRotas(List<Integer> ids);

    @Query("SELECT (:sourceType||'_'||:siteId||'_'||:employeeId||'_'||:guid) AS fileName, (ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||:siteId||'/'||'SiteDisbandment'||'/'||:sourceType||'_'||:siteId||'_'||:sourceType||'_'||:guid) AS storagePath FROM AIProfileEntity as ai")
    public abstract Maybe<AKRAttachmentMO> disbandmentAttachmentFile(int sourceType, int siteId, String employeeId, String guid);

    @Query("SELECT (:sourceType||'_'||:siteId||'_'||:employeeId||'_'||:guid) AS fileName, (ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||:siteId||'/'||'PractoApp'||'/'||:sourceType||'_'||:siteId||'_'||:sourceType||'_'||:guid) AS storagePath FROM AIProfileEntity as ai")
    public abstract Maybe<AKRAttachmentMO> practoAttachmentFile(int sourceType, int siteId, String employeeId, String guid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertAppTask(PractoQuestionEntity entity);

    //    @Query("select cg.employeeId,cg.employeeNo,cg.guardStatus,cg.siteId,cg.taskId, pqe.questionsjson from checkedGuardEntity cg inner join practoQuestionEntity pqe on cg.taskid=pqe.taskid where cg.taskid = :taskId group by cg.employeeId")
    @Query("select cg.employeeId,ese.employeeName,cg.employeeNo,cg.guardStatus,cg.siteId,cg.taskId, pqe.questionsjson from checkedGuardEntity cg inner join practoQuestionEntity pqe on cg.taskid=pqe.taskid and cg.employeeid=pqe.employeeid inner join EmployeeSiteEntity ese on ese.employeeId = cg.employeeId where cg.taskid = :taskId group by cg.employeeId")
    public abstract Single<List<PractoCheckedGuardsMO>> getAllPractoCheckedGuards(int taskId);

    //    @Query("Select Id from TaskEntity where TasktypeId=:taskTypeId and taskStatus = 2")
    @Query("Select Id from TaskEntity where TasktypeId=:taskTypeId and taskStatus = 2 and Date(estimatedTaskExecutionStartDateTime) = :date")
    public abstract Single<Integer> isAnyTaskAlreadyStarted(int taskTypeId, String date);
}