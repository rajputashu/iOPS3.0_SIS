package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.room.entities.ActionPlanEntity;
import com.sisindia.ai.android.room.entities.AttachmentMetadataDefinitionEntity;
import com.sisindia.ai.android.room.entities.BranchEntity;
import com.sisindia.ai.android.room.entities.ClientHandShakeQuestionEntity;
import com.sisindia.ai.android.room.entities.CompanyEntity;
import com.sisindia.ai.android.room.entities.CountryEntity;
import com.sisindia.ai.android.room.entities.DeploymentTypeEntity;
import com.sisindia.ai.android.room.entities.HolidayEntity;
import com.sisindia.ai.android.room.entities.IndustrySectorEntity;
import com.sisindia.ai.android.room.entities.KitItemEntity;
import com.sisindia.ai.android.room.entities.LanguageEntity;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.room.entities.OrganizationEntity;
import com.sisindia.ai.android.room.entities.RankEntity;
import com.sisindia.ai.android.room.entities.RegionEntity;
import com.sisindia.ai.android.room.entities.SiteTypeEntity;
import com.sisindia.ai.android.room.entities.TaskTypeEntity;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class CommonMasterDataResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public CommonMasterData commonMasterData;

    public CommonMasterDataResponse() {
    }


    @Parcel
    public static class CommonMasterData {

        @SerializedName("organization")
        public OrganizationEntity organization;

        @SerializedName("company")
        public CompanyEntity company;

        @SerializedName("lookups")
        public List<LookUpEntity> lookups;

        @SerializedName("currentBranch")
        public BranchEntity currentBranch;

        @SerializedName("countries")
        public List<CountryEntity> countries;

        @SerializedName("languages")
        public List<LanguageEntity> languages;

        @SerializedName("regions")
        public List<RegionEntity> regions;

        @SerializedName("branches")
        public List<BranchEntity> branches;

        @SerializedName("holidays")
        public List<HolidayEntity> holidays;

        @SerializedName("ranks")
        public List<RankEntity> ranks;

        @SerializedName("actionPlans")
        public List<ActionPlanEntity> actionPlans;

        @SerializedName("industrySectors")
        public List<IndustrySectorEntity> industrySectors;

        @SerializedName("deploymentTypes")
        public List<DeploymentTypeEntity> deploymentTypes;

        @SerializedName("taskTypes")
        public List<TaskTypeEntity> taskTypes;

        @SerializedName("siteTypes")
        public List<SiteTypeEntity> siteTypes;

        @SerializedName("clientHandshakeQuestions")
        public List<ClientHandShakeQuestionEntity> clientHandshakeQuestions;

        @SerializedName("kitItems")
        public List<KitItemEntity> kitItems;

        @SerializedName("attachmentMetadataDefinition")
        public List<AttachmentMetadataDefinitionEntity> metadataDefinition;

        //db insert response
        public List<Long> lookupRows;

        public List<Long> countryRows;

        public List<Long> languageRows;

        public List<Long> regionRows;

        public List<Long> holidayRows;

        public List<Long> rankRows;

        public List<Long> actionPlanRows;

        public List<Long> industrySectorRows;

        public List<Long> deploymentTypeRows;

        public List<Long> taskTypeRows;

        public List<Long> siteTypeRows;

        public List<Long> clientHandshakeQuestionRows;

        public List<Long> kitItemRows;


        public CommonMasterData() {
        }

    }
}

