package com.sisindia.ai.android.rest;

import com.google.gson.JsonObject;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.commons.RequestHeaderInterceptor;
import com.sisindia.ai.android.features.disband.AddDisbandmentBodyMO;
import com.sisindia.ai.android.features.disband.DashboardSitesResponseMO;
import com.sisindia.ai.android.models.AddClientApiBodyMO;
import com.sisindia.ai.android.models.AppVersionResponseMO;
import com.sisindia.ai.android.models.BillCollectionApiBodyMO;
import com.sisindia.ai.android.models.BillsForCollectionResponse;
import com.sisindia.ai.android.models.CommonResponse;
import com.sisindia.ai.android.models.ComplaintResponse;
import com.sisindia.ai.android.models.ConveyanceResponse;
import com.sisindia.ai.android.models.DeviceInfo;
import com.sisindia.ai.android.models.EmployeeAndRewardFineResponse;
import com.sisindia.ai.android.models.EmployeeResponse;
import com.sisindia.ai.android.models.EventsAPIResponseMO;
import com.sisindia.ai.android.models.GrievanceResponse;
import com.sisindia.ai.android.models.ReviewInformationResponse;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.models.UserOnBoardModel;
import com.sisindia.ai.android.models.civil.AddNominationBodyMO;
import com.sisindia.ai.android.models.civil.CivilNominationResponseMO;
import com.sisindia.ai.android.models.civil.NominationSummaryResponseMO;
import com.sisindia.ai.android.models.kits.KitDistributionApiBodyMO;
import com.sisindia.ai.android.models.kits.KitDistributionResponseMO;
import com.sisindia.ai.android.models.mask.AddMaskRequestBodyMO;
import com.sisindia.ai.android.models.mask.MaskApiResponseMO;
import com.sisindia.ai.android.models.nudges.NudgeRequestBodyMO;
import com.sisindia.ai.android.models.performance.AOConveyanceSummaryResponseMO;
import com.sisindia.ai.android.models.performance.ConveyanceReportResponseMO;
import com.sisindia.ai.android.models.performance.IncentiveResponseMO;
import com.sisindia.ai.android.models.performance.PerformanceEffortsApiResponseMO;
import com.sisindia.ai.android.models.performance.PerformanceResultApiResponseMO;
import com.sisindia.ai.android.models.poa.ClosePoaApiBodyMO;
import com.sisindia.ai.android.models.poa.ImprovementPoaResponseMO;
import com.sisindia.ai.android.models.poa.MonthlyAtRiskPoaResponseMO;
import com.sisindia.ai.android.models.profile.AIProfileDataResponse;
import com.sisindia.ai.android.models.profile.AIProfileUpdateBodyMO;
import com.sisindia.ai.android.models.recruit.AddedRecruitmentApiResponse;
import com.sisindia.ai.android.models.recruit.RecruitmentApiBodyMO;
import com.sisindia.ai.android.models.recruit.RecruitmentApiResponseMO;
import com.sisindia.ai.android.models.register.MapRegistersResponseMO;
import com.sisindia.ai.android.models.rota.InActiveRotaResponseMO;
import com.sisindia.ai.android.models.rota.RotaComplianceApiResponse;
import com.sisindia.ai.android.models.rota.RotaResponse;
import com.sisindia.ai.android.models.sales.RaisedSiteResponseMO;
import com.sisindia.ai.android.models.sales.SalesAddBodyMO;
import com.sisindia.ai.android.models.sales.SalesRefApiResponseMO;
import com.sisindia.ai.android.models.sales.SalesRefSectorsResponseMO;
import com.sisindia.ai.android.models.sales.ValidateRaisedCodeResponse;
import com.sisindia.ai.android.models.sas.SasApiResponse;
import com.sisindia.ai.android.models.site.AddEditPostsMO;
import com.sisindia.ai.android.models.site.MySitesResponseMO;
import com.sisindia.ai.android.models.site.SiteTaskSummaryResponseMO;
import com.sisindia.ai.android.room.entities.ComplaintEntity;
import com.sisindia.ai.android.room.entities.DutyStatusEntity;
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity;
import com.sisindia.ai.android.room.entities.GrievanceEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.uimodels.akr.GuardKitRequestMO;
import com.sisindia.ai.android.uimodels.attachments.AKRAttachmentMetadata;
import com.sisindia.ai.android.uimodels.attachments.SelfieAttachmentMetadata;
import com.sisindia.ai.android.uimodels.barracks.BarrackUpdateBodyMO;
import com.sisindia.ai.android.uimodels.nudges.PendingNudgesResponseMO;
import com.sisindia.ai.android.uimodels.tasks.ClientDetailsResponseMO;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CoreApi {

    String IS_DUTY_ON = "api/Employee/DutyONOFF";
    //    String DAILY_TIME_LINE = "api/TimeLine/Add";
    //    String GET_EMPLOYEE_REWARD_SUMMARY = "api/Employee/GetRewardFineSummary";
//    String AZURE_SHARED_SAS_TOKEN = "api/Storage/GetAzSASToken";
//    String EMPLOYEE_SITES = "api/Employee/EmployeeSites";
    String ISSUE_SUMMARY = "api/Complaint/IssueSummary";
    String GET_EMPLOYEE_DETAIL = "api/Employee/GetEmployee";
    String ADD_REWARD_FINE = "api/Employee/AddRewardFine";
    String ADD_GRIEVANCE = "api/Grievance/Add";
    String ADD_COMPLAINT = "api/Complaint/Add";
    String UPDATE_COMPLAINT = "api/Complaint/Update";//https://iops2core-uat.azurewebsites.net/api/Complaint/Update
    String ADD_OR_UPDATE_SITE_TASK = "api/SiteTask/AddSiteTask";
    String ADD_KIT_REQUEST = "api/Kit/AddKitRequest";
    String GET_DASHBOARD_ROTA = "api/Rota/GetRota";
    String GET_ROTA_VIA_WEEK = "api/Rota/GetRota";
    String GET_COMPLAINTS = "api/Complaint/GetComplaints";
    String GET_GRIEVANCES = "api/Grievance/GetGrievances";
    String ADD_DUTY_POST = "api/DutyPost/Add";
    String ADD_CLIENT = "api/CustomerContact/Add";
    String CLOSE_POA = "api/SiteRisk/ClosePoA";
    String AI_PROFILE = "api/Employee/AIProfile";
    String UPDATE_EMPLOYEE = "api/Employee/UpdateEmployee";
    String ADD_RECRUIT = "api/Recruitment/Add";
    String YEARLY_SUMMARY_RECRUITS = "api/Recruitment/YearlySummaryRecruits";
    String UPDATE_DEVICE_INFO = "api/Account/UpdateDeviceInfo";
    String GET_CURRENT_VERSION = "api/Account/GetCurrentVersion";
    String AZURE_USER_SAS_TOKEN = "api/Storage/GetSASToken";
    String CLIENT_OTP = "api/CustomerContact/SendOTP";
    String KIT_OTP = "api/Kit/SendKitDistributedOTP";
    String SUBMIT_CLIENT_OTP = "api/CustomerContact/SubmitOTP";
    String SEND_OTP = "api/Account/SendOtp";
    String UPDATE_BILL_COLLECTION = "api/BillCollection/UpdateBill";
    String PERFORMANCE_RESULT = "api/Performance/GetPerformanceResults";
    String PERFORMANCE_EFFORTS = "api/Performance/GetPerformanceEfforts";
    String PERFORMANCE_ROTA_COMPLIANCE = "api/Rota/GetRotaSummary";
    String REFRESHED_SITE_LIST = "api/Site/GetSiteMasters";
    //    String KIT_DISTRIBUTION_LIST = "api/Kit/GetKitDistributionList";
    String KIT_DISTRIBUTION_LIST = "api/Kit/GetKitDistributionList_V1";
    String UPDATE_KIT_DISTRIBUTION_LIST = "/api/Kit/UpdateKitDistribution";
    String AO_CONVEYANCE = "api/TimeLine/GetAOConveyance";
    String MONTHLY_AT_RISK_POA = "api/SiteRisk/GetLatestSiteAtRisks";
    String BILL_SUBMISSION_ROTAS = "api/Rota/GetBSRota";
    String MON_INPUT_ROTAS = "api/Rota/GetMonInputRota";
    String SEND_ROTAS_TO_SERVER = "api/Rota/SyncRota";
    String GET_BILLS_FOR_COLLECTION = "api/BillCollection/GetBills";
    String GET_SITE_REGISTERS = "api/Register/GetSiteRegister";
    String GET_SITE_TASK_SUMMARIES = "api/Site/GetAOSiteTaskSummaries";
    String GET_AI_IMPROVEMENT_PLANS = "api/ImprovementPlan/AIImprovementPlans";
    String CLOSE_IMPROVEMENT_PLAN = "api/ImprovementPlan/CloseImprovementPlan";
    String AO_MONTHLY_CONVEYANCE_REPORT = "api/ConveyanceApproval/GetAreaOfficerReports";
    String AO_CONVEYANCE_SUMMARY = "api/ConveyanceApproval/GetAOConveyanceSummary";
    //    String GET_QUESTION_JSON = "api/Raising/GetAllRaisingQuestion";
    String POST_DUTY_ON_ATTACHMENT = "api/Attachment/Add";
    String GET_EMPLOYEE_MASTER_DATA = "api/Employee/GetEmployeeMaster";
    String GET_SALES_REFERENCE_DATA = "api/SalesRecommendation/Get";
    String ADD_SALES_REFERENCE = "api/SalesRecommendation/Add";
    String UPDATE_SALES_REFERENCE = "api/SalesRecommendation/UpdateStatus";
    String GET_SALES_REFERENCE_SECTORS = "api/SalesRecommendation/GetSectors";
    String GET_SITES_VIA_SITE_CODE = "api/SalesRecommendation/GetSite";
    String VALIDATE_RAISED_CODE = "api/Site/GetRaisedUnitCode";
    String GET_MASK_DISTRIBUTION_LIST = "api/MaskDistribution/GetList";
    String ADD_MASK_DISTRIBUTION = "api/MaskDistribution/Add";
    String UPDATE_BARRACK = "api/Barrack/Update";
    String GET_INACTIVE_ROTA = "api/Rota/GetInActiveRota";
    String GET_INCENTIVE = "api/Employee/GetAreaOfficerIncentives";
    String GET_EVENTS_LIST = "api/Employee/GetEvent";
    String GET_SITE_DISBANDMENT = "api/SiteDisbandment/GetSiteDisbandmentSummary";
    String ADD_DISBANDMENT_SITE = "api/SiteDisbandment/AddRequests";
    //    String GET_CLIENT_LIST = "api/Customer/GetCustomerContactsV1";
    String GET_CLIENT_LIST = "api/Customer/GetCustomerContactsBySiteId";
    String UPDATE_NUDGE_RESPONSE = "api/NudgeNotification/UpdatedResponse";
    String GET_PENDING_NUDGES = "api/NudgeNotification/GetNudgeNotificationResponses";
    String GET_STATE_DISTRICT = "api/DefenceNomination/GetCDNDropDown";
    String GET_NOMINATION_SUMMARY = "api/DefenceNomination/NomanitionSummary";
    String ADD_UPDATE_NOMINATION = "api/DefenceNomination/AddUpdate";

    @POST(UPDATE_DEVICE_INFO)
    Single<JsonObject> updateDeviceInfo(@Body DeviceInfo item);

    @POST(IS_DUTY_ON)
    Single<TableSyncResponse> isDutyOnOff(@Body DutyStatusEntity item);

    /*@POST(DAILY_TIME_LINE)
    Single<TableSyncResponse> dailyTimeLine(@Body DailyTimeLineEntity item);*/

    @GET(ISSUE_SUMMARY)
    Single<ReviewInformationResponse> getIssueSummary(@Query("siteid") int siteId);

    @GET(GET_EMPLOYEE_DETAIL)
    Single<EmployeeResponse> getEmployeeByEmployeeNo(@Query("employeeNo") String employeeNo);

    /*@GET(GET_EMPLOYEE_REWARD_SUMMARY)
    Single<EmployeeRewardSummaryResponse> getEmployeeRewardSummary(@Query("employeeNo") String employeeNo);*/

    @GET(AO_CONVEYANCE)
    Single<ConveyanceResponse> getAOConveyance(@Query("date") String dateTime);

    @POST(ADD_DUTY_POST)
    Single<TableSyncResponse> addEditPosts(@Body AddEditPostsMO item);

    @POST(ADD_OR_UPDATE_SITE_TASK)
    Single<TableSyncResponse> addOrUpdateCreatedTask(@Body TaskEntity item);

    @POST(ADD_CLIENT)
    Single<BaseNetworkResponse> addClient(@Body AddClientApiBodyMO item);

    @POST(CLOSE_POA)
    Single<CommonResponse> updateClosedPOA(@Body ClosePoaApiBodyMO item);

    @POST(ADD_REWARD_FINE)
    Single<TableSyncResponse> addRewardFine(@Body EmployeeFineRewardEntity item);

    @POST(ADD_GRIEVANCE)
    Single<TableSyncResponse> addGrievance(@Body GrievanceEntity item);

    /*@POST(ADD_KIT_REQUEST)
    Single<TableSyncResponse> addKitRequest(@Body GuardKitRequestEntity item);*/

    @POST(ADD_KIT_REQUEST)
    Single<TableSyncResponse> addKitRequest(@Body GuardKitRequestMO item);

    @POST(ADD_COMPLAINT)
    Single<TableSyncResponse> addComplaint(@Body ComplaintEntity item);

    @POST(UPDATE_COMPLAINT)
    Single<TableSyncResponse> updateComplaint(@Body ComplaintEntity item);

    @GET(AI_PROFILE)
    Single<AIProfileDataResponse> fetchAIProfile();

    @GET(GET_DASHBOARD_ROTA)
    Single<RotaResponse> getServerRotas();

    @GET(GET_ROTA_VIA_WEEK)
    Single<RotaResponse> getServerRotasViaWeek(@Query("isLastWeek") boolean isLastWeek);

    @POST(UPDATE_EMPLOYEE)
    Single<CommonResponse> updateAIProfile(@Body AIProfileUpdateBodyMO item);

    @POST(ADD_RECRUIT)
    Single<AddedRecruitmentApiResponse> addRecruitment(@Body RecruitmentApiBodyMO item);

    @GET(YEARLY_SUMMARY_RECRUITS)
    Single<RecruitmentApiResponseMO> getRecruitmentDetails();

    @GET(GET_GRIEVANCES)
    Single<GrievanceResponse> getGrievances();

    @GET(GET_COMPLAINTS)
    Single<ComplaintResponse> getComplaints();

    /*@GET(AZURE_SHARED_SAS_TOKEN)
    Single<SasApiResponse> getAzureSasSharedContainerToken();*/

    @GET(AZURE_USER_SAS_TOKEN)
    Single<SasApiResponse> getAzureSasUserContainerToken();

    @POST(CLIENT_OTP)
    Single<BaseNetworkResponse> requestClientOTP(@Query("mobileNumber") String clientNo);

    @POST(KIT_OTP)
    Single<BaseNetworkResponse> requestKitOTP(@Query("mobileNumber") String mobNumber);

    @POST(SUBMIT_CLIENT_OTP)
    Single<BaseNetworkResponse> submitClientOTP(@Query("mobileNumber") String clientNo,
                                                @Query("OTP") String otp);

    @POST(UPDATE_BILL_COLLECTION)
    Single<CommonResponse> updateBillCollection(@Body BillCollectionApiBodyMO item);

    @POST(SEND_OTP)
    Single<BaseNetworkResponse> sendOtp(@Header(RequestHeaderInterceptor.HeaderConstants.AUTORIZATION_KEY) String preAuthToken,
                                        @Body UserOnBoardModel item);

    @GET(PERFORMANCE_RESULT)
    Single<PerformanceResultApiResponseMO> getPerformanceResultDetails(@Query("searchKey") int key);

    @GET(PERFORMANCE_EFFORTS)
    Single<PerformanceEffortsApiResponseMO> getPerformanceEffortsDetails(@Query("searchKey") int key);

    @GET(PERFORMANCE_ROTA_COMPLIANCE)
    Single<RotaComplianceApiResponse> getPerformanceRotaCompliance();

    @GET(REFRESHED_SITE_LIST)
    Single<MySitesResponseMO> getRefreshedSiteList();

    @GET(KIT_DISTRIBUTION_LIST)
    Single<KitDistributionResponseMO> getKitDistributionList();

    @POST(UPDATE_KIT_DISTRIBUTION_LIST)
    Single<CommonResponse> updateKitDistribution(@Body KitDistributionApiBodyMO body);

    /*@GET(EMPLOYEE_SITES)
    Single<EmployeeSitesResponseMO> getEmployeeSites();*/

    /*
    @GET(SITE_RAISING_TIMELINES)
    Single<RaisingActivitiesListResponse> getSiteRaisingTimeLines(@Query("siteId") int siteId);

    @GET(SITE_FOR_RAISING)
    Single<SiteForRaisingResponseMO> getSitesForRaisingDetails();

    @POST(RAISING_UPDATE_BARRACK)
    Single<BaseNetworkResponse> updateRaisingBarrack(@Body UpdateRaisingBarrackBodyMO body);

    @POST(RAISING_UPDATE_KITTING)
    Single<BaseNetworkResponse> updateRaisingKitting(@Body UpdateKittingBodyMO body);

    @POST(RAISING_UPDATE_MTRAINING)
    Single<BaseNetworkResponse> updateRaisingMTraining(@Body UpdateMTrainingBodyMO body);

    @POST(RAISING_UPDATE_CLIENT_MEETING)
    Single<BaseNetworkResponse> updateRaisingClientMeeting(@Body UpdateClientMeetingBodyMO body);*/

    @GET(MONTHLY_AT_RISK_POA)
    Single<MonthlyAtRiskPoaResponseMO> getMonthlyAtRiskPOAs(@Query("date") String currentDate);

    @GET(BILL_SUBMISSION_ROTAS)
//    Single<RotaResponse> getWeeklyBillRotas(@Query("weekNo") int weekNo);
    Single<RotaResponse> getMonthlyBillRotas(@Query("date") String currentDate);

    @GET(GET_BILLS_FOR_COLLECTION)
//    Single<BillsForCollectionResponse> getMonthlyBillCollectionRotas(@Query("date") String currentDate);
    Single<BillsForCollectionResponse> getMonthlyBillCollectionRotas();

    @GET(MON_INPUT_ROTAS)
//    Single<RotaResponse> getWeeklyMonInputRotas(@Query("weekNo") int weekNo);
    Single<RotaResponse> getMonthlyMonInputRotas(@Query("date") String currentDate);

    @POST(SEND_ROTAS_TO_SERVER)
    Single<BaseNetworkResponse> sendAllTaskToServer(@Body List<TaskEntity> taskList);

    @GET(GET_SITE_REGISTERS)
    Single<MapRegistersResponseMO> getSiteRegisters(@Query("siteid") int siteId);

    @GET(GET_SITE_TASK_SUMMARIES)
    Single<SiteTaskSummaryResponseMO> getSiteTaskSummaries(@Query("searchKey") int key);

    @GET(GET_AI_IMPROVEMENT_PLANS)
    Single<ImprovementPoaResponseMO> getAiImprovementPlans();

    @POST(CLOSE_IMPROVEMENT_PLAN)
    Single<CommonResponse> updateClosedImprovementPOA(@Query("planId") int planId, @Query("resolution") String remarks);

    @GET(AO_MONTHLY_CONVEYANCE_REPORT)
    Single<ConveyanceReportResponseMO> getAoMonthlyConveyanceReport(@Query("areaofficerId") int aoId, @Query("month") int month,
                                                                    @Query("year") int year);

    @GET(AO_CONVEYANCE_SUMMARY)
    Single<AOConveyanceSummaryResponseMO> getAOConveyanceSummary(@Query("aoId") int aoId, @Query("dateTime") String dateTime);

    @GET(GET_CURRENT_VERSION)
    Single<AppVersionResponseMO> getCurrentVersion(@Query("appTypeId") int appTypeId);

    /*@GET(GET_QUESTION_JSON)
    Single<ChatQuestionsResponseMO> getChatQuestionsJson();*/

    @POST(POST_DUTY_ON_ATTACHMENT)
    Single<BaseNetworkResponse> pushDutyOnMetaDataAttachment(@Body SelfieAttachmentMetadata metaData);

    @POST(POST_DUTY_ON_ATTACHMENT)
    Single<BaseNetworkResponse> pushAKRMetaDataAttachment(@Body AKRAttachmentMetadata metaData);

    @GET(GET_EMPLOYEE_MASTER_DATA)
    Single<EmployeeAndRewardFineResponse> getEmployeeMasterData();

    @GET(GET_SALES_REFERENCE_SECTORS)
    Single<SalesRefSectorsResponseMO> getSalesRefSectors();

    @GET(GET_SALES_REFERENCE_DATA)
    Single<SalesRefApiResponseMO> getSalesReferenceData(@Query("month") int month, @Query("year") int year);

    @POST(ADD_SALES_REFERENCE)
    Single<BaseNetworkResponse> addSalesReference(@Body SalesAddBodyMO salesAddBodyMO);

    @POST(UPDATE_SALES_REFERENCE)
//    Single<BaseNetworkResponse> updateSalesReference(@Body SalesUpdateBodyMO salesUpdateBodyMO);
    Single<BaseNetworkResponse> updateSalesReference(@Query("id") int id, @Query("status") int status, @Query("raisedUnitCode") String raisedUnitCode);

    @GET(GET_SITES_VIA_SITE_CODE)
    Single<RaisedSiteResponseMO> getSitesViaSiteCode(@Query("siteCode") String siteCode);

    @GET(VALIDATE_RAISED_CODE)
    Single<ValidateRaisedCodeResponse> validateRaisedCode(@Query("unitCode") String unitCode);

    @GET(GET_MASK_DISTRIBUTION_LIST)
    Single<MaskApiResponseMO> getMaskDistributionList();

    @POST(ADD_MASK_DISTRIBUTION)
    Single<BaseNetworkResponse> addMaskDistribution(@Body AddMaskRequestBodyMO maskBody);

    @GET(GET_INACTIVE_ROTA)
    Single<InActiveRotaResponseMO> getInActiveRota();

    @POST(UPDATE_BARRACK)
    Single<BaseNetworkResponse> updateBarrackDetails(@Body BarrackUpdateBodyMO barrackTagBody);

    @GET(GET_INCENTIVE)
    Single<IncentiveResponseMO> getIncentive(@Query("month") int month, @Query("year") int year);

    @GET(GET_EVENTS_LIST)
    Single<EventsAPIResponseMO> getEventsList(@Query("employeeNo") String employeeNo);
//    https://iops2core-uat.azurewebsites.net/api/Employee/GetEvent?employeeNo=BEL019423

    @GET(GET_SITE_DISBANDMENT)
    Single<DashboardSitesResponseMO> getSiteDisbandmentDetails(@Query("employeeId") int employeeId);

    @POST(ADD_DISBANDMENT_SITE)
    Single<BaseNetworkResponse> addDisbandmentSite(@Body List<AddDisbandmentBodyMO> body);

    @GET(GET_CLIENT_LIST)
    Single<ClientDetailsResponseMO> getClientDetails(@Query("siteId") int siteId);

    @POST(UPDATE_NUDGE_RESPONSE)
    Single<BaseNetworkResponse> updateNudgeResponse(@Body NudgeRequestBodyMO body);

    @GET(GET_PENDING_NUDGES)
    Single<PendingNudgesResponseMO> getPendingNudges(@Query("date") String date);

    @GET(GET_STATE_DISTRICT)
    Single<CivilNominationResponseMO> getStateDistrict();

    @GET(GET_NOMINATION_SUMMARY)
    Single<NominationSummaryResponseMO> getNominationSummary();

    @POST(ADD_UPDATE_NOMINATION)
    Single<BaseNetworkResponse> addUpdateNomination(@Body AddNominationBodyMO body);
}