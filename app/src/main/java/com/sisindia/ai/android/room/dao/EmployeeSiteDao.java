package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class EmployeeSiteDao implements BaseDao<EmployeeSiteEntity> {

    @Query("SELECT * from EmployeeSiteEntity")
    public abstract Single<List<EmployeeSiteEntity>> fetchAll();

    @Query("SELECT * from EmployeeSiteEntity where employeeNo LIKE '%' || :empNumber || '%'")
    public abstract Single<EmployeeSiteEntity> fetchGuardViaEmpNo(String empNumber);

    @Query("SELECT min(employeeid) FROM EmployeeSiteEntity")
    public abstract Single<Integer> fetchMinGuardId();

    @Query("SELECT * from EmployeeSiteEntity AS es WHERE es.employeeId=:guardId")
    public abstract Single<GuardSuggestionItem> fetchGuardDetailForAddGrievance(int guardId);

    @Query("SELECT * from EmployeeSiteEntity AS es WHERE es.siteId=:siteId")
    public abstract Single<List<GuardSuggestionItem>> fetchAllGuardsBySiteId(int siteId);

    @Query("SELECT * from EmployeeSiteEntity")
    public abstract Single<List<GuardSuggestionItem>> fetchAllGuards();

    @Query("SELECT * from EmployeeSiteEntity AS es INNER JOIN SiteEntity AS site ON site.id = es.siteId ")
    public abstract Single<List<GuardSuggestionItem>> fetchAllGuardsForKitRequest();

    @Query("SELECT * from EmployeeSiteEntity AS es INNER JOIN SiteEntity AS site ON site.id = es.siteId WHERE es.employeeId=:employeeId LIMIT 1")
    public abstract Single<GuardSuggestionItem> fetchGuardsForKitRequest(int employeeId);

    @Query("DELETE FROM EmployeeSiteEntity")
    public abstract Completable deleteEmployeeSite();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllEmployeeSite(List<EmployeeSiteEntity> list);

    @Query("UPDATE EmployeeSiteEntity SET deployedDate=:deployedDate WHERE employeeId=:employeeId")
    public abstract Single<Integer> updateDeployedDate(String deployedDate, int employeeId);
}
