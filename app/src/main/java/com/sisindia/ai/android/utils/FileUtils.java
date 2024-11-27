package com.sisindia.ai.android.utils;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;

import androidx.room.Room;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.IopsDatabase;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import timber.log.Timber;

public final class FileUtils {

    public static final String EXT_JPG = ".jpg";
    private static final String APP_NAME = "IopsV2";
//    public static final String THREE_GPP = ".3gp";
    public static final String THREE_GPP = ".mp3";
    public static final String EXT_PNG = ".png";
    private static final String BASE_DIR = APP_NAME + File.separator;
    public static final String DIR_ROOT = FileUtils.getRootPath() + File.separator + BASE_DIR;

    /**
     * Get SD card root directory, if SD card is not available, get the root directory of internal storage
     */
    public static File getRootPath() {
        File path;
        if (sdCardIsAvailable()) {
            path = Environment.getExternalStorageDirectory(); // SD card root directory / storage / emulated / 0
        } else {
            path = Environment.getDataDirectory(); // The root directory of the internal storage / data
        }
        return path;
    }

    public static String getRootPathV2(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + BASE_DIR;
    }

    public static String getRootPathForDBV2(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + BASE_DIR;
    }

    /**
     * Whether SD card is available
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }

    /**
     * Determine if the directory exists, if not, determine whether the creation was successful
     *
     * @param dirPath file path
     *                { @code to true }: the presence or creation is successful < br > { @code to false }: creation fails or does not exist
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * Determine if the directory exists, if not, determine whether the creation was successful
     *
     * @param file file
     *             { @code to true }: the presence or creation is successful < br > { @code to false }: creation fails or does not exist
     */
    public static boolean createOrExistsDir(File file) {
        // If it exists, it returns true if it is a directory, false if it is a file, and it returns whether it was created successfully.
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /*public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }*/

    public static boolean createOrExistsFile(File file) {
        if (file == null)
            return false;
        if (file.exists())
            return file.isFile();
        if (!createOrExistsDir(file.getParentFile()))
            return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get files based on file path
     *
     * @param filePath file path
     * @return file
     */
    public static File getFileByPath(String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * Determine if the string is null or all whitespace characters
     */
    private static boolean isSpace(final String s) {
        if (s == null)
            return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Close IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null)
            return;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static String createAudioFileForGrievances() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("grievances_audio_").append(s).append(THREE_GPP).toString();
        return imagePath;
    }*/

    public static String createAudioFileForGrievancesV2(Context context) {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String audioPath;
        audioPath = buffer.append(getRootPathV2(context))
                .append(APP_NAME).append("grievances_audio_").append(s).append(THREE_GPP).toString();
        return audioPath;
    }

    /*
    public static Uri getUriFromPath(String path) {
        return Uri.fromFile(new File(path));
    }

    public static String createFileForSleepingGuard() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_sleeping_guard_").append(s).append(EXT_JPG).toString();
        return imagePath;
    }

    public static String createFileForGuardEvaluation() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_guard_evaluation").append(s).append(EXT_JPG).toString();
        return imagePath;
    }

    public static String createFileForGuardSignature() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_guard_signature").append(s).append(EXT_PNG).toString();
        return imagePath;
    }

    public static String createFile() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("unkwon").append(s).append(EXT_JPG).toString();
        return imagePath;
    }

    public static String createFileForPost() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_post_").append(s).append(EXT_JPG).toString();
        return imagePath;
    }

    public static String createFileForPostSPI() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_postSPI_").append(s).append(EXT_JPG).toString();
        return imagePath;
    }

    public static String createFileForAddSecurity() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_add_security_risk_").append(s).append(EXT_JPG).toString();
        return imagePath;
    }

    public static String createFileForAiProfile() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_ai_profile_").append(s).append(EXT_JPG).toString();
        return imagePath;
    }*/

    /*public static String createFileForKitRequest() {
        StringBuilder buffer = new StringBuilder();
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMMyyyy'_'HHmmss", d.getTime());
        String imagePath;
        imagePath = buffer.append(DIR_ROOT).append(APP_NAME).append("_kit_request_").append(s).append(EXT_JPG).toString();
        return imagePath;
    }*/

    /*
     * ---------------------------Created file name with below format-------------------------------------
     *    \{ZoneName}\{RegionName}\{BranchName}\{SiteName\{TaskId}\{AttachmentSourceTypeId}-{pkId}-{guid}
     * ----------------------------------------------------------------------------------------------------
     */
    public static String createFileName(String azureFileNameFormat) {
        return DIR_ROOT + azureFileNameFormat;
    }

    public static String createFileNameV2(String azureFileNameFormat, Context context) {
        return getRootPathV2(context) + azureFileNameFormat;
    }

    public static String createTempFile(String fileName) {
        StringBuilder buffer = new StringBuilder();
        CharSequence s = DateFormat.format("_ddMMMyyyy'_'HHmmss", (new Date()).getTime());
        return buffer.append(DIR_ROOT).append(fileName).append(s).append(EXT_JPG).toString();
    }

    public static String createTempFileV2(String fileName, Context context) {
        StringBuilder buffer = new StringBuilder();
        CharSequence s = DateFormat.format("_ddMMMyyyy'_'HHmmss", (new Date()).getTime());
        return buffer.append(getRootPathV2(context)).append(fileName).append(s).append(EXT_JPG).toString();
    }

    /*public static void copyLocalDBInPhone(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + context.getPackageName() + "/databases/" + context.getPackageName();
                Timber.d("currentDBPath " + currentDBPath);

                String backupDBPath = DIR_ROOT + APP_NAME + "AshuRajputLocalDB" + ".db";

//                String backupDBPath = "UserName" + "_iOPS2.0_" + ".db";

                String pathDirectory = sd.getAbsolutePath() + "/AshuRajputIops2.0";
                File path = new File(pathDirectory);
                if (!path.exists())
                    path.mkdirs();

                File currentDB = new File(currentDBPath);
                File backupDB = new File(path, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }

            Log.e("DBCopy", "Backup has been created!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static File saveDB(Context context) {
        boolean isBackupDone = true;
        IopsDatabase mTestDB = Room.databaseBuilder(context, IopsDatabase.class, BuildConfig.APPLICATION_ID).build();
        mTestDB.close();
        File dbFile = context.getDatabasePath(BuildConfig.APPLICATION_ID);
//        File sdir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "iOPS_DB");
//        String sfpath = sdir.getPath() + File.separator + "DBsave" + System.currentTimeMillis();
//        File dbStorageDir = new File(DIR_ROOT, "database");
        File dbStorageDir = new File(getRootPathForDBV2(context), "database");
        String filePath = dbStorageDir.getPath() + File.separator + Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME) + "_DB";
        if (!dbStorageDir.exists()) {
            dbStorageDir.mkdirs();
        }
        File savedFile = new File(filePath);
        try {
            boolean isFileSaved = savedFile.createNewFile();
            Timber.d("IsDB File save : %s", isFileSaved);
            int bufferSize = 8 * 1024;
            byte[] buffer = new byte[bufferSize];
            int bytes_read;
            OutputStream saveDb = new FileOutputStream(filePath);
            InputStream inDB = new FileInputStream(dbFile);
            while ((bytes_read = inDB.read(buffer, 0, bufferSize)) > 0) {
                saveDb.write(buffer, 0, bytes_read);
            }
            saveDb.flush();
            inDB.close();
            saveDb.close();
        } catch (Exception e) {
            isBackupDone = false;
            e.printStackTrace();
        }

        if (isBackupDone)
            return savedFile;
        else
            return null;
    }
}