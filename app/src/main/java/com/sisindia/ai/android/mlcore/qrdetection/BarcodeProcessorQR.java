package com.sisindia.ai.android.mlcore.qrdetection;

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

import android.animation.ValueAnimator;
import android.graphics.RectF;

import androidx.annotation.MainThread;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.sisindia.ai.android.mlcore.QRScannerViewModel;
import com.sisindia.ai.android.mlcore.camera.CameraReticleAnimator;
import com.sisindia.ai.android.mlcore.camera.QRFrameProcessorBase;
import com.sisindia.ai.android.mlcore.camera.QRGraphicOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.sisindia.ai.android.mlcore.qrdetection.WorkflowState.CONFIRMING;
import static com.sisindia.ai.android.mlcore.qrdetection.WorkflowState.DETECTED;
import static com.sisindia.ai.android.mlcore.qrdetection.WorkflowState.DETECTING;
import static com.sisindia.ai.android.mlcore.qrdetection.WorkflowState.SEARCHED;
import static com.sisindia.ai.android.mlcore.qrdetection.WorkflowState.SEARCHING;

/**
 * A processor to run the barcode detector.
 */
public class BarcodeProcessorQR extends QRFrameProcessorBase<List<FirebaseVisionBarcode>> {

    private final FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    private final QRScannerViewModel scannerViewModel;
    private final CameraReticleAnimator cameraReticleAnimator;

    public BarcodeProcessorQR(QRGraphicOverlay graphicOverlay, QRScannerViewModel scannerViewModel) {
        this.scannerViewModel = scannerViewModel;
        this.cameraReticleAnimator = new CameraReticleAnimator(graphicOverlay);
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @MainThread
    @Override
    protected void onSuccess(FirebaseVisionImage image, List<FirebaseVisionBarcode> results, QRGraphicOverlay graphicOverlay) {
        if (scannerViewModel != null) {
            if (!scannerViewModel.isCameraLive())
                return;
        }
//        Timber.d("Barcode result size: " + results.size());

        // Picks the barcode, if exists, that covers the center of graphic overlay.
        FirebaseVisionBarcode barcodeInCenter = null;
        for (FirebaseVisionBarcode barcode : results) {
            RectF box = graphicOverlay.translateRect(Objects.requireNonNull(barcode.getBoundingBox()));
            if (box.contains(graphicOverlay.getWidth() / 2f, graphicOverlay.getHeight() / 2f)) {
                barcodeInCenter = barcode;
                break;
            }
        }

        graphicOverlay.clear();
        if (barcodeInCenter == null) {
            cameraReticleAnimator.start();
            graphicOverlay.add(new BarcodeReticleGraphic(graphicOverlay, cameraReticleAnimator));
            setWorkflowState(DETECTING);
        } else {
            cameraReticleAnimator.cancel();
            float sizeProgress =
                    PreferenceUtils.getProgressToMeetBarcodeSizeRequirement(graphicOverlay, barcodeInCenter);
            if (sizeProgress < 1) {
                // Barcode in the camera view is too small, so prompt user to move camera closer.
                graphicOverlay.add(new BarcodeConfirmingGraphic(graphicOverlay, barcodeInCenter));
                setWorkflowState(CONFIRMING);
            } else {
                // Barcode size in the camera view is sufficient.
                if (PreferenceUtils.shouldDelayLoadingBarcodeResult()) {
                    ValueAnimator loadingAnimator = createLoadingAnimator(graphicOverlay, barcodeInCenter);
                    loadingAnimator.start();
                    graphicOverlay.add(new BarcodeLoadingGraphic(graphicOverlay, loadingAnimator));
                    setWorkflowState(SEARCHING);
                } else {
                    setWorkflowState(DETECTED);
                }
            }
        }
        graphicOverlay.invalidate();
    }

    private ValueAnimator createLoadingAnimator(QRGraphicOverlay graphicOverlay, FirebaseVisionBarcode barcode) {
        float endProgress = 1.1f;
        ValueAnimator loadingAnimator = ValueAnimator.ofFloat(0f, endProgress);
        loadingAnimator.setDuration(2000);
        loadingAnimator.addUpdateListener(
                animation -> {
                    if (Float.compare((float) loadingAnimator.getAnimatedValue(), endProgress) >= 0) {
                        graphicOverlay.clear();
                        setWorkflowState(SEARCHED);
                        setDetectedBarCode(barcode);
                    } else {
                        graphicOverlay.invalidate();
                    }
                });
        return loadingAnimator;
    }

    private void setDetectedBarCode(FirebaseVisionBarcode barcode) {
        if (scannerViewModel != null)
            scannerViewModel.detectedBarCode.setValue(barcode);
    }

    @Override
    protected void onFailure(Exception e) {
        Timber.e("Barcode detection failed!");
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Timber.e("Failed to close barcode detector!");
        }
    }

    private void setWorkflowState(WorkflowState state) {
        if (scannerViewModel != null) {
            scannerViewModel.setWorkflowState(state);
        }
    }
}