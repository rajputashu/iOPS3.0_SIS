package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.ComplaintEntity;
import com.sisindia.ai.android.uimodels.ClosedComplaintModel;
import com.sisindia.ai.android.uimodels.ComplaintModel;
import com.sisindia.ai.android.uimodels.IssueSeverityModel;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class ComplaintDao implements BaseDao<ComplaintEntity> {


    @Query("SELECT * from ComplaintEntity WHERE isSync=:isSync")
    public abstract Single<List<ComplaintEntity>> fetchAllNotSyncedItems(boolean isSync);

    @Query("UPDATE ComplaintEntity SET serverId=:serverId,isSync=:isSync WHERE localId=:localId")
    public abstract Single<Integer> updateOnSyncedToServer(int serverId, String localId, boolean isSync);

    @Query("SELECT c.* ,site.*, cc.*,c.id as id, mode.displayName AS modeName, " +
            "type.displayName AS typeName, nature.displayName AS natureName " +
            "FROM ComplaintEntity AS c  INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 5) AS mode ON  mode.lookupIdentifier = c.modeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 6) AS type ON  type.lookupIdentifier = c.typeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 7) AS nature ON  nature.lookupIdentifier = c.natureLookupIdentifier " +
            "INNER JOIN CustomerContactEntity AS cc ON cc.id = c.customerId INNER JOIN SiteEntity AS site ON site.id = c.siteId " +
            "WHERE status IN (1,2)")
    public abstract Single<List<ComplaintModel>> fetchAllPendingComplaints();

    @Query("SELECT c.* ,site.*, cc.*,c.id as id, mode.displayName AS modeName, " +
            "type.displayName AS typeName, nature.displayName AS natureName " +
            "FROM ComplaintEntity AS c  INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 5) AS mode ON  mode.lookupIdentifier = c.modeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 6) AS type ON  type.lookupIdentifier = c.typeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 7) AS nature ON  nature.lookupIdentifier = c.natureLookupIdentifier " +
            "INNER JOIN CustomerContactEntity AS cc ON cc.id = c.customerId INNER JOIN SiteEntity AS site ON site.id = c.siteId " +
            "WHERE status IN (3,4)")
    public abstract Single<List<ComplaintModel>> fetchAllClosedComplaints();


    @Query("SELECT c.* ,site.*, cc.*,c.id as id, mode.displayName AS modeName, " +
            "type.displayName AS typeName, nature.displayName AS natureName " +
            "FROM ComplaintEntity AS c  INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 5) AS mode ON  mode.lookupIdentifier = c.modeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 6) AS type ON  type.lookupIdentifier = c.typeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 7) AS nature ON  nature.lookupIdentifier = c.natureLookupIdentifier " +
            "INNER JOIN CustomerContactEntity AS cc ON cc.id = c.customerId INNER JOIN SiteEntity AS site ON site.id = c.siteId " +
            "WHERE c.id=:complaintId AND status IN (1,2,3) LIMIT 1")
    public abstract Single<ComplaintModel> fetchComplaintById(int complaintId);


    @Query("UPDATE ComplaintEntity SET closureComment=:remarks, isSync=:isSync, status=:closed WHERE id=:complaintId")
    public abstract Single<Integer> updateComplaintWithCloseRemarks(int complaintId, String remarks, int closed, boolean isSync);


    @Query("SELECT c.*,site.siteName as siteName,lu.displayName as natureName , " +
            "(SELECT COUNT(id) FROM ComplaintEntity WHERE status IN (1,2)) as pendingComplaints, " +
            "(SELECT COUNT(id) FROM ComplaintEntity ) as totalComplaints, " +
            "(SELECT COUNT(id) FROM ComplaintEntity WHERE status IN (3,4)) as completedComplaints " +
            "FROM ComplaintEntity AS c " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 7) AS lu ON lu.lookupIdentifier = c.natureLookupIdentifier  " +
            "INNER JOIN SiteEntity AS site ON site.id = c.siteId " +
            "WHERE c.id = :complaintId")
    public abstract Single<ClosedComplaintModel> fetchDetailOnClosedClose(int complaintId);

    ////


    @Query("SELECT c.* ,site.*, cc.*,c.id as id, mode.displayName AS modeName, " +
            "type.displayName AS typeName, nature.displayName AS natureName " +
            "FROM ComplaintEntity AS c  INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 5) AS mode ON  mode.lookupIdentifier = c.modeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 6) AS type ON  type.lookupIdentifier = c.typeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 7) AS nature ON  nature.lookupIdentifier = c.natureLookupIdentifier " +
            "INNER JOIN CustomerContactEntity AS cc ON cc.id = c.customerId INNER JOIN SiteEntity AS site ON site.id = c.siteId " +
            "WHERE c.siteId = :siteId AND  c.status IN (1,2)")
    public abstract Single<List<ComplaintModel>> fetchAllPendingComplaintsBySite(int siteId);

    @Query("SELECT c.* ,site.*, cc.*,c.id as id, mode.displayName AS modeName, " +
            "type.displayName AS typeName, nature.displayName AS natureName " +
            "FROM ComplaintEntity AS c  INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 5) AS mode ON  mode.lookupIdentifier = c.modeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 6) AS type ON  type.lookupIdentifier = c.typeLookupIdentifier " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 7) AS nature ON  nature.lookupIdentifier = c.natureLookupIdentifier " +
            "INNER JOIN CustomerContactEntity AS cc ON cc.id = c.customerId INNER JOIN SiteEntity AS site ON site.id = c.siteId " +
            "WHERE c.siteId = :siteId AND  c.status IN (3,4)")
    public abstract Single<List<ComplaintModel>> fetchAllClosedComplaintsBySite(int siteId);


    @Query("SELECT (SELECT COUNT(id) FROM ComplaintEntity  " +
            "WHERE createdDateTime BETWEEN :mediumDateTime AND :lowDateTime AND status in (1,2)) AS lowSeverity,  " +
            "(SELECT COUNT(id) FROM ComplaintEntity  " +
            "WHERE createdDateTime BETWEEN :highDateTime AND :mediumDateTime AND status in (1,2)) AS mediumSeverity, " +
            "(SELECT COUNT(id) FROM ComplaintEntity WHERE createdDateTime < :highDateTime AND status in (1,2)) AS highSeverity ")
    public abstract Single<IssueSeverityModel> fetchComplaintsSeverity(String lowDateTime, String mediumDateTime, String highDateTime);

    @Query("SELECT * FROM ComplaintEntity")
    public abstract Single<List<ComplaintEntity>> fetchAll();
}
