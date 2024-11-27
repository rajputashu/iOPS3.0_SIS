package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import com.sisindia.ai.android.room.entities.CheckInOutEntity;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class DayNightCheckData {

    public List<PostCheckData> posts = new ArrayList<>();

    public List<GuardCheckData> guards = new ArrayList<>();

    public List<RegisterCheckData> registers = new ArrayList<>();

    public List<SecurityCheckData> securityRisks = new ArrayList<>();

    public List<PostCheckListData> postCheckLists = new ArrayList<>();

    public List<SiteCheckListData> siteCheckLists = new ArrayList<>();

    public List<SiteStrengthData> siteStrength = new ArrayList<>();

    public ClientHandShakeData clientHandShakeData;

    public CheckInOutEntity checkInOutData;

    public DayNightCheckData() {
    }

    public DayNightCheckData(List<PostCheckData> posts, List<GuardCheckData> guards, List<RegisterCheckData> registers, List<RegisterAttachmentCheckData> registerAttachments, List<SecurityCheckData> securityRisks, List<PostCheckListData> postCheckLists, List<SiteCheckListData> siteCheckLists, ClientHandShakeData clientHandShakeData) {
        this.posts = posts;
        this.guards = guards;
        this.registers = registers;
        this.securityRisks = securityRisks;
        this.postCheckLists = postCheckLists;
        this.siteCheckLists = siteCheckLists;
        this.clientHandShakeData = clientHandShakeData;
    }

    public DayNightCheckData(List<PostCheckData> posts, List<GuardCheckData> guards, List<RegisterCheckData> registers, List<RegisterAttachmentCheckData> registerAttachments, List<SecurityCheckData> securityRisks, List<PostCheckListData> postCheckLists, List<SiteCheckListData> siteCheckLists) {
        this.posts = posts;
        this.guards = guards;
        this.registers = registers;
        this.securityRisks = securityRisks;
        this.postCheckLists = postCheckLists;
        this.siteCheckLists = siteCheckLists;
    }

    @Parcel
    public static class PostCheckData {

        public int postId;

        public String postName;

        public int guardsChecked;

        public int registersChecked;

        public int postCheckList;

        public int totalGuards;

        public int totalRegisters;

        public int totalCheckList;

        public PostCheckData() {
        }
    }

    @Parcel
    public static class SiteStrengthData {

        public int strengthId;

        public int siteId;

        public int taskId;

        public int postId;

        public String grade;

        public int authorizedStrength;

        public Integer actualStrength;

        public int shiftId;

        public int rankId;

        public SiteStrengthData() {
        }
    }

    @Parcel
    public static class ClientHandShakeData {

        public int taskId;

        public int siteId;

        public boolean isMetClient;

        public String reason;

        public String clientName;

        public String contactNo;

        public String designation;

        public String clientId;

        public String clientEmail;

        public String feedbackStar;

        public int taskStatus;

        public String questions;

        public ClientHandShakeData() {
        }
    }

    @Parcel
    public static class SiteCheckListData {

        public int siteChecklistId;

        public String checklistLabel;

        public String checklistQuestionType;

        public int siteChecklistOptionId;

        public String optionLabel;

        public String optionResponseType;

        //        public int imageAttachmentId;
        public String imageAttachmentId;

        public int siteId;

        public int taskId;

        public SiteCheckListData() {
        }
    }

    @Parcel
    public static class PostCheckListData {

        public int postChecklistId;

        public String checklistLabel;

        public String checklistQuestionType;

        public int siteId;

        public int postId;

        public String optionLabel;

        public String optionResponseType;

        public int imageAttachmentId;

        public int taskId;

        public String remarks;

        public PostCheckListData() {
        }
    }

    @Parcel
    public static class RegisterAttachmentCheckData {

        public int postId;

        public int siteId;

        public int taskId;

        public int registerTypeId;

        public int registerAttachmentId;

        public String registerAttachmentGuid;

        public RegisterAttachmentCheckData() {
        }

        @Ignore
        public RegisterAttachmentCheckData(int postId, int siteId, int taskId, int registerTypeId, int registerAttachmentId, String registerAttachmentGuid) {
            this.postId = postId;
            this.siteId = siteId;
            this.taskId = taskId;
            this.registerTypeId = registerTypeId;
            this.registerAttachmentId = registerAttachmentId;
            this.registerAttachmentGuid = registerAttachmentGuid;
        }
    }

    @Parcel
    public static class RegisterCheckData {

        public int postId;

        public int siteId;

        public int taskId;

        public boolean isMissing;

        public int registerTypeId;

        public String registerType;

        public boolean isMandatory;

        @Ignore
        public List<RegisterAttachmentCheckData> registerAttachments = new ArrayList<>();

        public RegisterCheckData() {
        }
    }

    @Parcel
    public static class SecurityCheckData {

        public int categoryId;

        public int lookupIdentifier;

        public String remarks;

        public int siteId;

        public int taskId;

        public int postId;

        public int addSecurityRiskStatus;

        public String addSecurityGuid;


        public SecurityCheckData() {
        }
    }

    @Parcel
    public static class GuardCheckData {
        public String employeeName;
        public String employeeNo;
        public String employeeId;
        public String rewardFineValue;
        public String postName;
        public String guardEvaluationResult;
        public String mlGuardEvaluationResult;
        public String guardNotAvailableRemarks;
        public int rewardType;
        public int postId;
        public int siteId;
        public int taskId;
        public int guardStatus;
        public String sleepingGuardGuid;
        public String guardEvaluationGuid;
        public String guardSignatureGuid;
        public int turnOutScore;
        public int totalTurnOut;
        public int notAvailableStatus;

        //ADDING THIS FLAG TO DETECT WHETHER FAKE GUARD IMAGE WAS CLICKED OR NOT
        public boolean isFakeGuardImage;

        public GuardCheckData() {
        }
    }

    /*@Parcel
    public static class CheckInOutData {

        public int status; // Refer : CheckInStatus Enum for status values
        public String checkInDateTime;
        public String checkOutDateTime;
        public String checkInQrCode = "";
        public String checkOutQrCode = "";
        public String skipReason = "";

        public CheckInOutData() {

        }
    }*/
}
