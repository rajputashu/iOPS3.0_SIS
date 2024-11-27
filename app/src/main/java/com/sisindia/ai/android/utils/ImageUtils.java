package com.sisindia.ai.android.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.text.TextUtils;
import android.util.Log;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.services.GeoLocationService;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author   wildma
 * Github   https://github.com/wildma
 * Date     2018/6/24
 * Desc $ {Picture related tools class}
 */

public class ImageUtils {

    /**
     * save Picture
     *
     * @param src      source image
     * @param filePath File path to save to
     * @param format
     * @Return { @code to true }: Success < br > { @code to false }: Failed
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format) {
        return save(src, FileUtils.getFileByPath(filePath), format, false);
    }

    public static boolean saveV2(Bitmap src, String filePath, CompressFormat format) {
        return save(src, FileUtils.getFileByPath(filePath), format, false);
    }

    /**
     * save Picture
     *
     * @param src    source image
     * @param file   The file to save to
     * @param format
     * @Return { @code to true }: Success < br > { @code to false }: Failed
     */
    public static boolean save(Bitmap src, File file, CompressFormat format) {
        return save(src, file, format, false);
    }

    /**
     * save Picture
     *
     * @param src      source image
     * @param filePath File path to save to
     * @param format
     * @param recycle  whether to recycle
     * @Return { @code to true }: Success < br > { @code to false }: Failed
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format, boolean recycle) {
        return save(src, FileUtils.getFileByPath(filePath), format, recycle);
    }

    /**
     * save Picture
     *
     * @param src     source image
     * @param file    The file to save to
     * @param format
     * @param recycle whether to recycle
     * @Return { @code to true }: Success < br > { @code to false }: Failed
     */
    public static boolean save(Bitmap src, File file, CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src) || !FileUtils.createOrExistsFile(file)) {
            return false;
        }
//        Log.d("", src.getWidth() + ", " + src.getHeight());
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
//            ret = src.compress(format, 100, os);
            ret = src.compress(format, 80, os);
            if (recycle && !src.isRecycled()) {
                src.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeIO(os);
        }
        return ret;
    }

    /**
     * Determine if the bitmap object is empty
     *
     * @param src source image
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * Convert byte [] to Bitmap
     *
     * @param bytes
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromByte(byte[] bytes, int width, int height) {
        final YuvImage image = new YuvImage(bytes, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length);
        if (!image.compressToJpeg(new Rect(0, 0, width, height), 100, os)) {
            return null;
        }
        byte[] tmp = os.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        return bmp;
    }


    public static Bitmap mark(Bitmap src, AttachmentEntity metaData, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(12f);
        paint.setAntiAlias(true);

        String firstLine = Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE) + " " + String.valueOf(Prefs.getDouble(PrefConstants.LATITUDE)).substring(0, 4) + ", " + String.valueOf(Prefs.getDouble(PrefConstants.LONGITUDE)).substring(0, 4);
        firstLine = TextUtils.concat(firstLine, " ", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"))).toString();

        canvas.drawText(firstLine, 20f, result.getHeight() - 40, paint);
        canvas.drawText(metaData.imageText, 20f, result.getHeight() - 20, paint);
        return result;
    }


}