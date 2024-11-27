package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.models.EmployeeRewardSummaryResponse;
import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class EmployeeFineRewardDao implements BaseDao<EmployeeFineRewardEntity> {

    @Query("SELECT * from EmployeeFineRewardEntity")
    public abstract Single<List<EmployeeFineRewardEntity>> fetchAll();

    @Query("SELECT * from EmployeeFineRewardEntity WHERE isSync=:isSync")
    public abstract Single<List<EmployeeFineRewardEntity>> fetchAllNotSyncedItems(boolean isSync);

    @Query("UPDATE EmployeeFineRewardEntity SET serverId=:serverId,isSync=:isSync WHERE localId=:localId")
    public abstract Single<Integer> updateOnSyncedToServer(int serverId, String localId, boolean isSync);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllFineRewards(List<EmployeeFineRewardEntity> list);
//    public abstract Single<List<Long>> insertAllFineRewards(List<FineRewardMasterEntity> list);

//    @Query("Select rf.employeeId,e.employeeName,e.employeeNo, (Select Count(*) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=1 )rewardCount, (Select Sum(amount) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=1 )totalReward, (Select Count(*) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=2) fineCount, (Select Sum(amount) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=2 )totalFine from EmployeeFineRewardEntity rf join EmployeeSiteEntity e on rf.employeeId=e.employeeId or rf.employeeNo=e.employeeNo where e.employeeNo LIKE '%' || :empNo || '%' GROUP by rf.employeeId,e.employeeName,e.employeeNo")
    @Query("Select rf.employeeId,e.employeeName,e.employeeNo, (Select Count(ids) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=1 and strftime('%m',createdDateTime)=strftime('%m','now') )rewardCount, (Select Sum( ifnull(amount,rewardFineValue)) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=1 and strftime('%m',createdDateTime)=strftime('%m','now') )totalReward, (Select Count(ids) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=2 and strftime('%m',createdDateTime)=strftime('%m','now')) fineCount, (Select Sum(ifnull(amount,rewardFineValue)) from EmployeeFineRewardEntity where (employeeId=e.employeeId Or employeeNo=e.employeeNo) and rewardType=2 and strftime('%m',createdDateTime)=strftime('%m','now') )totalFine from EmployeeFineRewardEntity rf join EmployeeSiteEntity e on rf.employeeId=e.employeeId or rf.employeeNo=e.employeeNo where e.employeeNo LIKE '%' || :empNo || '%' and strftime('%m',createdDateTime)=strftime('%m','now') GROUP by rf.employeeId,e.employeeName,e.employeeNo")
    public abstract Single<EmployeeRewardSummaryResponse.RewardSummary> getRewardOrFineViaEmpId(String empNo);

    @Query("Select sum(CAST(rewardFineValue AS INTEGER)) as amount From EmployeeFineRewardEntity where strftime('%m', createdDateTime)=strftime('%m',date('now')) and employeeId=:empId and rewardType=:typeId GROUP by employeeId")
    public abstract Maybe<Integer> getLastRewardOrFineAmount(int empId, int typeId);
}
