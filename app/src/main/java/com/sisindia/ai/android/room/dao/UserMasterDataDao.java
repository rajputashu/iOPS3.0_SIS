package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.BillCenterEntity;
import com.sisindia.ai.android.room.entities.BillCollectionsEntity;
import com.sisindia.ai.android.room.entities.ContractsEntity;
import com.sisindia.ai.android.room.entities.CustomerContactEntity;
import com.sisindia.ai.android.room.entities.CustomerEntity;
import com.sisindia.ai.android.room.entities.CustomerSiteContactEntity;
import com.sisindia.ai.android.room.entities.EmployeeLeaveEntity;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;
import com.sisindia.ai.android.room.entities.PostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.PostRegisterEntity;
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity;
import com.sisindia.ai.android.room.entities.SiteBarrackMapsEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.room.entities.SiteExtensionEntity;
import com.sisindia.ai.android.room.entities.SitePostEntity;
import com.sisindia.ai.android.room.entities.SiteRiskPoaEntity;
import com.sisindia.ai.android.room.entities.SiteShiftEntity;
import com.sisindia.ai.android.room.entities.SiteStrengthEntity;
import com.sisindia.ai.android.room.entities.WageCenterEntity;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class UserMasterDataDao {

    //WageCenter
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllWageCenter(List<WageCenterEntity> list);

    @Query("DELETE FROM WageCenterEntity")
    public abstract int  nukeWageCenterTable();

    //SiteShift
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSiteShift(List<SiteShiftEntity> list);

    @Query("DELETE FROM SiteShiftEntity")
    public abstract int  nukeSiteShiftTable();

    //SitePost
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSitePost(List<SitePostEntity> list);

    @Query("DELETE FROM SitePostEntity")
    public abstract int  nukeSitePostTable();


    //SiteExtension
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertSiteExtension(SiteExtensionEntity obj);

    @Query("DELETE FROM SiteExtensionEntity")
    public abstract int  nukeSiteExtensionTable();


    //EmployeeSite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertEmployeeSite(EmployeeSiteEntity obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllEmployeeSite(List<EmployeeSiteEntity> list);

    @Query("DELETE FROM EmployeeSiteEntity")
    public abstract int  nukeEmployeeSiteTable();


    //EmployeeLeave
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllEmployeeLeave(List<EmployeeLeaveEntity> list);

    @Query("DELETE FROM EmployeeLeaveEntity")
    public abstract int  nukeEmployeeLeaveTable();


    //CustomerSiteContact
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllCustomerSiteContact(List<CustomerSiteContactEntity> list);

    //Site Barrack Mapping
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSiteBarrackMaps(List<SiteBarrackMapsEntity> list);

    @Query("DELETE FROM CustomerSiteContactEntity")
    public abstract int  nukeCustomerSiteContactTable();


    //Customer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllCustomer(List<CustomerEntity> list);

    @Query("DELETE FROM CustomerEntity")
    public abstract int  nukeCustomerTable();


    //CustomerContact
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllCustomerContact(List<CustomerContactEntity> list);

    @Query("DELETE FROM CustomerContactEntity")
    public abstract int  nukeCustomerContactTable();


    //BillCenter

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllBillCenter(List<BillCenterEntity> list);

    @Query("DELETE FROM BillCenterEntity")
    public abstract int  nukeBillCenterTable();


    //Barrack
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllBarrack(List<BarrackEntity> list);

    @Query("DELETE FROM BarrackEntity")
    public abstract int  nukeBarrackTable();


    //SiteStrength
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSiteStrength(List<SiteStrengthEntity> list);

    @Query("DELETE FROM SiteStrengthEntity")
    public abstract int  nukeSiteStrengthTable();

    //Sites
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSites(List<SiteEntity> list);

    //Bill Collections
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllBillCollections(List<BillCollectionsEntity> list);

    @Query("DELETE FROM SiteEntity")
    public abstract int  nukeSiteTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSiteAtRisk(List<SiteAtRiskEntity> list);

    @Query("DELETE FROM SiteAtRiskEntity")
    public abstract int  nukeSiteAtRiskTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllAtRiskPOA(List<SiteRiskPoaEntity> list);

    @Query("DELETE FROM SiteRiskPoaEntity")
    public abstract int  nukeSiteRiskPoaTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllContracts(List<ContractsEntity> list);

    @Query("DELETE FROM ContractsEntity")
    public abstract int  nukeContractsTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertSiteChecklistOption(SiteCheckListOptionEntity option);

    @Query("DELETE FROM SiteCheckListOptionEntity")
    public abstract int  nukeSiteCheckListOptionTable();

    @Query("DELETE FROM CheckedSiteCheckListEntity")
    public abstract int  nukeCheckedSiteCheckListTable();

    @Query("DELETE FROM CheckedPostCheckListEntity")
    public abstract int  nukeCheckedPostCheckListTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertSiteCheckList(SiteCheckListEntity siteCheckList);

    @Query("DELETE FROM SiteCheckListEntity")
    public abstract int  nukeSiteCheckListTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertPostRegister(PostRegisterEntity register);

    @Query("DELETE FROM PostRegisterEntity")
    public abstract int  nukePostRegisterTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertPostCheckList(PostCheckListEntity postCheckList);

    @Query("DELETE FROM WageCenterEntity")
    public abstract int  nukePostCheckListTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertPostCheckListOption(PostCheckListOptionEntity option);

    @Query("DELETE FROM PostCheckListOptionEntity")
    public abstract int  nukePostCheckListOptionTable();


    public void nukeUserMasterData() {
        nukeWageCenterTable();
        nukeSiteShiftTable();
        nukeSitePostTable();
        nukeSiteExtensionTable();
        nukeEmployeeSiteTable();
        nukeEmployeeLeaveTable();
        nukeCustomerSiteContactTable();
        nukeCustomerTable();
        nukeCustomerContactTable();
        nukeBillCenterTable();
        nukeBarrackTable();
        nukeSiteStrengthTable();
        nukeSiteTable();
        nukeSiteAtRiskTable();
        nukeSiteRiskPoaTable();
        nukeContractsTable();
        nukeSiteCheckListOptionTable();
        nukeSiteCheckListTable();
        nukePostRegisterTable();
        nukePostCheckListTable();
        nukePostCheckListOptionTable();
        nukeCheckedSiteCheckListTable();
        nukeCheckedPostCheckListTable();
    }

}
