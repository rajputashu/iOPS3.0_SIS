package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.sisindia.ai.android.room.entities.CacheClientHandshakeEntity;
import com.sisindia.ai.android.room.entities.CheckInOutEntity;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.room.entities.CheckedPostEntity;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.PostRegisterEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;
import com.sisindia.ai.android.uimodels.AttachmentModel;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;
import com.sisindia.ai.android.uimodels.CheckedPostModel;
import com.sisindia.ai.android.uimodels.DayNightCheckData;
import com.sisindia.ai.android.uimodels.EmployeePostScanModel;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;
import com.sisindia.ai.android.uimodels.GuardSummaryModel;
import com.sisindia.ai.android.uimodels.PostValidationModel;
import com.sisindia.ai.android.uimodels.RotaTaskItemModel;
import com.sisindia.ai.android.uimodels.TaskValidationModel;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class DayCheckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertCheckedPost(CheckedPostEntity obj);

    /*@Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Integer> updateCheckedPost(CheckedPostEntity obj);*/

    /*@Query("SELECT * FROM CheckedPostEntity WHERE siteId=:siteId")
    public abstract Maybe<List<CheckedPostEntity>> fetchAllPostbySiteId(int siteId);*/

    /*@Query("SELECT * FROM CheckedPostEntity WHERE taskId=:taskId")
    public abstract Maybe<List<CheckedPostEntity>> fetchAllPostbyTaskId(int taskId);*/

    @Query("SELECT * FROM CheckedPostEntity WHERE postId=:postId AND siteId=:siteId AND taskId=:taskId LIMIT 1")
    public abstract Single<CheckedPostEntity> fetchPostById(int postId, int siteId, int taskId);

    /*@Query("SELECT * FROM CheckedPostEntity")
    public abstract Maybe<List<CheckedPostEntity>> fetchAllPosts();*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertCheckedGuard(CheckedGuardEntity obj);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Integer> updateCheckedGuard(CheckedGuardEntity obj);

    @Transaction
    @Query("UPDATE CheckedGuardEntity SET turnOutScore = :turnOutScore, totalTurnOut = :totalScore ,guardEvaluationResult = :evaluationRes, mlGuardEvaluationResult= :mlGuardEvaluationRes ,updatedDateTime = :updateDateTime, currentState =:currentState, guardEvaluationGuid =:guardGuid, isFakeGuardImage=:isFakeImage WHERE id = :id")
    public abstract Maybe<Integer> updateCheckedGuardV2(int turnOutScore, int totalScore, String evaluationRes, String mlGuardEvaluationRes, String updateDateTime, int id, int currentState, String guardGuid, boolean isFakeImage);

    /*@Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Integer> updateCheckedGuardV2(CheckedGuardEntity obj);*/

    /*@Transaction
    @Query("UPDATE CheckedGuardEntity SET guardEvaluationResult = :evaluationRes")
    public abstract Maybe<Integer> updateCheckedGuardV2(String evaluationRes);*/

    /*@Query("SELECT * FROM CheckedGuardEntity WHERE siteId=:siteId")
    public abstract Maybe<List<CheckedGuardEntity>> fetchAllGuardbySiteId(int siteId);

    @Query("SELECT * FROM CheckedGuardEntity WHERE taskId=:taskId")
    public abstract Maybe<List<CheckedGuardEntity>> fetchAllGuardbyTaskId(int taskId);

    @Query("SELECT * FROM CheckedGuardEntity WHERE postId=:postId")
    public abstract Maybe<List<CheckedGuardEntity>> fetchAllGuardbyPostId(int postId);*/

    @Query("SELECT * FROM CheckedGuardEntity WHERE postId=:postId AND taskId=:taskId AND siteId=:siteId AND employeeId=:employeeId LIMIT 1")
    public abstract Single<CheckedGuardEntity> fetchCheckedGuard(int taskId, int siteId, int postId, int employeeId);

