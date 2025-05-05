package com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.LookUpType;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.tflite.BoundingBox;
import com.sisindia.ai.android.tflite.Constants;
import com.sisindia.ai.android.tflite.Detector;
import com.sisindia.ai.android.tflite.SpecialClassifier;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;
import com.sisindia.ai.android.utils.GsonUtils;

import org.threeten.bp.LocalDateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GuardPhotoEvaluationResultViewModel extends IopsBaseViewModel {
    public final ObservableField<CheckedGuardEntity> checkedGuard = new ObservableField<>();

    public GuardTurnOutViewRecyclerAdapter recyclerAdapter = new GuardTurnOutViewRecyclerAdapter();

    @Inject
    public LookUpDao lookUpDao;

    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public DayCheckDao dayCheckDao;

    public AttachmentEntity attachment = new AttachmentEntity();
    private boolean isFakeGuardPic = false;

    private Detector detector;
    private SpecialClassifier classifier;
    private final Map<String, SpecialClassifier.ClassificationResult> classificationResults = new HashMap<>();
//    private final List<String> turnOutDisplayList = new ArrayList<>();

    Bitmap selectedImageBitmap;
    private final Set<String> finalClassifierList = new HashSet<>();
    List<GuardTurnOutResult.GuardTurnoutModel> aiTurnOutMOList = new ArrayList<>();
    boolean isComingAfterEditingTurnOut = false;
    private int entityRowId = 0;
    private List<BoundingBox> detectedBoundingBox = new ArrayList<>();

    @Inject
    public GuardPhotoEvaluationResultViewModel(@NonNull Application application) {
        super(application);
    }

    public void initTurnOutAIDetector() {

        isLoading.set(View.VISIBLE);

        classifier = new SpecialClassifier(getApplication(), Constants.CLASSIFIER_MODEL_PATH,
                classifierListener);
        classifier.setup();

        detector = new Detector(getApplication(), Constants.MODEL_PATH,
                Constants.LABELS_PATH, detectorListener);
        detector.setup(true);

        if (attachment != null) {

            fetchGuardTurnOutFromDB();

            Uri uri = Uri.parse(attachment.localFilePath);
            try {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
                selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                if (selectedImageBitmap != null) {
                    processImage(selectedImageBitmap);
                } else {
//                    fetchGuardTurnOutFromDB();
                    Timber.e("Attachment image is null");
                }
            } catch (IOException e) {
//                fetchGuardTurnOutFromDB();
                Timber.e(e, "Failed to open file stream");
            }
        }
    }

    /*public void initObjectDetectorDetector() {

        isLoading.set(View.VISIBLE);

        ObjectDetectionModel model = new ObjectDetectionModel(getApplication());
        model.initializeModel();

        if (attachment != null) {

            fetchGuardTurnOutFromDB();

            Uri uri = Uri.parse(attachment.localFilePath);
            try {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
                selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                if (selectedImageBitmap != null) {
                    List<ObjectDetectionModel.DetectionResult> detections = model.detectObjects(selectedImageBitmap);
                    for (ObjectDetectionModel.DetectionResult result : detections) {

                        Timber.e("Obj DetectionResult %s", result.getLabel());
                    }

//                    val resultBitmap = model.drawBoundingBoxes(inputBitmap, detections);
                } else {
//                    fetchGuardTurnOutFromDB();
                    Timber.e("Attachment image is null");
                }
            } catch (IOException e) {
//                fetchGuardTurnOutFromDB();
                Timber.e(e, "Failed to open file stream");
            }
        }
    }*/

    Detector.DetectorListener detectorListener = new Detector.DetectorListener() {

        @Override
        public void onDetect(@NonNull List<BoundingBox> boundingBoxesList, long inferenceTime) {
            isLoading.set(View.GONE);
            detectedBoundingBox = boundingBoxesList;
//            fetchCheckedGuardForMLData(boundingBoxesList);
            runClassificationOnBoundingBox(boundingBoxesList);
        }

        @Override
        public void onEmptyDetect() {
            Timber.e("detectionText onEmptyDetect()");
//            fetchGuardTurnOutFromDB();
            isLoading.set(View.GONE);
        }
    };

    SpecialClassifier.ClassifierListener classifierListener = result -> {
        classificationResults.put(result.getOriginalClass(), result);
        updateClassificationUI();
    };

    Map<String, Boolean> uniqueClassifierClasses = new HashMap<>();
    private int mlTurnOutScore = 0;

    @SuppressLint("NotifyDataSetChanged")
    private void updateClassificationUI() {
        Timber.e("Classification Available results: %s", classificationResults.keySet());

        for (Map.Entry<String, SpecialClassifier.ClassificationResult> entry : classificationResults.entrySet()) {
            String originalClass = entry.getKey();
            SpecialClassifier.ClassificationResult result = entry.getValue();
            String classificationText = result.getClassifiedAs();

            Timber.e("Original Class: %s and Classified As: %s", originalClass, classificationText);

            boolean matchFound = false;
            int matchedPosition = -1;
            boolean isProperTurnOut = false;
            /*for (String item : turnOutDisplayList) {
                if (item.contains(originalClass)) {
                    matchFound = true;
                    matchedPosition = turnOutDisplayList.indexOf(item);
                    if (!classificationText.contains("POOR") || !classificationText.contains("NON"))
                        isProperTurnOut = true;
                    break;
                }
            }*/

            //Hard code logic to test the ML functionality
            switch (originalClass) {
                case "SHOE":
                    matchFound = true;
                    matchedPosition = 6;
                    if (!classificationText.contains("POOR") || !classificationText.contains("NON"))
                        isProperTurnOut = true;
                    break;
                case "PANT":
                    matchFound = true;
                    matchedPosition = 14;
                    if (!classificationText.contains("NON-SIS-PANT"))
                        isProperTurnOut = true;
                    break;
                case "SHIRT":
                    matchFound = true;
                    matchedPosition = 15;
                    if (!classificationText.contains("NON-SIS-SHIRT"))
                        isProperTurnOut = true;
                    break;
                case "HAIR":
                    matchFound = true;
                    matchedPosition = 21;
                    if (!classificationText.contains("IMPROPER"))
                        isProperTurnOut = true;
                    break;
                case "FACE":
                    matchFound = true;
                    matchedPosition = 22;
                    if (classificationText.contains("SHAVED"))
                        isProperTurnOut = true;
                    break;
            }

            if (!uniqueClassifierClasses.containsKey(originalClass) && matchFound) {
//                if (matchedPosition != -1 && isProperTurnOut) {
                if (isProperTurnOut) {
                    recyclerAdapter.getItem(matchedPosition).isSelected = true;
                    uniqueClassifierClasses.put(originalClass, true);
                    mlTurnOutScore += 1;
                } else {
                    Timber.e("Either result not matched or improper/Non/Poor");
                }

            } else {
                uniqueClassifierClasses.put(originalClass, true);
            }
        }

        if (uniqueClassifierClasses.size() == finalClassifierList.size()) {
            Timber.e("COMING AFTER PROCESSING ALL CLASSIFIER AND UPDATING UI");

            //Commenting below variable and respective logic as Uniform and Badge detection not required
            /*int uniformDetectedScore = 0;
            int badgeDetectedScore = 0;
            List<String> completeUniformSet = List.of("SHIRT", "PANT", "SHOE", "JACKET");
            List<String> badgeSet = List.of("CAP BADGE", "SHIRT_BADGE", "JACKET_BADGE", "SWEATER_BADGE");*/

            //Further checking detection from DETECTED OBJECTS FROM final list aiTurnOutMOList
            if (!detectedBoundingBox.isEmpty()) {
                for (BoundingBox detectedBox : detectedBoundingBox) {
                    String detectedLabel = detectedBox.getClsName().trim();
                    if (detectedLabel.equalsIgnoreCase("BELT BUCKLE STEEL")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(0).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("CAP")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(1).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("CAP BADGE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(2).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("CARD HOLDER")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(3).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("LANYARD")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(4).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("BELT")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(5).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("SOCKS")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(7).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("PRINTED CARD")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(8).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("NAME PLATE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(9).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("SHOULDER BADGE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(10).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("TIE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(11).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("JACKET")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(12).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("SWEATER")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(13).isSelected = true;
                    } /*else if (detectedLabel.equalsIgnoreCase("PANT")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(14).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("SHIRT")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(15).isSelected = true;
                    }*/ else if (detectedLabel.equalsIgnoreCase("WHISTLE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(16).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("SHIRT_BADGE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(17).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("JACKET_BADGE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(18).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("SWEATER_BADGE")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(19).isSelected = true;
                    } else if (detectedLabel.equalsIgnoreCase("HELMET")) {
                        mlTurnOutScore += 1;
                        recyclerAdapter.getItem(20).isSelected = true;
                    }

                    /*Timber.e("Uniform Bounding Box Label %s", detectedLabel);
                    if (completeUniformSet.contains(detectedLabel)) {
                        Timber.e("Uniform : Detected Label %s", detectedLabel);
                        uniformDetectedScore = uniformDetectedScore + 1;
                    }

                    if (badgeSet.contains(detectedLabel)) {
                        Timber.e("Badge : Detected Label %s", detectedLabel);
                        badgeDetectedScore = badgeDetectedScore + 1;
                    }*/
                }
            }
//            Timber.e("Uniform and Badge DetectedScore %d, %d ", uniformDetectedScore, badgeDetectedScore);

            //Checking below condition for Complete Uniform wrt Uniform score
            /*if (uniformDetectedScore > 2) {
                mlTurnOutScore += 1;
                recyclerAdapter.getItem(2).isSelected = true; // Uniform
            }*/

            //Checking below condition for Badge wrt Badge score
            /*if (badgeDetectedScore > 0) {
                mlTurnOutScore += 1;
                recyclerAdapter.getItem(5).isSelected = true; // BADGE
            }*/

            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> {
                if (checkedGuard.get() != null) {
                    CheckedGuardEntity item = checkedGuard.get();
                    if (item != null) {
                        synchronized (checkedGuard) {
                            item.turnOutScore = mlTurnOutScore;
                            Timber.e("COMING AFTER - turnOutScore- %d TotalTurnOut %d", item.turnOutScore, item.totalTurnOut);
                            CheckedGuardEntity updatedItem = new CheckedGuardEntity();
                            updatedItem.turnOutScore = item.turnOutScore;
                            updatedItem.totalTurnOut = item.totalTurnOut;
                            checkedGuard.set(updatedItem);
                        }
                    }
                }
                recyclerAdapter.notifyDataSetChanged();
            });
        }
    }

    private void processImage(Bitmap bitmap) {
        if (detector == null) {
            isLoading.set(View.GONE);
            showToast("Detector not ready, please wait");
            return;
        }

        new Thread(() -> {
            try {
                detector.detect(bitmap);
            } catch (Exception e) {
                Timber.e("Error processing image: %s", e.getMessage());
            }
        }).start();
    }

    private void runClassificationOnBoundingBox(List<BoundingBox> boundingBoxesList) {

        if (selectedImageBitmap != null) {
            List<String> classesToProcess = List.of("SHIRT", "PANT", "JACKET", "SHOE", "FACE", "HAIR");

            getUniqueListForClassification(boundingBoxesList, classesToProcess);

            // Create a set to track unique detected classes
            Map<String, Boolean> uniqueDetectedClasses = new HashMap<>();

            // Process each detection
            for (BoundingBox box : boundingBoxesList) {
                String detectedClass = box.getClsName().trim().toUpperCase();
                Timber.d("From Model detected class: %s", detectedClass);

                // Check if the detected class has not been processed before
                if (!uniqueDetectedClasses.containsKey(detectedClass) && classesToProcess.contains(detectedClass)) {
                    try {
                        Timber.d("Classification starting classification for detected class: %s", detectedClass);

                        Bitmap croppedBitmap = cropDetectedObject(selectedImageBitmap, box);

                        // Resize to 224x224 as per your training size
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedBitmap, 224, 224, true);

                        // Run classification - pass the original class name to maintain consistency
                        classifier.classify(resizedBitmap, detectedClass);

                        // Add the detected class to the unique set
                        uniqueDetectedClasses.put(detectedClass, true);
                    } catch (Exception e) {
                        Timber.e("Classification Error processing " + detectedClass + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Timber.d("Classification Skipping classification for: " + box.getClsName() + " (already processed or not in classesToProcess set).");
                }
            }
        }
    }

    private void getUniqueListForClassification(List<BoundingBox> boundingBoxesList, List<String> classesToProcess) {
        finalClassifierList.addAll(classesToProcess);
        Set<String> setDetected = new HashSet<>();
        for (BoundingBox box : boundingBoxesList) {
            setDetected.add(box.getClsName().trim().toUpperCase());
        }
        finalClassifierList.retainAll(setDetected);

        Set<String> labelList = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getApplication().getAssets().open(Constants.LABELS_PATH)));
            String line;
            while ((line = reader.readLine()) != null) {
                labelList.add(line.trim());
            }
            reader.close();
        } catch (Exception e) {
            Timber.e("LabelChecker Error reading labels.txt from assets");
        }

        if (!labelList.isEmpty()) {
            for (String label : labelList) {
                if (!setDetected.isEmpty() && setDetected.contains(label)) {
                    aiTurnOutMOList.add(new GuardTurnOutResult.GuardTurnoutModel(label, true));
                } else {
                    aiTurnOutMOList.add(new GuardTurnOutResult.GuardTurnoutModel(label, false));
                }
            }
        }
    }

    private Bitmap cropDetectedObject(Bitmap bitmap, BoundingBox box) {
        int x1 = (int) (box.getX1() * bitmap.getWidth());
        int y1 = (int) (box.getY1() * bitmap.getHeight());
        int x2 = (int) (box.getX2() * bitmap.getWidth());
        int y2 = (int) (box.getY2() * bitmap.getHeight());

        int width = x2 - x1;
        int height = y2 - y1;

        return Bitmap.createBitmap(bitmap, x1, y1, Math.max(width, 1), Math.max(height, 1));
    }

    /*private void fetchCheckedGuardForMLData(List<BoundingBox> boundingBoxes) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);

        addDisposable(dayCheckDao.fetchCheckedGuard(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> turnOutViaMLModel(entity, boundingBoxes), Timber::e));
    }*/

    /*private void turnOutViaMLModel(CheckedGuardEntity item, List<BoundingBox> boundingBoxesList) {
        addDisposable(lookUpDao.fetchGuardTurnOut(LookUpType.GUARD_TURNOUT.getTypeId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (!list.isEmpty()) {
                        item.totalTurnOut = list.size();
                        checkedGuard.set(item);

                        for (int k = 0; k < list.size(); k++) {

                            for (int i = 0; i < boundingBoxesList.size(); i++) {
                                BoundingBox box = boundingBoxesList.get(i);

//                                String detectionText = "Detection " + (i + 1) + ": " + box.getClsName();
//                            String confidenceText = "Confidence: " + String.format("%.2f", box.getCnf() * 100) + "%";

                                Timber.e("ML LabelName%s : LookUpName : %s",
                                        box.getClsName().toLowerCase(),list.get(k).displayName.toLowerCase());
//                            Timber.e("%s", confidenceText);
                                if (list.get(k).displayName.toLowerCase().contains(box.getClsName().toLowerCase())) {
                                    Timber.e("Is it matching??");
                                    list.get(k).isSelected = true;
                                }
                            }
                        }
                        recyclerAdapter.clearAndSetItems(list);

//                        runClassificationOnBoundingBox();

                    }
                }, Timber::e));
    }*/

    public void fetchGuardTurnOutFromDB() {

        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);
        /*Timber.e("fetchGuardTurnOutFromDB taskId :%d",taskId);
        Timber.e("fetchGuardTurnOutFromDB postId :%d",postId);
        Timber.e("fetchGuardTurnOutFromDB siteId :%d",siteId);
        Timber.e("fetchGuardTurnOutFromDB employeeId :%d",employeeId);*/

        addDisposable(dayCheckDao.fetchCheckedGuard(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGuardFetched, Timber::e));
    }

    private void onGuardFetched(CheckedGuardEntity item) {
        entityRowId = item.id;
        if (!TextUtils.isEmpty(item.guardEvaluationResult)) {
            GuardTurnOutResult result = new Gson().fromJson(item.guardEvaluationResult, GuardTurnOutResult.class);
            recyclerAdapter.clearAndSetItems(result.turnOutResult);
            checkedGuard.set(item);
        } else {
            addDisposable(lookUpDao.fetchGuardTurnOut(LookUpType.GUARD_TURNOUT.getTypeId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        if (!list.isEmpty()) {
                            item.totalTurnOut = list.size();
                            checkedGuard.set(item);
                            /*for (GuardTurnOutResult.GuardTurnoutModel model : list) {
                                turnOutDisplayList.add(model.displayName.toUpperCase());
                            }*/
                            recyclerAdapter.clearAndSetItems(list);
                        }
                    }, Timber::e));
        }
    }

    public void onContinuePhotoEvaluationBtnClick(View view) {
        if (isComingAfterEditingTurnOut) {
            validateAndUpdateAfterEditingTurnOut();
        } else {
            updateMLDetectedTurnOutToDB();
        }
    }

    public void onGuardTurnOutEditClick(View view) {
        GuardTurnOutResult turnOutResult = new GuardTurnOutResult();
        turnOutResult.turnOutResult = recyclerAdapter.getItems();
        turnOutResult.mlTurnOutResult = aiTurnOutMOList;
        message.what = NavigationConstants.ON_GUARD_TURN_OUT_EDIT;
//        message.obj = recyclerAdapter.getItems();
        message.obj = turnOutResult;
        liveData.postValue(message);
    }

    private void validateAndUpdateAfterEditingTurnOut() {
        if (!TextUtils.isEmpty(attachment.attachmentGuid) && !TextUtils.isEmpty(attachment.localFilePath)) {
            int turnOut = 0;
            for (GuardTurnOutResult.GuardTurnoutModel model : recyclerAdapter.getItems()) {
                if (model.isSelected) {
                    turnOut += 1;
                }
            }
            if (turnOut != 0) {
                isFakeGuardPic = attachment.isFakeGuardImage;
                addDisposable(attachmentDao.insert(attachment)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onAttachmentDone, Timber::e));
            } else {
                showToast("Please select at least one turnOut to continue.");
            }
        } else {
            showToast("Error in processing guard image at local, please re-take");
        }
    }

    private void updateMLDetectedTurnOutToDB() {

        if (!TextUtils.isEmpty(attachment.attachmentGuid) && !TextUtils.isEmpty(attachment.localFilePath)) {

            //Creating model for manual/editing guardTurnOutResult
            GuardTurnOutResult result = new GuardTurnOutResult();
            result.turnOutResult = recyclerAdapter.getItems();

            //Creating model for ML detection guardTurnOutResult
            GuardTurnOutResult mlResult = new GuardTurnOutResult();
            mlResult.mlTurnOutResult = aiTurnOutMOList;

            int turnOut = 0;
            for (GuardTurnOutResult.GuardTurnoutModel model : result.turnOutResult) {
                if (model.isSelected) {
                    turnOut += 1;
                }
            }

            if (turnOut != 0) {
                CheckedGuardEntity item = checkedGuard.get();
                if (item == null) {
                    showToast("Unable to save guard");
                    return;
                }
                item.turnOutScore = turnOut;
                item.guardEvaluationResult = GsonUtils.toJsonWithoutExopse().toJson(result);
                item.mlGuardEvaluationResult = GsonUtils.toJsonWithoutExopse().toJson(mlResult);
                item.currentState = CheckedGuardEntity.CurrentState.EVALUATION.getGuardStatus();
                item.updatedDateTime = LocalDateTime.now().toString();

                //Adding these 2 lines
//                item.isFakeGuardImage = isFakeGuardPic;
//                item.guardEvaluationGuid = attachment.attachmentGuid;

//                Timber.e("ITEM ID PRINT %d", entityRowId);

                addDisposable(dayCheckDao.updateCheckedGuardV2(turnOut, item.totalTurnOut,
                                item.guardEvaluationResult,
                                item.mlGuardEvaluationResult,
                                item.updatedDateTime, entityRowId, item.currentState,
                                attachment.attachmentGuid,
                                attachment.isFakeGuardImage)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(rowId -> {
                            isFakeGuardPic = attachment.isFakeGuardImage;
                            addDisposable(attachmentDao.insert(attachment)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::onAttachmentDone, Timber::e));
                        }, Timber::e));
            } else {
                showToast("Please select at least one turnOut to continue.");
            }
        } else {
            showToast("Error in processing guard image at local, please re-take");
        }
    }

    private void onAttachmentDone(Long guardEvaluationAttachmentId) {

        /*Timber.e(" guardEvaluationAttachmentId -->>> %d",guardEvaluationAttachmentId);
        Timber.e(" guardEvaluationAttachmentId -->>> %s",attachment.attachmentGuid);

        CheckedGuardEntity guard = checkedGuard.get();
        if (guard == null) {
            showToast("Unable to save guard..");
            return;
        }

        guard.guardEvaluationGuid = attachment.attachmentGuid;
        guard.updatedDateTime = LocalDateTime.now().toString();
        guard.isFakeGuardImage = isFakeGuardPic;

        addDisposable(dayCheckDao.updateCheckedGuard(guard)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGuardUpdated, Timber::e));*/


        message.what = NavigationConstants.ON_PHOTO_EVALUATION_DONE;
        liveData.postValue(message);
    }

    private void onGuardUpdated(Integer row) {
        message.what = NavigationConstants.ON_PHOTO_EVALUATION_DONE;
        liveData.postValue(message);
    }
}
