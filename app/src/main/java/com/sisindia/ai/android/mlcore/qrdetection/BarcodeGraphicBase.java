/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sisindia.ai.android.mlcore.qrdetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import androidx.core.content.ContextCompat;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.mlcore.camera.QRGraphicOverlay;


abstract class BarcodeGraphicBase extends QRGraphicOverlay.Graphic {

    final int boxCornerRadius;
    final Paint pathPaint;
    final RectF boxRect;
    private final Paint boxPaint;
    private final Paint scrimPaint;
    private final Paint eraserPaint;

    BarcodeGraphicBase(QRGraphicOverlay overlay) {
        super(overlay);

        boxPaint = new Paint();
        boxPaint.setColor(ContextCompat.getColor(context, R.color.barcode_reticle_stroke));
        boxPaint.setStyle(Style.STROKE);
        boxPaint.setStrokeWidth(
                context.getResources().getDimensionPixelOffset(R.dimen._4sdp));

        scrimPaint = new Paint();
        scrimPaint.setColor(ContextCompat.getColor(context, R.color.barcode_reticle_background));
        eraserPaint = new Paint();
        eraserPaint.setStrokeWidth(boxPaint.getStrokeWidth());
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        boxCornerRadius =
                context.getResources().getDimensionPixelOffset(R.dimen._8sdp);

        pathPaint = new Paint();
        pathPaint.setColor(Color.WHITE);
        pathPaint.setStyle(Style.STROKE);
        pathPaint.setStrokeWidth(boxPaint.getStrokeWidth());
        pathPaint.setPathEffect(new CornerPathEffect(boxCornerRadius));

        boxRect = PreferenceUtils.getBarcodeReticleBox(overlay);
    }

    @Override
    protected void draw(Canvas canvas) {
        // Draws the dark background scrim and leaves the box area clear.
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), scrimPaint);
        // As the stroke is always centered, so erase twice with FILL and STROKE respectively to clear
        // all area that the box rect would occupy.
        eraserPaint.setStyle(Style.FILL);
        canvas.drawRoundRect(boxRect, boxCornerRadius, boxCornerRadius, eraserPaint);
        eraserPaint.setStyle(Style.STROKE);
        canvas.drawRoundRect(boxRect, boxCornerRadius, boxCornerRadius, eraserPaint);

        // Draws the box.
        canvas.drawRoundRect(boxRect, boxCornerRadius, boxCornerRadius, boxPaint);
    }
}
