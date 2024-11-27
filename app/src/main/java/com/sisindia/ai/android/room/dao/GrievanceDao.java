package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.models.GrievanceModel;
import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.GrievanceEntity;
import com.sisindia.ai.android.uimodels.ClosedGrievanceModel;
import com.sisindia.ai.android.uimodels.IssueSeverityModel;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class GrievanceDao implements BaseDao<GrievanceEntity> {

    @Query("SELECT * FROM GrievanceEntity")
    public abstract Single<List<GrievanceEntity>> fetchAll();

    @Query("SELECT gs.*,lu.*,es.*,site.*, gs.id AS grievanceId," +
            "lu.displayName AS categoryName," +
            "es.employeeName as guardName FROM GrievanceEntity AS gs " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 12) AS lu ON gs.categoryId = lu.lookupIdentifier " +
            "INNER JOIN SiteEntity AS site ON es.siteId = site.id " +
            "INNER JOIN EmployeeSiteEntity AS es ON gs.employeeId = es.employeeId " +
            "WHERE gs.id=:grievanceId LIMIT 1")
    public abstract Single<GrievanceModel> fetchGrievanceById(int grievanceId);


    @Query("SELECT gs.*,lu.*,es.*,site.*,gs.id as grievanceId, " +
            "lu.displayName AS categoryName, lu.lookupIdentifier as categoryId," +
            "es.employeeName as guardName FROM GrievanceEntity AS gs " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 12) AS lu ON gs.categoryId = lu.lookupIdentifier " +
            "INNER JOIN SiteEntity AS site ON es.siteId = site.id " +
            "INNER JOIN EmployeeSiteEntity AS es ON gs.employeeId = es.employeeId " +
            "WHERE gs.siteId=:siteId AND gs.grievanceStatus IN (1,2,3)")
    public abstract Single<List<GrievanceModel>> fetchAllOpenGrievanceBySiteId(int siteId);

    @Query("SELECT gs.*,lu.*,es.*,site.*,gs.id as grievanceId," +
            "lu.displayName AS categoryName, lu.lookupIdentifier as categoryId," +
            "es.employeeName as guardName FROM GrievanceEntity AS gs " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 12) AS lu ON gs.categoryId = lu.lookupIdentifier " +
            "INNER JOIN SiteEntity AS site ON es.siteId = site.id " +
            "INNER JOIN EmployeeSiteEntity AS es ON gs.employeeId = es.employeeId " +
            "WHERE gs.siteId=:siteId AND gs.grievanceStatus IN (4,5)")
    public abstract Single<List<GrievanceModel>> fetchAllClosedGrievanceBySiteId(int siteId);

    @Query("SELECT gs.*,lu.*,es.*,site.*,gs.id as grievanceId," +
            "lu.displayName AS categoryName, lu.lookupIdentifier as categoryId," +
            "es.employeeName as guardName FROM GrievanceEntity AS gs " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 12) AS lu ON gs.categoryId = lu.lookupIdentifier " +
            "INNER JOIN SiteEntity AS site ON es.siteId = site.id " +
            "INNER JOIN EmployeeSiteEntity AS es ON gs.employeeId = es.employeeId " +
            "WHERE gs.siteId=:siteId AND gs.employeeId=:employeeId")
    public abstract Single<List<GrievanceModel>> fetchAllOpenGrievanceByEmployeeId(int employeeId, int siteId);

    @Query("SELECT * FROM GrievanceEntity WHERE isSync = :isSync")
    public abstract Single<List<GrievanceEntity>> fetchAllNotSyncedItems(boolean isSync);

    @Query("UPDATE GrievanceEntity SET serverId=:serverId,isSync=:isSync WHERE localId=:localId")
    public abstract Single<Integer> updateOnSyncedToServer(int serverId, String localId, boolean isSync);


    @Query("SELECT gs.*,lu.*,es.*,site.*,gs.id as grievanceId," +
            "lu.displayName AS categoryName, lu.lookupIdentifier as categoryId," +
            "es.employeeName as guardName,es.employeeNo FROM GrievanceEntity AS gs " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 12) AS lu ON gs.categoryId = lu.lookupIdentifier " +
            "INNER JOIN SiteEntity AS site ON es.siteId = site.id " +
            "INNER JOIN EmployeeSiteEntity AS es ON gs.employeeId = es.employeeId " +
            "WHERE gs.grievanceStatus IN (1,2,3)")
    public abstract Single<List<GrievanceModel>> fetchAllOpenGrievances();


    @Query("SELECT gs.*,lu.*,es.*,site.*,gs.id as grievanceId," +
            "lu.displayName AS categoryName, lu.lookupIdentifier as categoryId," +
            "es.employeeName as guardName FROM GrievanceEntity AS gs " +
            "INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 12) AS lu ON gs.categoryId = lu.lookupIdentifier " +
            "INNER JOIN SiteEntity AS site ON es.siteId = site.id " +
            "INNER JOIN EmployeeSiteEntity AS es ON gs.employeeId = es.employeeId " +
            "WHERE gs.grievanceStatus IN (4,5)")
    public abstract Single<List<GrievanceModel>> fetchAllClosedGrievances();

    @Query("UPDATE GrievanceEntity SET closeRemarks=:remarks, isSync=:isSync, grievanceStatus=:closed WHERE id=:grievanceId")
    public abstract Single<Integer> updateGrievanceWithCloseRemarks(int grievanceId, String remarks, int closed, boolean isSync);

    @Query("SELECT gs.id AS grievanceId,lu.displayName AS categoryName,site.siteName AS siteName, " +
            "(SELECT COUNT(id) FROM GrievanceEntity) AS totalGrievances , " +
            "(SELECT COUNT(id) FROM GrievanceEntity WHERE grievanceStatus IN (4,5)) AS completedGrievances, " +
            "(SELECT COUNT(id) FROM GrievanceEntity WHERE grievanceStatus IN (1,2,3)) AS pendingGrievances " +
            "FROM GrievanceEntity AS gs INNER JOIN (SELECT * FROM LookUpEntity WHERE lookupTypeId = 12) AS lu ON gs.categoryId = lu.lookupIdentifier " +
            "INNER JOIN SiteEntity AS site ON es.siteId = site.id " +
            "INNER JOIN EmployeeSiteEntity AS es ON gs.employeeId = es.employeeId WHERE gs.id=:grievanceId")
    public abstract Single<ClosedGrievanceModel> fetchDetailOnGrievanceClose(int grievanceId);

    @Query("SELECT (SELECT COUNT(id) FROM GrievanceEntity  " +
            "WHERE createdDateTime BETWEEN :mediumDateTime AND :lowDateTime AND grievanceStatus in (1,2,3)) AS lowSeverity,  " +
            "(SELECT COUNT(id) FROM GrievanceEntity  " +
            "WHERE createdDateTime BETWEEN :highDateTime AND :mediumDateTime AND grievanceStatus in (1,2,3)) AS mediumSeverity, " +
            "(SELECT COUNT(id) FROM GrievanceEntity WHERE createdDateTime < :highDateTime AND grievanceStatus in (1,2,3)) AS highSeverity ")
    public abstract Single<IssueSeverityModel> fetchGrievanceSeverity(String lowDateTime, String mediumDateTime, String highDateTime);

    @Query("SELECT zoneCode||'/'||regionCode||'/'||branchCode||'/'||s.siteCode||'/'||tte.name||'/'||t.serverId As storagePath from AIProfileEntity left join SiteEntity s join TaskEntity t on s.id=t.siteId join TaskTypeEntity tte on tte.id=t.tasktypeid WHERE t.id=:taskId")
    public abstract Single<String> getGrievanceStorage(int taskId);
}
