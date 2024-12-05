package com.sisindia.ai.android.commons

/**
 * Created by Ashu Rajput on 3/7/2020.
 */
enum class RCViewType(val viewType: Int) {
    DAY(0), WEEKLY(1), MONTHLY(2);
}

enum class TaskStatus(val status: Int) {
    PENDING(1), COMPLETED(2);
}

enum class POAStatus(val status: Int) {
    PENDING(1), COMPLETED(2), CLOSED(3);
}

enum class CheckInStatus(val status: Int) {
    DEFAULT(0), CHECKED_IN(1), CHECKED_OUT(2), SKIPPED(3);
}

enum class CaptureImageType(val attachmentSourceTypeId: Int) {
    POST_PHOTO(1),
    AI_PROFILE(2),
    GUARD_FULL_PHOTO(3),
    GUARD_SIGNATURE(4),
    INDEPENDENT_GRIEVANCE(5),
    INDEPENDENT_COMPLAINT(7),
    DOCUMENT_SCAN(9),
    DUTY_REGISTER(10),
    VISITOR_REGISTER(11),
    MATERIAL_REGISTER(12),
    CLIENT_REGISTER(13),
    SITE_REGISTER(14),
    VEHICLE_REGISTER(15),
    ADD_SECURITY(16),
    POST_SPI_PHOTO(17),
    BILL_SUBMIT(19),
    BILL_COLLECTION(20),
    BARRACK_BED(21),
    BARRACK_MESS(22),
    BARRACK_KIT(23),
    BARRACK_OUTSIDE(24),
    BARRACK_PROFILE(25),
    IMPROVEMENT_PLAN(27),
    CLOSE_POA(28),
    SLEEPING_GUARD(29),
    DEPENDENT_GRIEVANCE(30),
    DEPENDENT_COMPLAINT(31),
    KIT_REQUEST_PHOTO(32),
    KIT_REQUEST_SIGNATURE(33),
    OTHER_TASK(34),
    LANDLORD(35),
    CUSTODIAN(36),
    SITE_CHECK(37),
    REGISTER_DOCUMENT(40),
    EDIT_SITE_POST(38),
    EDIT_SITE_POST_SPI(39),
    UNKNOWN(0);
}

enum class CollectionMode(val modeValue: Int) {
    CHEQUE(1), NEFT(2), RTGS(3), UPI(4);
}

enum class ChatWidgetEnum(val widgetId: Int) {
    ENTER_TEXT(1), TEXT(2), MULTI_LINE_TEXT(3), DUAL_BUTTON(4), LIST_BUTTON(5),
    DROPDOWN(6), DATE(7), TIME(8), DATETIME(9), IMAGE(10), FORM(11),
    AUDIO(12), VIDEO(13), NUMBER(14), API_TEXT(15), DYNAMIC_SELECT(16),
    RATING(17), FINISH(18);
}

enum class SyncPOA(val typeId: Int) {
    DEFAULT(0), AT_RISK(2), IMPROVEMENT_PLAN(3);
}

enum class NotificationMode(val mode: Int) {
    SILENT(1), READ_ACTION(2), NO_ACTION(3), SQL_DELETE(4), SQL_UPDATE(5), WEB_VIEW(6)
}

enum class TaskControllerType {
    LABEL, CHECKBOX, TEXT, PICTURE, SPINNER, STATICSPINNER, AUDIO, QRCODE,RATING, DATETIMEPICKER,
    RadioButton
}

enum class GroupCompany(val companyId: Int) {
    SIS(1), UNIQ(3), SLV(4);
}

enum class KitOTP(val kitOtpType: Int) {
    OTP_NOT_REQUIRED(1), VERIFY_SKIP_OTP(2), MANDATORY_OTP(3);
}