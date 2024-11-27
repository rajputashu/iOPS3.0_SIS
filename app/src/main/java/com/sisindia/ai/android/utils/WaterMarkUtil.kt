package com.sisindia.ai.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextUtils
import androidx.annotation.ColorInt
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.entities.AttachmentEntity
import java.io.File

/**
 * Created by Ashu_Rajput on 8/6/2021.
 */
object WaterMarkUtil {

    fun createWaterMark(bitmap: Bitmap, attachmentMO: AttachmentEntity, context: Context,
        employeeNumber: String, capturedDateTime: String): File? {
        var firstLine = employeeNumber + " " +
                Prefs.getDouble(PrefConstants.LATITUDE).toString().substring(0, 4) + ", " +
                Prefs.getDouble(PrefConstants.LONGITUDE).toString().substring(0, 4)
        /*firstLine = TextUtils.concat(firstLine, "\n",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a")),
            "\n", attachmentMO.imageText).toString()*/
        firstLine = TextUtils.concat(firstLine, "\n",
            capturedDateTime, "\n", attachmentMO.imageText).toString()

        val waterMarkBitmap = addWatermark(bitmap, firstLine)
//        val filePath = FileUtils.createTempFile("iOPS2.0_Mark_" + attachmentMO.attachmentSourceType)
        val filePath =
            FileUtils.createTempFileV2("iOPS2.0_Mark_" + attachmentMO.attachmentSourceType, context)
        if (ImageUtils.save(waterMarkBitmap, filePath, Bitmap.CompressFormat.JPEG)) {
            return File(filePath)
        }
        return null
    }

    private fun addWatermark(
        bitmap: Bitmap,
        watermarkText: String,
        options: WatermarkOptions = WatermarkOptions()
    ): Bitmap {
        val result = bitmap.copy(bitmap.config, true)
        val canvas = Canvas(result)
        val paint = Paint(ANTI_ALIAS_FLAG or DITHER_FLAG)
        paint.textAlign = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> Paint.Align.LEFT

            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> Paint.Align.RIGHT
        }
        val textSize = result.width * options.textSizeToWidthRatio
        paint.textSize = textSize
        paint.color = options.textColor
        /*if (options.shadowColor != null) {
            paint.setShadowLayer(textSize / 2, 0f, 0f, options.shadowColor)
        }*/
        if (options.typeface != null) {
            paint.typeface = options.typeface
        }
        val padding = result.width * options.paddingToWidthRatio
        val coordinates = calculateCoordinates(watermarkText, paint, options,
            canvas.width,
            canvas.height,
            padding)

        if (watermarkText.contains("\n")) {
            val splitText = watermarkText.split("\n")

            /*splitText.forEachIndexed { index, wmText ->
//                canvas.drawText(wmText, coordinates.x, coordinates.y - 30, paint)
                Timber.e("Before y : ${coordinates.y}")
                canvas.drawText(wmText, coordinates.x, coordinates.y, paint)
//                coordinates.y += paint.textSize.toInt()
                coordinates.y += paint.textSize.toInt() + ((index + 1) * 15)
                Timber.e("After y : ${coordinates.y}")
            }*/
            canvas.drawText("", coordinates.x, coordinates.y, paint)
            for ((index, wmText) in splitText.withIndex().reversed()) {
//                canvas.drawText(wmText, coordinates.x, coordinates.y - 30, paint)
                canvas.drawText(wmText, coordinates.x, coordinates.y, paint)
//                coordinates.y += paint.textSize.toInt()
//                coordinates.y += paint.textSize.toInt() + (-(index + 1) * 30)
//                coordinates.y += (-(index + 1) * (paint.textSize.toInt() + 10))
                if (index != 0)
                    coordinates.y += -(index + 1) * (paint.textSize.toInt() * (0.75)).toInt() + 15
                else
                    coordinates.y = coordinates.y - 15
            }

//            paint.setShadowLayer(8f, 0f, 0f, options.shadowColor!!)
//            canvas.drawRect(0f, coordinates.y-200, coordinates.x, coordinates.y, paint)

        } else
            canvas.drawText(watermarkText, coordinates.x, coordinates.y - 30, paint)

        return result
    }

    private fun calculateCoordinates(
        watermarkText: String,
        paint: Paint,
        options: WatermarkOptions,
        width: Int,
        height: Int,
        padding: Float
    ): PointF {
        val x = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> {
                padding
            }

            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> {
                width - padding
            }
        }
        val y = when (options.corner) {
            Corner.BOTTOM_LEFT,
            Corner.BOTTOM_RIGHT -> {
                height - padding
            }

            Corner.TOP_LEFT,
            Corner.TOP_RIGHT -> {
                val bounds = Rect()
                paint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)
                val textHeight = bounds.height()
                textHeight + padding
            }
        }
        return PointF(x, y)
    }

    enum class Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
    }

    data class WatermarkOptions(
//        val corner: Corner = Corner.BOTTOM_RIGHT,
        val corner: Corner = Corner.BOTTOM_LEFT,
        val textSizeToWidthRatio: Float = 0.04f,
        val paddingToWidthRatio: Float = 0.03f,
        @ColorInt val textColor: Int = Color.WHITE,
        @ColorInt val shadowColor: Int? = Color.BLACK,
        val typeface: Typeface? = null)
}