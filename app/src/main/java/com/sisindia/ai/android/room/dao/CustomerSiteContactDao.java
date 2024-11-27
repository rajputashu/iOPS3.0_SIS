package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.CustomerSiteContactEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class CustomerSiteContactDao implements BaseDao<CustomerSiteContactEntity> {

    @Query("SELECT * from CustomerSiteContactEntity")
    public abstract Maybe<List<CustomerSiteContactEntity>> fetchAll();

    //CustomerSiteContact
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllCustomerSiteContact(List<CustomerSiteContactEntity> list);

    @Query("DELETE FROM CustomerSiteContactEntity")
    public abstract Completable deleteCustomerSiteContact();
}
