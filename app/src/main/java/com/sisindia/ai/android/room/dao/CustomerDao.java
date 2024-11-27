package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.CustomerEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class CustomerDao implements BaseDao<CustomerEntity> {

    @Query("SELECT * from CustomerEntity")
    public abstract Maybe<List<CustomerEntity>> fetchAll();

    //Customer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllCustomer(List<CustomerEntity> list);


    @Query("DELETE FROM CustomerEntity")
    public abstract Completable deleteCustomer();
}
