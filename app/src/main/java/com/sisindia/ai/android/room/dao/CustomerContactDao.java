package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.CustomerContactEntity;
import com.sisindia.ai.android.uimodels.ClientModel;
import com.sisindia.ai.android.uimodels.handshake.ClientDetailsMO;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class CustomerContactDao implements BaseDao<CustomerContactEntity> {

    @Query("SELECT * from CustomerContactEntity")
    public abstract Maybe<List<CustomerContactEntity>> fetchAll();

    @Query("select cc.* from CustomerContactEntity cc join CustomerSiteContactEntity csc on cc.id=csc.customerContactId where csc.siteId=:siteId and isSynced=1")
    public abstract Single<List<CustomerContactEntity>> fetchCustomerInfo(int siteId);

    //    @Query("SELECT c.id from CustomerContactEntity cc join CustomerEntity c on c.id =cc.customerId join CustomerSiteContactEntity csc on csc.customerContactId=cc.id where csc.siteid=:siteId")
    @Query("select c.customerId from SiteEntity s join ContractsEntity c on c.id=s.contractId where s.id=:siteId")
    public abstract Single<Integer> fetchCustomerId(int siteId);

    //    @Query("SELECT c.customerName as siteName,cc.id as clientId,cc.title, cc.contactFullName as clientName,cc.contactPhoneNo as clientNo from CustomerContactEntity cc join CustomerEntity c on c.id =cc.customerId join CustomerSiteContactEntity csc on csc.customerContactId=cc.id where csc.siteid=:siteId and cc.id=:clientId")
    @Query("SELECT s.siteName as siteName,cc.id as clientId,cc.title, cc.contactFullName as clientName, cc.contactPhoneNo as clientNo from CustomerContactEntity cc left join CustomerEntity c on c.id =cc.customerId join CustomerSiteContactEntity csc on csc.customerContactId=cc.id join SiteEntity s on s.id=csc.siteId where csc.siteid==:siteId and cc.id=:clientId")
    public abstract Maybe<ClientDetailsMO> fetchClientDetails(int siteId, int clientId);

    /*@Query("UPDATE CustomerContactEntity set isSynced=1 where id=:customerContactId")
    public abstract Single<Integer> updateCCEIsSyncedValue(int customerContactId);

    @Query("select count(*) from CustomerContactEntity where contactPhoneNo=:contactPhoneNo")
    public abstract Single<Integer> isMobileNumberExists(String contactPhoneNo);*/

    @Query("select cc.*,csc.customerContactId as customerContactId  from CustomerContactEntity cc join CustomerSiteContactEntity csc on cc.id=csc.customerContactId where csc.siteId=:siteId and isSynced=1")
    public abstract Single<List<ClientModel>> fetchAllCustomerBySite(int siteId);

    //CustomerContact
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllCustomerContact(List<CustomerContactEntity> list);

    @Query("DELETE FROM CustomerContactEntity")
    public abstract Completable deleteCustomerContact();

//    @Query("select id from CustomerContactEntity")
    @Query("select cc.id from CustomerContactEntity cc join CustomerSiteContactEntity csc on cc.id=csc.customerContactId where csc.siteId= :siteId")
    public abstract Single<List<Integer>> fetchAllClientListIds(int siteId);
}
