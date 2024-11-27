package com.sisindia.ai.android.utils;

import android.annotation.SuppressLint;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    //    public static String DASHBOARD_DD_MMM = "'Dashboard' dd MMM";
//    public static String DASHBOARD_DD_MMM = "EEEE dd MMM";
//    public static String BILL_DUE_DATE = "MMM- yyyy";
    public static String CREATE_TASK_DATE = "dd MMM'' yyyy";
    public static String BILL_COLLECTION_DATE = "dd MMM'' yyyy";
    public static String CREATE_TASK_TIME = "hh:mm a";
    public static String TARGET_DATE = "dd MMM'' yyyy";
    public static String KIT_REQUEST_DATE_TIME = "dd MMM'' yyyy";
    public static String LAST_VISIT_DATE_TIME = "dd MMM'' yyyy";
    public static String DATE_OF_BIRTH = "yyyy-MM-dd'T00:00:00'";
    public static String FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /*public static Boolean compareDateWithCurrentDate(String lastLaunchDate) {
        if (TextUtils.isEmpty(lastLaunchDate))
            return false;

        LocalDateTime localDate = LocalDateTime.parse(lastLaunchDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime currentDate = LocalDateTime.now();

        return localDate.getMonth().name().equals(currentDate.getMonth().name())
                && localDate.getDayOfMonth() == currentDate.getDayOfMonth()
                && localDate.getYear() == currentDate.getYear();
    }*/

    /*public static String getCurrentDateForRotaScreen() {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DASHBOARD_DD_MMM));
    }

    public static long getCurrentDateTimeInMilli() {
        return ZonedDateTime.now().toLocalDateTime().toInstant(ZoneOffset.UTC).toEpochMilli();
    }*/

    /**
     * //     * @param  If true: local to utc/If false: utc to local
     */
   /* private static String doTimeConversion(String time, boolean type) {
        ZoneId localZone = ZoneId.systemDefault();
        LocalTime lt = LocalTime.parse(time);
        LocalDateTime ldt = LocalDate.now(localZone).atTime(lt);
        ZonedDateTime resultTime;
        if (type) {
            resultTime = ldt.atZone(localZone).withZoneSameInstant(ZoneOffset.UTC);
        } else {
            resultTime = ldt.atOffset(ZoneOffset.UTC).atZoneSameInstant(localZone);
        }
        LocalTime newTime = resultTime.toLocalTime();
        return newTime.toString();
    }*/

    /*public static long getCurrentEpochUTC() {
        return Instant.now(Clock.systemUTC()).toEpochMilli();
    }

    public static String deviceDateTimeString(long epochMilliUtc) {
        Instant instant = Instant.ofEpochMilli(epochMilliUtc);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm:ss a", Locale.US).withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    public static String uTCDateTimeString(long epochMilliUtc) {
        Instant instant = Instant.ofEpochMilli(epochMilliUtc);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm:ss a", Locale.US).withZone(ZoneId.of("UTC"));
        return formatter.format(instant);
    }

    public static long convertDateStringToLongUTC(String stringUTCDate, DateTimeFormatter inputDateFormat) {
        LocalDateTime localDate = LocalDateTime.parse(stringUTCDate, inputDateFormat);
        long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        return timeInMilliseconds;
    }*/
    @SuppressLint("SimpleDateFormat")
    public static String convertIntSecondsToHHMMSS(int sec) {
        Date d = new Date(sec * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    @SuppressLint("SimpleDateFormat")
    public static String dutyOnOffWaitingTime(int sec) {
        Date d = new Date(sec * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("'Wait 'mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    public static String TASK_DATE_FORMAT(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(CREATE_TASK_DATE));
    }

    public static String COLLECTION_DATE_FORMAT(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(BILL_COLLECTION_DATE));
    }

    public static String DOB_DATE_FORMAT(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_OF_BIRTH));
    }

    public static String TASK_TIME_FORMAT(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(CREATE_TASK_TIME));
    }

    /*public static String DD_MM_YYYY_FORMAT(LocalDate time) {
        return time.format(DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY));
    }*/

    /*public static String DD_MM_YYYY_FORMAT(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY));
    }*/

    public static String YYYY_MM_DD_FORMAT(LocalDate time) {
        return time.format(DateTimeFormatter.ofPattern(FORMAT_YYYY_MM_DD));
    }

   /* public static String KIT_REQUESTED_DATE_TIME(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern(KIT_REQUEST_DATE_TIME));
    }*/

   /* public static LocalDateTime toZone(final LocalDateTime time, final ZoneId fromZone, final ZoneId toZone) {
        final ZonedDateTime zonedtime = time.atZone(fromZone);
        final ZonedDateTime converted = zonedtime.withZoneSameInstant(toZone);
        return converted.toLocalDateTime();
    }*/

   /* public static LocalDateTime toUtc(final LocalDateTime time, final ZoneId fromZone) {
        return toZone(time, fromZone, ZoneOffset.UTC);
    }

    public static LocalDateTime toUtc(final LocalDateTime time) {
        return toUtc(time, ZoneId.systemDefault());
    }*/

    /*public static LocalDateTime toLocal(LocalDateTime utcDateTime) {
        return ZonedDateTime.ofInstant(utcDateTime.atZone(ZoneOffset.UTC).toInstant(), ZoneId.systemDefault()).toLocalDateTime();
    }*/

   /* public static LocalDateTime getLocalDateTimeFromUtcStr(String utcStrDate) {
        LocalDateTime dateTime = LocalDateTime.parse(utcStrDate, DateTimeFormatter.ISO_DATE_TIME);
        return toLocal(dateTime);
    }

    public static boolean isNewTaskInBetweenTwoLocalDateTime(LocalDateTime newStart, LocalDateTime newEnd,
                                                             LocalDateTime oldStart, LocalDateTime oldEnd) {

        if (newStart.toLocalDate().getMonth().equals(oldStart.toLocalDate().getMonth()) &&
                newStart.toLocalDate().getDayOfMonth() == (oldStart.toLocalDate().getDayOfMonth())) {

            if (newStart.toLocalTime().equals(oldStart.toLocalTime())) { // same hour and same minutes
                return true;
            }
            if ((newStart.isAfter(oldStart) && newStart.isBefore(oldEnd)) || (oldStart.isAfter(newStart) && oldStart.isBefore(newEnd))) {
                return true;
            }
            return (newEnd.isAfter(oldStart) && newEnd.isBefore(oldEnd)) || (oldEnd.isAfter(newStart) && oldEnd.isBefore(newEnd));
        }
        return false;
    }*/

    public static String getFormatDateTime(String serverDateTime) {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());*/

        if (serverDateTime != null && !serverDateTime.isEmpty())
            return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.parse(serverDateTime));
        return serverDateTime;
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public static String DASHBOARD_DATE_FORMAT(LocalDate date) {
        String rotaDateFormat = "EEEE dd'" + getDayNumberSuffix(date.getDayOfMonth()) + "' MMM";
        return date.format(DateTimeFormatter.ofPattern(rotaDateFormat));
    }

    public static String formatToDDMMYYYY(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty())
            return DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY).format(LocalDate.parse(dateTime));
        return dateTime;
    }

    public static String formatServerDateToDDMMYYYY(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty())
            return DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY).format(LocalDateTime.parse(dateTime));
        return dateTime;
    }

    public static String formatServerDateToYYYYDDMM(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty())
            return DateTimeFormatter.ofPattern(FORMAT_YYYY_MM_DD).format(LocalDateTime.parse(dateTime));
        return dateTime;
    }

    public static int getCurrentWeekOfYear() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }

    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13)
            return "th";

        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static LocalDateTime getServerTimeFromStringDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return LocalDateTime.parse(dateString, formatter);
    }
}