//    @Query("SELECT type.*, site.*, task.*,task.id AS taskId, site.siteName as siteName, barrack.name AS barrackName, barrack.barrackCode as barrackCode, site.siteAddress as siteAddress, rota.estimatedTravelTime, rota.estimatedDistance, strftime('%M',CAST ((julianday(estimatedTaskExecutionEndDateTime) - julianday(estimatedTaskExecutionStartDateTime)) AS REAL),'12:00') estimatedTime, case WHEN task.taskTypeId=1 THEN case when se.dcMinGuard=0 then 1 ELSE ifnull(se.dcMinGuard,1)END WHEN task.taskTypeId=2 THEN case when se.NCMinGuard=0 then 1 ELSE ifnull(se.NCMinGuard,1) END end minGuard FROM TaskEntity AS task JOIN SiteEntity AS site ON task.siteId = site.id left join SiteExtensionEntity se on se.siteId=task.siteId LEFT JOIN BarrackEntity AS barrack ON barrack.id = task.barrackId Left JOIN RotaTasksEntity as rota on rota.siteTaskId=task.id INNER JOIN TaskTypeEntity AS type ON type.id = task.taskTypeId WHERE estimatedTaskExecutionStartDateTime LIKE '%' || :date || '%' AND task.tasktypeid not in (5) and task.taskStatus not in (3) Union All SELECT type.*, site.*, task.*,task.id AS taskId, site.siteName as siteName, barrack.name AS barrackName, barrack.barrackCode as barrackCode, site.siteAddress as siteAddress, rota.estimatedTravelTime, rota.estimatedDistance, strftime('%M',CAST ((julianday(estimatedTaskExecutionEndDateTime) - julianday(estimatedTaskExecutionStartDateTime)) AS REAL),'12:00') estimatedTime, case WHEN task.taskTypeId=1 THEN case when se.dcMinGuard=0 then 1 ELSE ifnull(se.dcMinGuard,1)END WHEN task.taskTypeId=2 THEN case when se.NCMinGuard=0 then 1 ELSE ifnull(se.NCMinGuard,1) END end minGuard FROM TaskEntity AS task left JOIN SiteEntity AS site ON task.siteId = site.id left join SiteExtensionEntity se on se.siteId=task.siteId LEFT JOIN BarrackEntity AS barrack ON barrack.id = task.barrackId Left JOIN RotaTasksEntity as rota on rota.siteTaskId=task.id INNER JOIN TaskTypeEntity AS type ON type.id = task.taskTypeId WHERE estimatedTaskExecutionStartDateTime LIKE '%' || :date || '%' AND task.tasktypeid =3 and task.taskStatus not in (3) ORDER BY task.taskTypeId, task.estimatedTaskExecutionStartDateTime")
    @Query("SELECT type.*, type.description as taskDescription, site.*, task.*,task.id AS taskId, site.siteName as siteName, barrack.name AS barrackName, barrack.barrackCode as barrackCode, site.siteAddress as siteAddress, rota.estimatedTravelTime, rota.estimatedDistance, strftime('%M',CAST ((julianday(estimatedTaskExecutionEndDateTime) - julianday(estimatedTaskExecutionStartDateTime)) AS REAL),'12:00') estimatedTime, case WHEN task.taskTypeId=1 THEN case when se.dcMinGuard=0 then 1 ELSE ifnull(se.dcMinGuard,1)END WHEN task.taskTypeId=2 THEN case when se.NCMinGuard=0 then 1 ELSE ifnull(se.NCMinGuard,1) END end minGuard FROM TaskEntity AS task JOIN SiteEntity AS site ON task.siteId = site.id left join SiteExtensionEntity se on se.siteId=task.siteId LEFT JOIN BarrackEntity AS barrack ON barrack.id = task.barrackId Left JOIN RotaTasksEntity as rota on rota.siteTaskId=task.id INNER JOIN TaskTypeEntity AS type ON type.id = task.taskTypeId WHERE estimatedTaskExecutionStartDateTime LIKE '%' || :date || '%' AND task.tasktypeid not in (5) and task.taskStatus not in (3) Union All SELECT type.*, type.description as taskDescription, site.*, task.*,task.id AS taskId, site.siteName as siteName, barrack.name AS barrackName, barrack.barrackCode as barrackCode, site.siteAddress as siteAddress, rota.estimatedTravelTime, rota.estimatedDistance, strftime('%M',CAST ((julianday(estimatedTaskExecutionEndDateTime) - julianday(estimatedTaskExecutionStartDateTime)) AS REAL),'12:00') estimatedTime, case WHEN task.taskTypeId=1 THEN case when se.dcMinGuard=0 then 1 ELSE ifnull(se.dcMinGuard,1)END WHEN task.taskTypeId=2 THEN case when se.NCMinGuard=0 then 1 ELSE ifnull(se.NCMinGuard,1) END end minGuard FROM TaskEntity AS task left JOIN SiteEntity AS site ON task.siteId = site.id left join SiteExtensionEntity se on se.siteId=task.siteId LEFT JOIN BarrackEntity AS barrack ON barrack.id = task.barrackId Left JOIN RotaTasksEntity as rota on rota.siteTaskId=task.id INNER JOIN TaskTypeEntity AS type ON type.id = task.taskTypeId WHERE estimatedTaskExecutionStartDateTime LIKE '%' || :date || '%' AND task.tasktypeid =3 and task.taskStatus not in (3) ORDER BY task.taskTypeId, task.estimatedTaskExecutionStartDateTime")
    public abstract Single<List<RotaTaskItemModel>> fetchRotaListByDate(String date);

    @Query("SELECT " +
            "(SELECT COUNT(employeeId) FROM CheckedGuardEntity WHERE postId=cp.postId AND taskId=cp.taskId AND checkedGuardStatus = 2 ) AS noOfGuards, " +
            "(SELECT COUNT(id) from CheckedRegisterEntity WHERE  (isMissing=1 OR noOfPages > 0) AND taskId = cp.taskId AND postId = cp.postId AND siteId = cp.siteId) AS noOfRegisters, " +
            "cp.postId,cp.taskId,cp.*,post.* FROM CheckedPostEntity AS cp " +
            "INNER JOIN SitePostEntity AS post ON cp.postId=post.id " +
            "LEFT JOIN CheckedGuardEntity AS cg ON cg.postId=cp.postId " +
            "LEFT JOIN CheckedRegisterEntity AS cr ON cp.postId=cr.postId " +
            "WHERE cp.siteId=:siteId AND cp.taskId=:taskId " +
            "GROUP BY post.id")
    public abstract Single<List<CheckedPostModel>> fetchCheckedPostBySite(int taskId, int siteId);

    @Query("SELECT * FROM EmployeeSiteEntity AS es " +
            "WHERE employeeId NOT IN (SELECT employeeId FROM CheckedGuardEntity WHERE taskId=:taskId)")
    public abstract Single<List<GuardSuggestionItem>> fetchAllGuardsBySiteId(int taskId);

    @Query("SELECT '' siteName, cg.guardStatus,cg.totalTurnOut,cg.turnOutScore,cg.guardStatus,cg.currentState,cg.checkedGuardStatus,cg.taskId,cg.siteId,cg.postId,es.employeeId,es.employeeName,es.employeeNo,es.phone,sp.postName,efr.rewardType, (SELECT COUNT(id) FROM GrievanceEntity WHERE employeeId=cg.employeeId AND taskId=cg.taskId) as grievances, (SELECT COUNT(id) FROM KitRequestItemEntity WHERE employeeId=cg.employeeId AND taskId = cg.taskId) as kitItems FROM CheckedGuardEntity AS cg  INNER JOIN EmployeeSiteEntity AS es ON es.employeeId = cg.employeeId INNER JOIN SitePostEntity AS sp ON sp.id = cg.postId LEFT JOIN EmployeeFineRewardEntity AS efr ON efr.employeeId = cg.employeeId LEFT JOIN GrievanceEntity AS gr ON gr.employeeId = cg.employeeId LEFT JOIN KitRequestItemEntity AS kr ON kr.employeeId = cg.employeeId WHERE cg.taskId = :taskId AND cg.postId = :postId GROUP BY cg.employeeId ORDER by cg.checkedDateTime DESC")
    public abstract Single<List<CheckedGuardItemModel>> fetchAllCheckedGuardByPostId(int postId, int taskId);

    //    @Query("SELECT cg.guardStatus, cg.totalTurnOut, cg.turnOutScore,cg.guardStatus,cg.currentState,cg.checkedGuardStatus,cg.taskId,cg.siteId,cg.postId, es.employeeId, es.employeeName,es.employeeNo, es.phone, 0 kitItems,0 grievances, 0 rewardType FROM CheckedGuardEntity AS cg INNER JOIN EmployeeSiteEntity AS es ON es.employeeId = cg.employeeId LEFT JOIN EmployeeFineRewardEntity AS efr ON efr.employeeId = cg.employeeId LEFT JOIN GrievanceEntity AS gr ON gr.employeeId = cg.employeeId LEFT JOIN KitRequestItemEntity AS kr ON kr.employeeId = cg.employeeId WHERE cg.taskId = :taskId GROUP BY cg.employeeId ORDER by cg.checkedDateTime DESC")
    @Query("SELECT cg.guardStatus, cg.totalTurnOut, cg.turnOutScore, cg.currentState, cg.checkedGuardStatus,cg.taskId,cg.siteId,cg.postId, es.employeeId, es.employeeName,es.employeeNo, es.phone, 0 kitItems,0 grievances, 0 rewardType,te.sitename FROM CheckedGuardEntity AS cg INNER JOIN EmployeeSiteEntity AS es ON es.employeeId = cg.employeeId LEFT JOIN TaskEntity AS te ON te.id = cg.taskId WHERE cg.taskId = :taskId GROUP BY cg.employeeId ORDER by cg.checkedDateTime DESC")
    public abstract Single<List<CheckedGuardItemModel>> fetchAllCheckedGuardByTaskId(int taskId);

    @Query("SELECT '' siteName, cg.guardStatus,cg.totalTurnOut,cg.turnOutScore,cg.guardStatus,cg.currentState," +
            "cg.checkedGuardStatus,cg.taskId,cg.siteId,cg.postId," +
            "es.employeeId,es.employeeName,es.employeeNo,es.phone,es.deployedDate," +
            "sp.postName,efr.rewardType, (SELECT COUNT(id) FROM GrievanceEntity WHERE employeeId=cg.employeeId AND taskId=cg.taskId) as grievances, (SELECT COUNT(id) FROM KitRequestItemEntity WHERE employeeId=cg.employeeId AND taskId = cg.taskId) as kitItems FROM CheckedGuardEntity AS cg  INNER JOIN EmployeeSiteEntity AS es ON es.employeeId = cg.employeeId INNER JOIN SitePostEntity AS sp ON sp.id = cg.postId LEFT JOIN EmployeeFineRewardEntity AS efr ON efr.employeeId = cg.employeeId LEFT JOIN GrievanceEntity AS gr ON gr.employeeId = cg.employeeId LEFT JOIN KitRequestItemEntity AS kr ON kr.employeeId = cg.employeeId WHERE cg.taskId = :taskId AND cg.siteId = :siteId AND cg.postId=:postId AND cg.employeeId=:employeeId GROUP BY cg.employeeId ORDER by cg.checkedDateTime DESC LIMIT 1")
    public abstract Single<CheckedGuardItemModel> fetchGuardDetailAfterScan(int taskId, int siteId, int postId, int employeeId);

    @Query("SELECT cg.turnOutScore as turnOutScore,cg.totalTurnOut as totalTurnOut," +
            "cg.checkedGuardStatus as checkedGuardStatus,cg.guardStatus as guardStatus," +
            "cg.sleepingGuardGuid as sleepingGuardGuid, cg.guardEvaluationGuid as guardEvaluationGuid," +
            "cg.guardSignatureGuid as guardSignatureGuid, cg.notAvailableStatus as notAvailableStatus, " +
            "cg.guardNotAvailableRemarks as guardNotAvailableRemarks, cg.guardEvaluationResult as guardEvaluationResult, " +
            "es.employeeId as employeeId,es.phone as phone, es.employeeName as employeeName, es.employeeNo as employeeNo," +
            " rf.ids as fineRewardId,rf.rewardType as rewardType, post.id as postId, site.id as siteId,task.id as taskId, " +
            "task.serverId as serverId, lu.displayName as displayName FROM CheckedGuardEntity as cg  " +
            "INNER JOIN TaskEntity AS task ON task.id=cg.taskId  " +
            "INNER JOIN SitePostEntity AS post ON post.id=cg.postId  " +
            "INNER JOIN SiteEntity AS site ON site.id=cg.siteId " +
            "INNER JOIN EmployeeSiteEntity AS es ON es.employeeId=cg.employeeId " +
            "LEFT JOIN EmployeeFineRewardEntity AS rf ON rf.ids= cg.fineRewardId " +
            "LEFT JOIN LookUpEntity AS lu ON lu.lookupIdentifier = rf.rewardLookupIdentifier AND lu.lookupTypeId = rf.rewardTypeId " +
            "WHERE cg.taskId=:taskId AND cg.siteId=:siteId AND cg.postId=:postId AND cg.employeeId=:employeeId LIMIT 1")
    public abstract Single<GuardSummaryModel> fetchGuardSummary(int taskId, int siteId, int postId, int employeeId);

    /*@Query("SELECT cg.guardStatus,cg.totalTurnOut,cg.turnOutScore,cg.guardStatus,cg.currentState,cg.checkedGuardStatus,cg.taskId,cg.siteId,cg.postId,es.employeeId,es.employeeName,es.employeeNo,es.phone,sp.postName,efr.rewardType, " +
            "(SELECT COUNT(id) FROM GrievanceEntity WHERE employeeId=cg.employeeId AND taskId=cg.taskId) as grievances, " +
            "(SELECT COUNT(id) FROM KitRequestItemEntity WHERE employeeId=cg.employeeId AND taskId = cg.taskId) as kitItems " +
            "FROM CheckedGuardEntity AS cg  INNER JOIN EmployeeSiteEntity AS es ON es.employeeId = cg.employeeId " +
            "INNER JOIN SitePostEntity AS sp ON sp.id = cg.postId LEFT JOIN EmployeeFineRewardEntity AS efr ON efr.employeeId = cg.employeeId " +
            "LEFT JOIN GrievanceEntity AS gr ON gr.employeeId = cg.employeeId " +
            "LEFT JOIN KitRequestItemEntity AS kr ON kr.employeeId = cg.employeeId " +
            "WHERE cg.taskId = :taskId AND cg.siteId = :siteId GROUP BY cg.employeeId")
    public abstract Single<List<CheckedGuardItemModel>> fetchAllGuardPostQR(int siteId, int taskId);*/

    //DAY CHECK DONE
    @Query("SELECT cp.postId as postId, sp.postName as postName, " +
            "(SELECT COUNT(id) FROM CheckedGuardEntity  WHERE postId = cp.postId AND taskId = cp.taskId AND checkedGuardStatus = 2) as guardsChecked, " +
            "(SELECT SUM(authorizedStrength) FROM SiteStrengthEntity WHERE postId = cp.postId) as totalGuards, " +
            "(SELECT COUNT(id) from CheckedRegisterEntity WHERE  (isMissing=1 OR noOfPages > 0) AND taskId = cp.taskId AND postId = cp.postId AND siteId = cp.siteId) AS registersChecked, " +
            "(SELECT COUNT(id) from PostRegisterEntity WHERE postId = cp.postId) as totalRegisters, " +
            "(SELECT Count(id) FROM CheckedPostCheckListEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND isEdited = 1) AS postCheckList, " +
            "(SELECT COUNT(id) FROM PostCheckListEntity where postId = cp.postId) as  totalCheckList " +
            "FROM CheckedPostEntity as cp INNER JOIN SitePostEntity AS sp ON sp.id = cp.postId WHERE  cp.taskId =:taskId")
    public abstract Single<List<DayNightCheckData.PostCheckData>> fetchAllPostsOnDayCheckDone(int taskId);

    //    @Query("SELECT es.employeeName, es.employeeId, es.employeeNo, efr.rewardFineValue, efr.rewardType, sp.id as postId, sp.postName,cg.postId, cg.siteId, cg.taskId, cg.guardStatus, cg.sleepingGuardGuid, cg.guardEvaluationGuid, cg.guardEvaluationResult,cg.guardNotAvailableRemarks,cg.guardSignatureGuid, cg.turnOutScore,cg.totalTurnOut, cg.notAvailableStatus FROM CheckedGuardEntity as cg INNER JOIN EmployeeSiteEntity as es ON cg.employeeNo = es.employeeNo INNER JOIN SitePostEntity as sp ON sp.id = cg.postId left JOIN EmployeeFineRewardEntity as efr ON (efr.employeeId = cg.employeeId or  efr.employeeNo = cg.employeeNo) AND efr.taskId = cg.serverId WHERE cg.taskId = :taskId and cg.checkedGuardStatus = 2")
    @Query("SELECT es.employeeName, es.employeeId, es.employeeNo, efr.rewardFineValue, efr.rewardType, sp.id as postId, sp.postName,cg.postId, cg.siteId, cg.taskId, cg.guardStatus, cg.sleepingGuardGuid, cg.guardEvaluationGuid, cg.guardEvaluationResult, cg.mlGuardEvaluationResult,cg.guardNotAvailableRemarks,cg.guardSignatureGuid, cg.turnOutScore,cg.totalTurnOut, cg.notAvailableStatus, cg.isFakeGuardImage FROM CheckedGuardEntity as cg INNER JOIN EmployeeSiteEntity as es ON cg.employeeNo = es.employeeNo INNER JOIN SitePostEntity as sp ON sp.id = cg.postId left JOIN EmployeeFineRewardEntity as efr ON (efr.employeeId = cg.employeeId or  efr.employeeNo = cg.employeeNo) AND efr.taskId = cg.serverId WHERE cg.taskId = :taskId and cg.checkedGuardStatus = 2")
    public abstract Single<List<DayNightCheckData.GuardCheckData>> fetchAllGuardsOnDayCheckDone(int taskId);

    @Query("SELECT * FROM CheckedRegisterEntity WHERE taskId=:taskId")
    public abstract Single<List<DayNightCheckData.RegisterCheckData>> fetchAllCheckedRegisters(int taskId);

    @Query("SELECT * FROM CheckInOutEntity WHERE taskId=:taskId")
    public abstract Single<CheckInOutEntity> fetchCheckInOutData(int taskId);

    /*@Query("SELECT * FROM RegisterAttachmentEntity WHERE taskId=:taskId")
    public abstract Single<List<DayNightCheckData.RegisterAttachmentCheckData>> fetchAllRegisterAttachmentCheckData(int taskId);*/

    @Query("SELECT * FROM AddSecurityRiskEntity AS asr " +
            "INNER JOIN LookUpEntity AS lu ON asr.categoryId = lu.id WHERE taskId=:taskId")
    public abstract Single<List<DayNightCheckData.SecurityCheckData>> fetchAllSecurityCheckData(int taskId);

    @Query("SELECT * FROM CheckedPostCheckListEntity WHERE taskId=:taskId")
    public abstract Single<List<DayNightCheckData.PostCheckListData>> fetchAllPostCheckListData(int taskId);

    @Query("SELECT * FROM CheckedSiteCheckListEntity WHERE taskId=:taskId")
    public abstract Single<List<DayNightCheckData.SiteCheckListData>> fetchAllSiteCheckListData(int taskId);

    @Query("SELECT * FROM CacheStrengthEntity WHERE taskId=:taskId")
    public abstract Single<List<DayNightCheckData.SiteStrengthData>> fetchAllSiteStrengthData(int taskId);

    //    @Query("SELECT * FROM CacheClientHandshakeEntity WHERE taskId=:taskId")
    @Query("SELECT ifnull(id,0),* FROM CacheClientHandshakeEntity WHERE taskId=:taskId")
    public abstract Single<DayNightCheckData.ClientHandShakeData> fetchClientHandShakeData(int taskId);

    @Query("SELECT * FROM SiteCheckListEntity WHERE siteId = :siteId")
    public abstract Single<List<SiteCheckListEntity>> fetchAllSiteCheckList(int siteId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Single<Long> insertSiteCheckedList(CheckedSiteCheckListEntity entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Integer> updateSiteCheckedList(CheckedSiteCheckListEntity item);

    @Query("SELECT csc.*, a.localFilePath AS imageUri FROM CheckedSiteCheckListEntity AS csc " +
            "LEFT JOIN AttachmentEntity AS a ON csc.imageAttachmentId = a.attachmentGuid " +
            "WHERE csc.taskId=:taskId AND csc.siteId=:siteId")
    public abstract Single<List<CheckedSiteCheckListEntity>> fetchAllCheckedSiteCheckListByTaskAndSite(int taskId, int siteId);


    @Query("SELECT csc.*, a.localFilePath AS imageUri FROM CheckedPostCheckListEntity AS csc " +
            "LEFT JOIN AttachmentEntity AS a ON csc.imageAttachmentId = a.id " +
            "WHERE csc.taskId=:taskId AND csc.siteId=:siteId AND postId=:postid")
    public abstract Single<List<CheckedPostCheckListEntity>> fetchAllCheckedPostCheckListByTaskAndSite(int taskId, int siteId, int postid);

    @Query("SELECT * FROM SiteCheckListOptionEntity WHERE siteId=:siteId AND siteChecklistId=:siteChecklistId")
    public abstract Single<List<SiteCheckListOptionEntity>> fetchAllSiteCheckListOptionsFor(int siteId, int siteChecklistId);

    @Query("SELECT * FROM PostCheckListEntity WHERE siteId = :siteId")
    public abstract Single<List<PostCheckListEntity>> fetchAllPostCheckList(int siteId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Single<Long> insertCheckedPostCheckListItem(CheckedPostCheckListEntity entity);

    /*@Query("SELECT * FROM SiteEntity WHERE id=:siteId")
    public abstract Single<SiteEntity> fetchSiteById(int siteId);

    @Query("SELECT * FROM SitePostEntity WHERE siteId=:siteId")
    public abstract Single<List<SitePostEntity>> fetchAllPostsBySiteId(int siteId);*/

    @Query("SELECT * FROM PostCheckListOptionEntity WHERE siteId=:siteId AND postChecklistId=:postChecklistId AND postId=:postId")
    public abstract Single<List<PostCheckListOptionEntity>> fetchAllPostCheckListOptionsFor(int siteId, int postChecklistId, int postId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Integer> updatePostCheckedList(CheckedPostCheckListEntity item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertClientHandshake(CacheClientHandshakeEntity obj);

    @Query("SELECT * FROM CacheClientHandshakeEntity WHERE taskId=:taskId")
    public abstract Single<CacheClientHandshakeEntity> isCacheClientHandshake(int taskId);

    @Query("UPDATE CacheClientHandshakeEntity set isMetClient = 0, taskStatus=2, updatedDateTime=:updatedDateTime, reason=:reason  where taskId=:taskId")
    public abstract Maybe<Integer> updateClientHandshakeTaskDetails(int taskId, String reason, String updatedDateTime);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Integer> updateClientHandshakeTaskDetails(CacheClientHandshakeEntity obj);

    @Query("UPDATE CacheClientHandshakeEntity set taskStatus=:statusType where taskId=:taskId")
    public abstract Single<Integer> updateHandshakeTaskStatusOnComplete(int taskId, int statusType);

    @Query("SELECT count(*)  FROM CheckedSiteCheckListEntity WHERE taskId = :taskId")
    public abstract Single<Integer> isCheckedSiteListPresent(int taskId);

    @Query("SELECT count(*)  FROM CheckedPostCheckListEntity WHERE taskId = :taskId")
    public abstract Single<Integer> isCheckedPostListPresent(int taskId);

    @Query("SELECT * FROM PostRegisterEntity WHERE siteId = :siteId")
    public abstract Single<List<PostRegisterEntity>> fetchAllPostRegisterList(int siteId);

    @Query("SELECT count(*)  FROM CheckedRegisterEntity WHERE taskId = :taskId")
    public abstract Single<Integer> isCheckedRegisterPresent(int taskId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Single<Long> insertCheckedRegister(CheckedRegisterEntity entity);

    @Query("SELECT cp.id AS checkedPostId, (SELECT COUNT(id) FROM CheckedGuardEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND checkedGuardStatus=2) AS countOfCheckedGuard, " +
            "(SELECT Count(id) FROM CheckedRegisterEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND isMandatory=1)AS totalRegisters, " +
            "(SELECT Count(id) FROM CheckedRegisterEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND isMandatory=1 AND (isMissing=1 OR noOfPages>0)) AS countOfCheckedRegister, " +
            "(SELECT Count(id) FROM CheckedPostCheckListEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId) AS totalPostCheckList, " +
            "(SELECT Count(id) FROM CheckedPostCheckListEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND isEdited = 1) AS editedPostCheckList, case when t.taskTypeId=1 then " +
            "(SELECT Case when dcMinGuard=0 then 1 else ifnull(dcMinGuard,1) end From SiteExtensionEntity where siteid=cp.siteId) when t.taskTypeId=2 then " +
            "(SELECT Case when ncMinGuard=0 then 1 else ifnull(ncMinGuard,1) end From SiteExtensionEntity where siteid=cp.siteId) ELSE 0 END minGuardCheckCount FROM CheckedPostEntity AS cp left join TaskEntity t on t.id=cp.taskId WHERE cp.postId = :postId AND cp.taskId = :taskId AND cp.siteId = :siteId")
//    @Query("SELECT cp.id AS checkedPostId, (SELECT COUNT(id) FROM CheckedGuardEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND checkedGuardStatus=2) AS countOfCheckedGuard, (SELECT COUNT(id) FROM CheckedPostEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND checkedPostStatus=2) AS countOfCheckedPost, (SELECT Count(id) FROM CheckedRegisterEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND isMandatory=1)AS totalRegisters, (SELECT Count(id) FROM CheckedRegisterEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND isMandatory=1 AND (isMissing=1 OR noOfPages>0)) AS countOfCheckedRegister, (SELECT Count(id) FROM CheckedPostCheckListEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId) AS totalPostCheckList, (SELECT Count(id) FROM CheckedPostCheckListEntity WHERE postId=cp.postId AND siteid=cp.siteId AND taskid=cp.taskId AND isEdited = 1) AS editedPostCheckList, case when t.taskTypeId=1 then (SELECT Case when dcMinGuard=0 then 1 else ifnull(dcMinGuard,1) end From SiteExtensionEntity where siteid=cp.siteId) when t.taskTypeId=2 then (SELECT Case when ncMinGuard=0 then 1 else ifnull(ncMinGuard,1) end From SiteExtensionEntity where siteid=cp.siteId) ELSE 0 END minGuardCheckCount, case when t.taskTypeId=1 then (SELECT Case when dcMinPostCheck=0 then 1 else ifnull(dcMinPostCheck,1) end From SiteExtensionEntity where siteid=cp.siteId) when t.taskTypeId=2 then (SELECT Case when ncMinPostCheck=0 then 1 else ifnull(ncMinPostCheck,1) end From SiteExtensionEntity where siteid=cp.siteId) ELSE 0 END minPostCheckCount FROM CheckedPostEntity AS cp left join TaskEntity t on t.id=cp.taskId WHERE cp.postId = :postId AND cp.taskId = :taskId AND cp.siteId = :siteId")
    public abstract Single<PostValidationModel> validatePostOnCompleted(int taskId, int siteId, int postId);

    @Query("UPDATE CheckedPostEntity SET checkedPostStatus=:status WHERE id=:id")
    public abstract Single<Integer> updateCheckedPostStatus(int id, int status);

    /*@Query("SELECT task.id AS taskId, task.taskTypeId AS taskType, (SELECT Count(postId) FROM CheckedPostEntity WHERE taskId=task.id AND siteid=task.siteId AND checkedPostStatus=2) posts, " +
            "(SELECT Count(id) FROM CheckedSiteCheckListEntity WHERE siteId=task.siteId AND taskId=task.id) totalSiteCheckList, " +
            "(SELECT Count(id) FROM CheckedSiteCheckListEntity WHERE siteId=task.siteId AND taskId=task.id AND isEdited = 1) totalEdited, " +
            "(SELECT Count(id) FROM CacheStrengthEntity WHERE taskId = task.id AND actualStrength IS NULL) AS isPendingStrength, " +
            "(SELECT taskStatus FROM CacheClientHandshakeEntity WHERE taskId = task.id) clientHandShakeStatus, " +
            "(Select Count(*) from CheckedPostEntity where taskId=id ) CheckedPost, Case When taskTypeId=1 then (Select Case when dcMinPostCheck=0 then 1 else ifnull(dcMinPostCheck,1) end from SiteExtensionEntity where siteId=siteId) When taskTypeId=2 THEN " +
            "(Select Case when ncMinPostCheck=0 then 1 else ifnull(ncMinPostCheck,1) end from SiteExtensionEntity where siteId=siteId) ELSE 0 END minPostCheckCount, Case When taskTypeId=1 then " +
            "(Select Case when dcMinGuard=0 then 1 else ifnull(dcMinGuard,1) end from SiteExtensionEntity where siteId=siteId) When taskTypeId=2 THEN " +
            "(Select Case when ncMinGuard=0 then 1 else ifnull(ncMinGuard,1) end from SiteExtensionEntity where siteId=siteId) ELSE 0 END minGuardCheckCount, (SELECT COUNT(id) FROM CheckedGuardEntity WHERE taskid=task.id and siteId=task.siteId AND checkedGuardStatus=2) AS checkedGuardCount FROM TaskEntity AS task WHERE task.id =:taskId")*/
    @Query("SELECT task.id AS taskId, task.taskTypeId AS taskType, (SELECT Count(postId) FROM CheckedPostEntity WHERE taskId=task.id AND siteid=task.siteId AND checkedPostStatus=2) posts, (SELECT Count(id) FROM CheckedSiteCheckListEntity WHERE siteId=task.siteId AND taskId=task.id) totalSiteCheckList, (SELECT Count(id) FROM CheckedSiteCheckListEntity WHERE siteId=task.siteId AND taskId=task.id AND isEdited = 1) totalEdited, (SELECT Count(id) FROM CacheStrengthEntity WHERE taskId = task.id AND actualStrength IS NULL) AS isPendingStrength, (SELECT taskStatus FROM CacheClientHandshakeEntity WHERE taskId = task.id) clientHandShakeStatus, (Select Count(*) from CheckedPostEntity where taskId=task.id ) CheckedPost, Case When taskTypeId=1 then (Select Case when dcMinPostCheck=0 then 1 else ifnull(dcMinPostCheck,1) end from SiteExtensionEntity where siteId=task.siteId) When taskTypeId=2 THEN (Select Case when ncMinPostCheck=0 then 1 else ifnull(ncMinPostCheck,1) end from SiteExtensionEntity where siteId=task.siteId) ELSE 0 END minPostCheckCount, Case When taskTypeId=1 then (Select Case when dcMinGuard=0 then 1 else ifnull(dcMinGuard,1) end from SiteExtensionEntity where siteId=task.siteId) When taskTypeId=2 THEN (Select Case when ncMinGuard=0 then 1 else ifnull(ncMinGuard,1) end from SiteExtensionEntity where siteId=task.siteId) ELSE 0 END minGuardCheckCount, (SELECT COUNT(id) FROM CheckedGuardEntity WHERE taskid=task.id and siteId=task.siteId AND checkedGuardStatus=2) AS checkedGuardCount FROM TaskEntity AS task WHERE task.id = :taskId")
    public abstract Single<TaskValidationModel> validateTaskCompleted(int taskId);

    @Query("SELECT COUNT(id) FROM CheckedGuardEntity WHERE taskId=:taskId AND employeeId=:employeeId")
    public abstract Single<Integer> isGuardAlreadyCheckInTask(int taskId, int employeeId);

    // Commented below line {if causing any issue : uncomment it}
//    @Query("SELECT *, es.employeeName as employeeName,es.phone as phone, es.employeeNo as employeeNo, " +
    @Query("SELECT  es.employeeName as employeeName,es.phone as phone, es.employeeNo as employeeNo, " +
            "(SELECT SUM(rewardFineValue) FROM EmployeeFineRewardEntity WHERE employeeId = cg.employeeId AND rewardType=1) as totalReward, " +
            "(SELECT SUM(rewardFineValue) FROM EmployeeFineRewardEntity WHERE employeeId = cg.employeeId AND rewardType=2) as totalFine " +
            "FROM CheckedGuardEntity AS cg INNER JOIN EmployeeSiteEntity AS es on es.employeeId=cg.employeeId " +
            "WHERE cg.employeeId=:employeeId AND cg.siteId =:siteId AND cg.postId=:postId AND cg.taskId=:taskId")
    public abstract Single<EmployeePostScanModel> fetchEmployeePostScan(int employeeId, int taskId, int siteId, int postId);

    //---------------------- For SITE CHECK LIST ------------------------//
    //{attachmentSourceTypeId_siteId_taskId_uuid} -> fileName
    //{zone}/{region}/{branch}/{site}/task/{taskId}/{fileName.jpg} -> storagePath
    @Query("SELECT task.serverId as serverId,(:sourceType||'_'||site.id||'_'||task.serverId||'_'||:guid) AS fileName, " +
            "(ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||site.siteCode||'/'||type.name||'/'||task.serverId||'/'||:sourceType||'_'||site.id||'_'||task.serverId||'_'||:guid) AS storagePath " +
            "FROM TaskEntity as task LEFT JOIN AIProfileEntity as ai " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id=task.taskTypeId WHERE task.id= :taskId")
    public abstract Single<AttachmentModel> getAttachmentForSiteCheckList(int sourceType, String guid, int taskId);

    @Query("UPDATE CheckedGuardEntity set checkedGuardStatus=2, guardStatus=4 where taskId=:taskId and employeeId = :employeeId")
    public abstract Maybe<Integer> updateCheckedGuardViaPracto(int taskId, int employeeId);

    /*@Query("select se.sitename from taskentity te inner join siteentity se on te.siteid=se.id where te.id = :taskId")
    public abstract Maybe<String> getSiteName(int taskId);*/
}
