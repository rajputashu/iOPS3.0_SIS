package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.BillCollectionsEntity;
import com.sisindia.ai.android.room.entities.CustomerContactEntity;
import com.sisindia.ai.android.room.entities.CustomerEntity;
import com.sisindia.ai.android.room.entities.EmployeeLeaveEntity;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class UserMasterDataResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public UserMasterData userMasterData;

    public UserMasterDataResponse() {
    }


    @Parcel
    public static class UserMasterData {

        @SerializedName("customers")
        public List<CustomerEntity> customers;

        @SerializedName("customerContacts")
        public List<CustomerContactEntity> customerContacts;

        @SerializedName("employeeLeaves")
        public List<EmployeeLeaveEntity> employeeLeaves;

        @SerializedName("barracks")
        public List<BarrackEntity> barracks;

        @SerializedName("sites")
        public List<SiteEntity> sites;

        @SerializedName("employeeSites")
        public List<EmployeeSiteEntity> employeeSites;

        @SerializedName("siteRisks")
        public List<SiteAtRiskEntity> siteRisks;

        @SerializedName("billCollections")
        public List<BillCollectionsEntity> billCollections;

        public UserMasterData() {
        }
    }
}

