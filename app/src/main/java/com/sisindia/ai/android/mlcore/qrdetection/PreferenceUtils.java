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

import android.graphics.RectF;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.droidcommons.preference.Prefs;
import com.google.android.gms.common.images.Size;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.sisindia.ai.android.mlcore.camera.CameraSizePair;
import com.sisindia.ai.android.mlcore.camera.QRGraphicOverlay;

import static com.sisindia.ai.android.mlcore.QRConstants.PREF_KEY_BARCODE_RETICLE_HEIGHT;
import static com.sisindia.ai.android.mlcore.QRConstants.PREF_KEY_BARCODE_RETICLE_WIDTH;
import static com.sisindia.ai.android.mlcore.QRConstants.PREF_KEY_DELAY_LOADING_BARCODE_RESULT;
import static com.sisindia.ai.android.mlcore.QRConstants.PREF_KEY_ENABLE_BARCODE_SIZE_CHECK;
import static com.sisindia.ai.android.mlcore.QRConstants.PREF_KEY_MINIMUM_BARCODE_WIDTH;
import static com.sisindia.ai.android.mlcore.QRConstants.PREF_KEY_REAR_CAMERA_PICTURE_SIZE;
import static com.sisindia.ai.android.mlcore.QRConstants.PREF_KEY_REAR_CAMERA_PREVIEW_SIZE;


/**
 * Utility class to retrieve shared preferences.
 */
public class PreferenceUtils {


    //DONE
    public static float getProgressToMeetBarcodeSizeRequirement(QRGraphicOverlay overlay, FirebaseVisionBarcode barcode) {

        if (Prefs.getBoolean(PREF_KEY_ENABLE_BARCODE_SIZE_CHECK, false)) {
            float reticleBoxWidth = getBarcodeReticleBox(overlay).width();
            float barcodeWidth = overlay.translateX(barcode.getBoundingBox().width());
            float requiredWidth = reticleBoxWidth * Prefs.getInt(PREF_KEY_MINIMUM_BARCODE_WIDTH, 50) / 100;
            return Math.min(barcodeWidth / requiredWidth, 1);
        } else {
            return 1;
        }
    }

    //done
    public static RectF getBarcodeReticleBox(QRGraphicOverlay overlay) {
        float overlayWidth = overlay.getWidth();
        float overlayHeight = overlay.getHeight();
        float boxWidth = overlayWidth * Prefs.getInt(PREF_KEY_BARCODE_RETICLE_WIDTH, 80) / 100;
        float boxHeight =
                overlayHeight * Prefs.getInt(PREF_KEY_BARCODE_RETICLE_HEIGHT, 35) / 100;
        float cx = overlayWidth / 2;
        float cy = overlayHeight / 2;
        return new RectF(cx - boxWidth / 2, cy - boxHeight / 2, cx + boxWidth / 2, cy + boxHeight / 2);
    }

    //done
    public static boolean shouldDelayLoadingBarcodeResult() {
        return Prefs.getBoolean(PREF_KEY_DELAY_LOADING_BARCODE_RESULT, true);
    }


    //done
    @Nullable
    public static CameraSizePair getUserSpecifiedPreviewSize() {

        String previewSize = Prefs.getString(PREF_KEY_REAR_CAMERA_PREVIEW_SIZE, null);
        String pictureSize = Prefs.getString(PREF_KEY_REAR_CAMERA_PICTURE_SIZE, null);

        if (TextUtils.isEmpty(previewSize) || TextUtils.isEmpty(pictureSize)) {
            return null;
        }

        return new CameraSizePair(
                Size.parseSize(previewSize),
                Size.parseSize(pictureSize));
    }

}
