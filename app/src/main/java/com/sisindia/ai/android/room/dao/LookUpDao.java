package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.uimodels.AddSecurityCategory;
import com.sisindia.ai.android.uimodels.ComplaintSection;
import com.sisindia.ai.android.uimodels.GrievanceCategory;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;
import com.sisindia.ai.android.uimodels.RewardFineCategory;
import com.sisindia.ai.android.uimodels.RewardFineReasonMO;
import com.sisindia.ai.android.uimodels.billsubmit.BillCheckListMO;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class LookUpDao implements BaseDao<LookUpEntity> {

    @Query("SELECT * from LookUpEntity")
    public abstract Maybe<List<LookUpEntity>> fetchAll();

    @Query("DELETE FROM LookUpEntity")
    public abstract Completable deleteLookUp();

    //LookUp
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllLookUp(List<LookUpEntity> list);

    @Query("SELECT * FROM LookUpEntity WHERE lookupTypeId=:lookUpTypeId")
    public abstract Single<List<LookUpEntity>> fetchAllReasons(int lookUpTypeId);

    @Query("SELECT lookupName as lookUpName,lookupIdentifier as lookUpIdentifier FROM LookUpEntity WHERE lookupTypeId=:lookUpTypeId")
    public abstract Maybe<List<BillCheckListMO>> fetchBillCheckList(int lookUpTypeId);

    @Query("SELECT lu.* FROM LookUpEntity AS lu WHERE lu.lookupTypeId=:lookUpTypeId")
    public abstract Single<List<GuardTurnOutResult.GuardTurnoutModel>> fetchGuardTurnOut(int lookUpTypeId);

    @Query("SELECT lu.* FROM LookUpEntity AS lu WHERE lu.lookupTypeId=:lookUpTypeId")
    public abstract Single<List<GrievanceCategory>> fetchAllCategoryForGrievance(int lookUpTypeId);

    @Query("SELECT lu.* FROM LookUpEntity AS lu WHERE lu.lookupTypeId=:lookUpTypeId")
    public abstract Single<List<AddSecurityCategory>> fetchAllCategoryForAddSecurity(int lookUpTypeId);

    @Query("SELECT lu.* FROM LookUpEntity AS lu WHERE lu.lookupTypeId=:lookUpTypeId")
    public abstract Single<List<RewardFineCategory>> fetchAllCategoryForFineReward(int lookUpTypeId);

    @Query("SELECT lu.* FROM LookUpEntity AS lu WHERE lu.lookupTypeId=:lookUpTypeId")
    public abstract Single<List<ComplaintSection>> fetchAllComplaintSections(int lookUpTypeId);

    @Query("SELECT lu.* FROM LookUpEntity AS lu WHERE lu.lookupTypeId=:typeId AND lookupDependentValue=:lookupIdentifier")
    public abstract Single<List<ComplaintSection>> fetchAllComplaintNature(int typeId, int lookupIdentifier);

    @Query("SELECT displayName FROM LookUpEntity WHERE lookupIdentifier=:lookupIdentifier AND lookupTypeId = :lookupTypeId")
    public abstract Single<String> fetchDisplayName(int lookupIdentifier, int lookupTypeId);

    /*@Query("SELECT * FROM LookUpEntity WHERE lookupTypeId = :lookupTypeId")
    public abstract Single<List<LookUpEntity>> fetchMTrainingMode(int lookupTypeId);*/

    @Query("SELECT displayName,lookupIdentifier FROM LookUpEntity WHERE lookupTypeId=:lookUpTypeId")
    public abstract Single<List<RewardFineReasonMO>> fetchRewardOrFineReasons(int lookUpTypeId);

    @Query("SELECT displayName FROM LookUpEntity WHERE lookupTypeId = 41")
    public abstract Single<List<String>> fetchDisbandmentReason();

    @Query("SELECT * FROM LookUpEntity WHERE lookupTypeId = 202")
    public abstract Single<List<LookUpEntity>> fetchContactLevels();
}
