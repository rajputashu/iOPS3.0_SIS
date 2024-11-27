package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.GuardKitRequestEntity;
import com.sisindia.ai.android.uimodels.AttachmentModel;
import com.sisindia.ai.android.uimodels.KitItemModel;
import com.sisindia.ai.android.uimodels.KitRequestModel;
import com.sisindia.ai.android.uimodels.akr.AKRAttachmentMO;
import com.sisindia.ai.android.uimodels.akr.GuardKitRequestMO;
import com.sisindia.ai.android.uimodels.akr.KitAssignedDistributedMO;
import com.sisindia.ai.android.uimodels.akr.KitBySiteDetailsMO;
import com.sisindia.ai.android.uimodels.akr.KitCountDetailsMO;
import com.sisindia.ai.android.uimodels.akr.KitRequestItemsMO;
import com.sisindia.ai.android.uimodels.akr.KitToReplaceItemsMO;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class GuardKitRequestDao implements BaseDao<GuardKitRequestEntity> {

    @Query("SELECT * from GuardKitRequestEntity")
    public abstract Maybe<List<GuardKitRequestEntity>> fetchAll();

    @Query("SELECT count(kri.kitRequestId) AS noOfItems, " +
            "(SELECT  i.itemName FROM KitRequestItemEntity AS r " +
            "INNER JOIN KitItemEntity AS i ON i.id=r.kitItemId  " +
            "WHERE r.kitRequestId=kri.kitRequestId LIMIT 1) AS firstKitItemName, " +
            "(SELECT es.employeeName FROM EmployeeSiteEntity AS es " +
            "WHERE es.employeeId=kri.employeeId) AS employeeName, " +
            "gkr.*, kri.*, gkr.createdDateTime AS requestDateTime, kri.kitRequestId " +
            "FROM GuardKitRequestEntity AS gkr " +
            "INNER JOIN KitRequestItemEntity AS kri ON gkr.id=kri.kitRequestId " +
            "WHERE gkr.id=:id GROUP BY kri.kitRequestId")
    public abstract Single<KitRequestModel> fetchAddedKitRequestItem(int id);

    @Query("SELECT count(kri.kitRequestId) AS noOfItems, " +
            "(SELECT  i.itemName FROM KitRequestItemEntity AS r " +
            "INNER JOIN KitItemEntity AS i ON i.id=r.kitItemId  " +
            "WHERE r.kitRequestId=kri.kitRequestId LIMIT 1) AS firstKitItemName, " +
            "(SELECT es.employeeName FROM EmployeeSiteEntity AS es " +
            "WHERE es.employeeId=kri.employeeId) AS employeeName, " +
            "gkr.*, kri.*, gkr.createdDateTime AS requestDateTime, kri.kitRequestId " +
            "FROM GuardKitRequestEntity AS gkr " +
            "INNER JOIN KitRequestItemEntity AS kri ON gkr.id=kri.kitRequestId " +
            "WHERE gkr.employeeId=:employeeId GROUP BY kri.kitRequestId")
    public abstract Single<List<KitRequestModel>> fetchAllEmployeeKitRequests(int employeeId);

    @Query("SELECT count(kri.kitRequestId) AS noOfItems, " +
            "(SELECT  i.itemName FROM KitRequestItemEntity AS r " +
            "INNER JOIN KitItemEntity AS i ON i.id=r.kitItemId  " +
            "WHERE r.kitRequestId=kri.kitRequestId LIMIT 1) AS firstKitItemName, " +
            "(SELECT es.employeeName FROM EmployeeSiteEntity AS es " +
            "WHERE es.employeeId=kri.employeeId) AS employeeName, " +
            "gkr.*, kri.*, gkr.createdDateTime AS requestDateTime, kri.kitRequestId " +
            "FROM GuardKitRequestEntity AS gkr " +
            "INNER JOIN KitRequestItemEntity AS kri ON gkr.id=kri.kitRequestId " +
            "WHERE gkr.employeeId=:employeeId AND gkr.siteId = :siteId AND kitStatus=1 GROUP BY kri.kitRequestId")
    public abstract Single<List<KitRequestModel>> fetchAllEmployeeKitRequestsBySite(int employeeId, int siteId);

    //Used in{AKR module : Home Screen {Counts Card}}
//    @Query("Select (Select Count(id) from KitDistributionListEntity WHERE distributionStatus=2 AND createdDateTime BETWEEN DATE('now','start of month','-1 month','0 day') and DATE('now','start of month','+1 month','0 day') and kitissueno is not null )As DueSinceLastMonth, (Select Count(id) from KitDistributionListEntity where createdDateTime BETWEEN DATE('now','start of month','0 month','0 day') and DATE('now','start of month','+1 month','0 day') and distributionStatus=2 and kitissueno is not null )As AddedThisMonth, (Select Count(id) from KitDistributionListEntity where distributionDateTime BETWEEN DATE('now','start of month','0 month','0 day') and DATE('now','start of month','+1 month','0 day') and distributionStatus=3 and kitissueno is not null)As DistributedThisMonth, (Select count(*) from KitDistributionListEntity where distributionStatus =2 and kitissueno is not null )As PendingForDistribution")
    @Query("Select (Select Count(id) from KitDistributionListEntity WHERE distributionStatus=2 AND createdDateTime BETWEEN DATE('now','start of month','-1 month','0 day') and DATE('now','start of month','+1 month','0 day') and kitissueno is not null )As DueSinceLastMonth, (Select Count(id) from KitDistributionListEntity where createdDateTime BETWEEN DATE('now','start of month','0 month','0 day') and DATE('now','start of month','+1 month','0 day') and distributionStatus=2 and kitissueno is not null )As AddedThisMonth, (Select Count(id) from KitDistributionListEntity where distributionDateTime BETWEEN DATE('now','start of month','0 month','0 day') and DATE('now','start of month','+1 month','0 day') and distributionStatus=3 and kitissueno is not null)As DistributedThisMonth, (Select count(*) from KitDistributionListEntity where distributionStatus =2 and kitissueno is not null )As PendingForDistribution")
    public abstract Single<KitCountDetailsMO> fetchPendingDistributedKitCounts();

    //Used in{AKR module} : Updating Query
//    @Query("select k.siteId,s.siteName, (Select Count(id) from KitDistributionListEntity WHERE siteid=k.siteId and distributionStatus=2 and kitissueno is not null)as kitPending, (select count(kri.id) from KitDistributionListEntity grk join KitDistributionItemsEntity kri on kri.kitDistributionId=grk.id where grk.siteid=k.siteId and grk.distributionStatus=2 and kri.isIssued=0 and kitissueno is not null) as kitItems, (Select Count(id) from KitDistributionListEntity WHERE siteid=k.siteId and distributionStatus=3 and kitissueno is not null)as kitDistributed, (Select Count(id) from KitDistributionListEntity WHERE siteid=k.siteId and distributionStatus=5 and kitissueno is not null) as unPaidKits from KitDistributionListEntity as k join SiteEntity s on s.id=k.siteId GROUP by siteId")
    @Query("select k.siteId,k.siteName, (Select Count(id) from KitDistributionListEntity WHERE siteid=k.siteId and distributionStatus=2 and kitissueno is not null)as kitPending, (select count(kri.id) from KitDistributionListEntity grk join KitDistributionItemsEntity kri on kri.kitDistributionId=grk.id where grk.siteid=k.siteId and grk.distributionStatus=2 and kri.isIssued=0 and kitissueno is not null) as kitItems, (Select Count(id) from KitDistributionListEntity WHERE siteid=k.siteId and distributionStatus=3 and kitissueno is not null)as kitDistributed, (Select Count(id) from KitDistributionListEntity WHERE siteid=k.siteId and distributionStatus=5 and kitissueno is not null) as unPaidKits from KitDistributionListEntity as k group by k.siteId,k.siteName")
    public abstract Single<List<KitBySiteDetailsMO>> fetchAllKitDetails();

    @Query("SELECT count(kri.kitRequestId) AS noOfItems, " +
            "(SELECT  i.itemName FROM KitRequestItemEntity AS r " +
            "INNER JOIN KitItemEntity AS i ON i.id=r.kitItemId  " +
            "WHERE r.kitRequestId=kri.kitRequestId LIMIT 1) AS firstKitItemName, " +
            "(SELECT es.employeeName FROM EmployeeSiteEntity AS es " +
            "WHERE es.employeeId=kri.employeeId) AS employeeName, " +
            "gkr.*, kri.*, gkr.createdDateTime AS requestDateTime, kri.kitRequestId " +
            "FROM GuardKitRequestEntity AS gkr " +
            "INNER JOIN KitRequestItemEntity AS kri ON gkr.id=kri.kitRequestId " +
            "WHERE gkr.siteId=:siteId GROUP BY kri.kitRequestId")
    public abstract Single<List<KitRequestModel>> fetchAllSiteKitRequests(int siteId);

//    @Query("Select gkr.recipientId as guardId, gkr.recipientName as guardName, gkr.recipientCode as guardRegNo, Count(kri.id) as totalItems, substr(gkr.createdDateTime,1,10) as date, gkr.distributionStatus as status, gkr.kitIssueNo from KitDistributionListEntity gkr join KitDistributionItemsEntity kri on gkr.id=kri.kitDistributionId WHERE gkr.siteId=:siteId and gkr.distributionStatus in(1,2,3) AND gkr.createdDateTime BETWEEN DATE('now','start of month','-1 month','0 day') and DATE('now','start of month','+1 month','0 day') and kri.isIssued=0 and gkr.kitissueno is not null Group by gkr.siteId,gkr.recipientId,gkr.kitissueno")
    @Query("Select gkr.recipientId as guardId, gkr.recipientName as guardName, gkr.recipientCode as guardRegNo, Count(kri.id) as totalItems, substr(gkr.createdDateTime,1,10) as date, gkr.distributionStatus as status, gkr.kitIssueNo from KitDistributionListEntity gkr join KitDistributionItemsEntity kri on gkr.id=kri.kitDistributionId WHERE gkr.siteId=:siteId and gkr.distributionStatus=2 and kri.isIssued=0 and gkr.kitissueno is not null Group by gkr.siteId,gkr.recipientId,gkr.kitissueno")
    public abstract Single<List<KitAssignedDistributedMO>> fetchAssignedKit(int siteId);

//    @Query("Select gkr.recipientId as guardId, gkr.recipientName as guardName, gkr.recipientCode as guardRegNo, Count(kri.id) as totalItems, substr(gkr.distributionDateTime,1,10) as date, gkr.distributionStatus as status, gkr.kitIssueNo from KitDistributionListEntity gkr join KitDistributionItemsEntity kri on gkr.id=kri.kitDistributionId WHERE gkr.siteId=:siteId and gkr.distributionStatus in(4,5) AND gkr.createdDateTime BETWEEN DATE('now','start of month','-1 month','10 day') and DATE('now','start of month','+1 month','0 day') Group by gkr.siteId,gkr.recipientId")
    @Query("Select gkr.recipientId as guardId, gkr.recipientName as guardName, gkr.recipientCode as guardRegNo, Count(kri.id) as totalItems, substr(gkr.distributionDateTime,1,10) as date, gkr.distributionStatus as status, gkr.kitIssueNo from KitDistributionListEntity gkr join KitDistributionItemsEntity kri on gkr.id=kri.kitDistributionId WHERE gkr.siteId=:siteId and gkr.distributionStatus in(3,5) Group by gkr.siteId,gkr.recipientId")
    public abstract Single<List<KitAssignedDistributedMO>> fetchDistributedKit(int siteId);

//    @Query("select gkr.id,gkr.siteId,s.siteName, (Select Count(id) from KitDistributionListEntity WHERE siteid=gkr.siteId and distributionStatus=2 and kitissueno is not null )as kitPending, (select count(kri.id) from KitDistributionListEntity grk join KitDistributionItemsEntity kri on kri.kitDistributionId=grk.id where grk.siteid=gkr.siteId and grk.distributionStatus=2 and kri.isIssued =0 and kitissueno is not null ) as kitItems, (Select Count(id) from KitDistributionListEntity WHERE siteid=gkr.siteId and distributionStatus=3 and kitissueno is not null )as kitDistributed, (Select Count(id) from KitDistributionListEntity WHERE siteid=gkr.siteId and distributionStatus=5 and kitissueno is not null ) as unPaidKits from KitDistributionListEntity as gkr join SiteEntity s on s.id=gkr.siteId where siteId=:siteId GROUP by siteId")
    @Query("select gkr.siteId,gkr.siteName, (Select Count(id) from KitDistributionListEntity WHERE siteid=gkr.siteId and distributionStatus=2 and kitissueno is not null )as kitPending, (select count(kri.id) from KitDistributionListEntity grk join KitDistributionItemsEntity kri on kri.kitDistributionId=grk.id where grk.siteid=gkr.siteId and grk.distributionStatus=2 and kri.isIssued =0 and kitissueno is not null ) as kitItems, (Select Count(id) from KitDistributionListEntity WHERE siteid=gkr.siteId and distributionStatus=3 and kitissueno is not null )as kitDistributed, (Select Count(id) from KitDistributionListEntity WHERE siteid=gkr.siteId and distributionStatus=5 and kitissueno is not null ) as unPaidKits from KitDistributionListEntity as gkr where siteId=:siteId GROUP by gkr.siteId,gkr.siteName")
    public abstract Single<KitBySiteDetailsMO> fetchKitDetailsViaSiteId(int siteId);

//    @Query("Select gkr.id,kri.id as kitId,gkr.siteId, gkr.recipientName as guardName, s.siteName,kri.kitItemId,kri.itemName as kitItemName, kri.itemSizeCode, gkr.otpTypeId, gkr.recipientPhoneNo from KitDistributionListEntity gkr join KitDistributionItemsEntity kri on gkr.id=kri.kitDistributionId join SiteEntity s on s.id=gkr.siteId WHERE gkr.siteId=:siteId and gkr.recipientId=:guardId and kri.isIssued=0 and gkr.kitIssueNo = :kitIssueNo")
    @Query("Select gkr.id,kri.id as kitId,gkr.siteId, gkr.recipientName as guardName, gkr.siteName,kri.kitItemId,kri.itemName as kitItemName, kri.itemSizeCode, gkr.otpTypeId, gkr.recipientPhoneNo from KitDistributionListEntity gkr join KitDistributionItemsEntity kri on gkr.id=kri.kitDistributionId WHERE gkr.siteId=:siteId and gkr.recipientId=:guardId and kri.isIssued=0 and gkr.kitIssueNo = :kitIssueNo")
    public abstract Single<List<KitToReplaceItemsMO>> fetchKitToReplaceDetails(int siteId, int guardId, String kitIssueNo);
//    public abstract Single<List<KitToReplaceItemsMO>> fetchKitToReplaceDetails(int siteId, int guardId, int[] ids, String kitIssueNo);

    @Query("SELECT * FROM KitItemSizeEntity WHERE kitItemId = :kitItemId")
    public abstract Single<List<KitItemModel.KitItemSizeModel>> fetAllKitItemSizes(int kitItemId);

    @Query("SELECT * from KitItemEntity")
    public abstract Single<List<KitItemModel>> fetAllKitItems();

    //Used for sync(WORKER)
    /*@Query("SELECT * from GuardKitRequestEntity WHERE isSync=:isSync")
    public abstract Maybe<List<GuardKitRequestEntity>> fetchAllNotSyncedItems(boolean isSync);*/

    //    @Query("SELECT id,employeeId as requestorId,imageAttachmentGuid as photoId,signatureAttachmentGuid as signatureId,taskId as siteTaskId,siteId,localId as kitRequestGuid,serverId,createdDateTime as requestedOn,kitStatus as requestStatus, isSync from GuardKitRequestEntity WHERE isSync = 0")
    @Query("SELECT id,employeeId as requestorId,taskId as siteTaskId,siteId,localId as kitRequestGuid,serverId,createdDateTime as requestedOn,kitStatus as requestStatus, isSync from GuardKitRequestEntity WHERE isSync = 0")
    public abstract Maybe<List<GuardKitRequestMO>> fetchAllNotSyncedItems();

    /*@Query("SELECT * from KitRequestItemEntity WHERE kitRequestId=:kitRequestId")
    public abstract Single<List<KitRequestItemEntity>> fetchAllKitRequestItems(int kitRequestId);*/

    @Query("SELECT id,kitRequestId,kitItemId, kitSizeId as itemSizeCode,taskId,siteId,createdDateTime,updatedDateTime,replaceStatus from KitRequestItemEntity WHERE kitRequestId=:kitRequestId")
    public abstract Single<List<KitRequestItemsMO>> fetchAllKitRequestItems(int kitRequestId);

    @Query("UPDATE GuardKitRequestEntity SET serverId=:serverId,isSync=:isSync WHERE localId=:localId")
    public abstract Single<Integer> updateOnSyncedToServer(int serverId, String localId, boolean isSync);

    //{global-container}/{zone}/{region}/{branch}/{site}/task/{taskId}/{attachmentSourceTypeId_siteId_taskId_employeeId_uuid}.jpg
    @Query("SELECT task.serverId as serverId, (:sourceType||'_'||site.id||'_'||task.serverId||'_'||:employeeId||'_'||:guid) AS fileName, " +
            "(ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||site.siteCode||'/'||type.name||'/'||task.serverId||'/'||:sourceType||'_'||site.id||'_'||task.serverId||'_'||:employeeId||'_'||:guid) AS storagePath " +
            "FROM TaskEntity as task LEFT JOIN AIProfileEntity as ai " +
            "INNER JOIN SiteEntity AS site ON site.id = task.siteId " +
            "INNER JOIN TaskTypeEntity AS type ON type.id=task.taskTypeId WHERE task.id= :taskId")
    public abstract Single<AttachmentModel> attachmentFileAndStorageForKitRequestSignature(int sourceType, int employeeId, String guid, int taskId);

    //{global-container}/{zone}/{region}/{branch}/{site}/KitDistribute/{kitId}/{attachmentSourceTypeId_siteId_KitId_GuardId_UUID}.jpg
    @Query("SELECT (:sourceType||'_'||:siteId||'_'||:kitId||'_'||:employeeId||'_'||:guid) AS fileName, (ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||:siteId||'/'||'KitDistribute'||'/'||:kitId||'/'||:sourceType||'_'||:siteId||'_'||:kitId||'_'||:employeeId||'_'||:guid) AS storagePath FROM AIProfileEntity as ai")
    public abstract Maybe<AKRAttachmentMO> distributedKitSignatureFile(int sourceType, int siteId, int kitId, int employeeId, String guid);

    //{global-container}/{zone}/{region}/{branch}/{site}/MaskDistribution/{siteCode}/{attachmentSourceTypeId_siteId_siteCode_employeeNo_UUID}.jpg
    @Query("SELECT (:sourceType||'_'||:siteId||'_'||:siteCode||'_'||:employeeNo||'_'||:guid) AS fileName, (ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||:siteId||'/'||'MaskDistribution'||'/'||:siteCode||'/'||:sourceType||'_'||:siteId||'_'||:siteCode||'_'||:employeeNo||'_'||:guid) AS storagePath FROM AIProfileEntity as ai")
    public abstract Maybe<AKRAttachmentMO> maskGuardFile(int sourceType, int siteId, String siteCode, String employeeNo, String guid);

}