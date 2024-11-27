package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.BarrackStrengthEntity;
import com.sisindia.ai.android.room.entities.BillCollectionsEntity;
import com.sisindia.ai.android.room.entities.ContractsEntity;
import com.sisindia.ai.android.room.entities.CustomerContactEntity;
import com.sisindia.ai.android.room.entities.CustomerEntity;
import com.sisindia.ai.android.room.entities.CustomerSiteContactEntity;
import com.sisindia.ai.android.room.entities.EmployeeLeaveEntity;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;
import com.sisindia.ai.android.room.entities.KitDistributionListEntity;
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity;
import com.sisindia.ai.android.room.entities.SiteBarrackMapsEntity;
import com.sisindia.ai.android.room.entities.SiteBillingCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.room.entities.SitePostEntity;
import com.sisindia.ai.android.room.entities.SiteShiftEntity;
import com.sisindia.ai.android.room.entities.SiteStrengthEntity;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class UserMasterDataResponseV2 extends BaseNetworkResponse {

    @SerializedName("data")
    public UserMasterDataV2 userMasterData;

    public UserMasterDataResponseV2() {
    }


    @Parcel
    public static class UserMasterDataV2 {

        @SerializedName("employeeLeaves")
        public List<EmployeeLeaveEntity> employeeLeaves;

        @SerializedName("sites")
        public List<SiteEntity> sites;

        @SerializedName("customers")
        public List<CustomerEntity> customers;

        @SerializedName("customerContacts")
        public List<CustomerContactEntity> customerContacts;

        @SerializedName("employeeSites")
        public List<EmployeeSiteEntity> employeeSites;

        @SerializedName("siteStrengths")
        public List<SiteStrengthEntity> siteStrengths;

        @SerializedName("siteShifts")
        public List<SiteShiftEntity> siteShifts;

        @SerializedName("siteBillingChecklists")
        public List<SiteBillingCheckListEntity> siteBillingChecklists;

        @SerializedName("siteBarrackMaps")
        public List<SiteBarrackMapsEntity> siteBarrackMaps;

        @SerializedName("customerSiteContacts")
        public List<CustomerSiteContactEntity> customerSiteContacts;

        @SerializedName("sitePosts")
        public List<SitePostEntity> sitePosts;

        @SerializedName("siteRisks")
        public List<SiteAtRiskEntity> siteRisks;

        @SerializedName("billCollections")
        public List<BillCollectionsEntity> billCollections;

        @SerializedName("barracks")
        public List<BarrackEntity> barracks;

        @SerializedName("contracts")
        public List<ContractsEntity> contracts;

        @SerializedName("barrackStrengths")
        public List<BarrackStrengthEntity> barrackStrengths;
        //todo barrackStrengths is ignored for now

        @SerializedName("kitDistributions")
        public List<KitDistributionListEntity> kitDistributions;

        public UserMasterDataV2() {
        }
    }
}

