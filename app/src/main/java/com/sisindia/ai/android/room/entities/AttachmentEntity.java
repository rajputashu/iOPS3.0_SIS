package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.UUID;

@Parcel
@Entity
public class AttachmentEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    /**
     * save temp on capture
     */
    @SerializedName("localFilePath")
    public String localFilePath;

    @SerializedName("attachmentMetaData")
    public String attachmentMetaData;

    @SerializedName("isAttachmentSync")
    public boolean isAttachmentSync;

    @SerializedName("attachmentGuid")
    public String attachmentGuid;

    @SerializedName("attachmentSourceType")
    public int attachmentSourceType;

    @SerializedName("isFileSaved")
    public boolean isFileSaved;

    @SerializedName("fileSize")
    public long fileSize;

    @SerializedName("isDone")
    public boolean isDone;

    /**
     * save on attachment sync
     */
    @SerializedName("localFileName")
    public String localFileName;

    @SerializedName("storagePath")
    public String storagePath;

    @SerializedName("capturedDateTime")
    public String capturedDateTime = "";

    @SerializedName("imageText")
    public String imageText = "";

    @SerializedName("isSelfieApiDone")
    public boolean isSelfieApiDone;

    public boolean isFakeGuardImage = false;

    public AttachmentEntity() {
    }

    @Ignore
    public AttachmentEntity(String localFilePath) {
        this.localFilePath = localFilePath;
        this.isAttachmentSync = false;
        this.capturedDateTime = LocalDateTime.now().toString();
    }

    @Ignore
    public AttachmentEntity(String localFilePath, String attachmentMetaData) {
        this.localFilePath = localFilePath;
        this.attachmentMetaData = attachmentMetaData;
        this.isAttachmentSync = false;
        this.capturedDateTime = LocalDateTime.now().toString();
    }

    @Ignore
    public AttachmentEntity(AttachmentSourceType attachmentSourceType) {
        this.attachmentSourceType = attachmentSourceType.sourceType;
        this.attachmentGuid = UUID.randomUUID().toString();
        this.capturedDateTime = LocalDateTime.now().toString();
    }

    public enum AttachmentSourceType {
        POST_PHOTO(1, "sourceName"),
        AI_PROFILE(2, "sourceName"),
        GUARD_FULL_PHOTO(3, "Guard Full Photo"),
        GUARD_SIGNATURE(4, "Add Signature"),
        INDEPENDENT_GRIEVANCE(5, "sourceName"),
        INDEPENDENT_COMPLAINT(7, "sourceName"),
        DOCUMENT_SCAN(9, "sourceName"),
        DUTY_REGISTER(10, "Duty Register"),
        VISITOR_REGISTER(11, "Visitor Register"),
        MATERIAL_REGISTER(12, "Material Register"),
        CLIENT_REGISTER(13, "Client Register"),
        SITE_REGISTER(14, "Site Register"),
        VEHICLE_REGISTER(15, "Vehicle Register"),
        ADD_SECURITY(16, "Add Security"),
        POST_SPI_PHOTO(17, "sourceName"),
        BILL_SUBMIT(19, "sourceName"),
        BILL_COLLECTION(20, "sourceName"),
        BARRACK_BED(21, "sourceName"),
        BARRACK_MESS(22, "sourceName"),
        BARRACK_KIT(23, "sourceName"),
        BARRACK_OUTSIDE(24, "sourceName"),
        BARRACK_PROFILE(25, "sourceName"),
        IMPROVEMENT_PLAN(27, "sourceName"),
        CLOSE_POA(28, "sourceName"),
        SLEEPING_GUARD(29, "Guard Sleeping"),
        DEPENDENT_GRIEVANCE(30, "sourceName"),
        DEPENDENT_COMPLAINT(31, "sourceName"),
        KIT_REQUEST_PHOTO(32, "Kit Request Photo"),
        KIT_REQUEST_SIGNATURE(33, "sourceName"),
        OTHER_TASK(34, "Other Tasks"),
        LANDLORD(35, "sourceName"),
        CUSTODIAN(36, "sourceName"),
        SITE_CHECK(37, "Site Check"),
        REGISTER_DOCUMENT(40, "sourceName"),
        EDIT_SITE_POST(38, "sourceName"),
        EDIT_SITE_POST_SPI(39, "sourceName"),
        SITE_CHECKLIST(71, "SiteCheckList"),
        SELFIE(101, "sourceName"),
        KIT_DISTRIBUTE_PHOTO(102, "Kit Request Photo"),
        KIT_DISTRIBUTE_SIGNATURE(103, "sourceName"),
        KIT_VOUCHER_PHOTO(104, "KitVoucher"),
        LOCAL_DB(105, "Local DB"),
        MASK_GUARD_PHOTO(106, "Local DB"),
        DYNAMIC_IMAGE(107, ""),
        DYNAMIC_AUDIO(108, ""),
        EMERGENCY_PHOTO(109, "EmergencyAlarm"),
        EMERGENCY_AUDIO(110, "EmergencyAlarm"),
        DISBANDMENT_IMAGE(111, "DISBANDMENT"),
        PRACTO_APP_INSTALL(112, "PRACTO_APP"),
        PRACTO_APP_NOT_INSTALL(113, "PRACTO_APP"),
        UNKNOWN(0, "sourceName");

        private final int sourceType;
        private final String sourceName;

        AttachmentSourceType(int sourceType, String sourceName) {
            this.sourceType = sourceType;
            this.sourceName = sourceName;
        }

        public static AttachmentSourceType of(int sourceType) {
            for (AttachmentSourceType type : AttachmentSourceType.values()) {
                if (type.sourceType == sourceType) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public int getSourceType() {
            return sourceType;
        }

        public String getSourceName() {
            return sourceName;
        }
    }
}

