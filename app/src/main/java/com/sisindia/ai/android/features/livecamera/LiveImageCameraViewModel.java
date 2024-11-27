package com.sisindia.ai.android.features.livecamera;

import android.app.Application;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.commons.CaptureImageType;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.AttachmentMetadataDefinitionDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LiveImageCameraViewModel extends IopsBaseViewModel {

    public ObservableField<Uri> imageUri = new ObservableField<>();
    public ObservableField<String> generatedFileName = new ObservableField<>();
    public ObservableField<Integer> compressedFileSize = new ObservableField<>();
    public ObservableField<Integer> attachmentSourceId = new ObservableField<>(0);
    private ObservableField<String> generatedUUID = new ObservableField<>("");
    private String generatedStoragePath = "";
    public ObservableField<String> updatedAttachmentMetaData = new ObservableField<>("");
    private String barrackId = "";
    private String serverIdAsTaskId = "";
    private String riskId = "";
    private ObservableField<CaptureImageType> obsCapturedImageType = new ObservableField<>(CaptureImageType.UNKNOWN);
    public ObservableField<AttachmentEntity> obsAttachmentEntity = new ObservableField<>(new AttachmentEntity());

    @Inject
    AttachmentMetadataDefinitionDao metadataDefinitionDao;

    @Inject
    TaskDao taskDao;

    @Inject
    AttachmentDao attachmentDao;

    @Inject
    public LiveImageCameraViewModel(@NonNull Application application) {
        super(application);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri.set(imageUri);
    }

    public void generateFileNaming(CaptureImageType imageType) {
        generatedUUID.set(UUID.randomUUID().toString());
        obsCapturedImageType.set(imageType);
        switch (imageType) {
            case POST_SPI_PHOTO:
            case POST_PHOTO:
                generateFileNameAndPathForPostAndSpiImages();
                break;
            case AI_PROFILE:
                generateFileNameAndPathForAIProfile();
                break;
            case GUARD_FULL_PHOTO:
//                generateFileNameAndPathForGuard();
//                break;
            case BILL_COLLECTION:
            case GUARD_SIGNATURE:
            case INDEPENDENT_GRIEVANCE:
            case INDEPENDENT_COMPLAINT:
            case DOCUMENT_SCAN:
            case DUTY_REGISTER:
            case VISITOR_REGISTER:
            case MATERIAL_REGISTER:
            case CLIENT_REGISTER:
            case SITE_REGISTER:
            case VEHICLE_REGISTER:
            case ADD_SECURITY:
            case BARRACK_PROFILE:
            case IMPROVEMENT_PLAN:
            case SLEEPING_GUARD:
            case DEPENDENT_GRIEVANCE:
            case DEPENDENT_COMPLAINT:
            case KIT_REQUEST_PHOTO:
            case KIT_REQUEST_SIGNATURE:
            case SITE_CHECK:
            case REGISTER_DOCUMENT:
            case CLOSE_POA:
            case UNKNOWN:
                //                generateFileNameAndPathForClosePOA();
                tempFileNameGeneratorReplaceWithActualGenerator();
                break;
            case OTHER_TASK:
            case BILL_SUBMIT:
                generateFileNameAndPathForTasks();
                break;
            case BARRACK_BED:
            case BARRACK_MESS:
            case BARRACK_KIT:
            case LANDLORD:
            case CUSTODIAN:
            case BARRACK_OUTSIDE:
                generateFileNameAndPathForBarrackTask();
                break;
            case EDIT_SITE_POST:
            case EDIT_SITE_POST_SPI:
                generateFileNameAndPathForEditSitePostAndSpiPic();
                break;
        }
    }

    private void onFileNameGeneratedSuccessfully() {
        message.what = NavigationConstants.ON_FILE_NAME_GENERATED;
        liveData.postValue(message);
    }

    public void getAndCreateEmployeeProfileMetaData() {
        if (isTempFileRequest) {
            onTempAttachmentMetadataInsertedSuccessfully();
        } else {
            addDisposable(metadataDefinitionDao.fetchMetaDataJsonFormat(Objects.requireNonNull(attachmentSourceId.get()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateMetaDataJsonFormatValues,
                            e -> updateMetaDataJsonFormatValues("{}")));
        }
    }

    private void updateMetaDataJsonFormatValues(String metadataJson) {
        try {
            JSONObject jsonObjectMO = new JSONObject(metadataJson);
            jsonObjectMO.put("uuid", generatedUUID.get());
            jsonObjectMO.put("fileName", generatedFileName.get());
            jsonObjectMO.put("fileSize", Objects.requireNonNull(compressedFileSize.get()) + "KB");
            jsonObjectMO.put("storagePath", generatedStoragePath);

            switch (Objects.requireNonNull(obsCapturedImageType.get())) {
                case AI_PROFILE:
                    jsonObjectMO.put("employeeId", String.valueOf(Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID)));
                    break;
                case BILL_SUBMIT:
                case OTHER_TASK:
                    jsonObjectMO.put("siteId", Prefs.getInt(PrefConstants.CURRENT_SITE));
                    jsonObjectMO.put("taskId", serverIdAsTaskId);
                    jsonObjectMO.put("sequenceNo", "1");
                    break;
                case POST_PHOTO:
                case POST_SPI_PHOTO:
                    jsonObjectMO.put("siteId", Prefs.getInt(PrefConstants.CURRENT_SITE));
                    jsonObjectMO.put("sequenceNo", "1");
                    break;
                case BARRACK_BED:
                case BARRACK_KIT:
                case BARRACK_MESS:
                case BARRACK_OUTSIDE:
                case CUSTODIAN:
                    jsonObjectMO.put("taskId", serverIdAsTaskId);
                    jsonObjectMO.put("sequenceNo", "1");
                    jsonObjectMO.put("barrackId", barrackId);
                    break;
                case EDIT_SITE_POST:
                case EDIT_SITE_POST_SPI:
                    jsonObjectMO.put("siteId", Prefs.getInt(PrefConstants.CURRENT_SITE));
                    jsonObjectMO.put("postId", Prefs.getInt(PrefConstants.CURRENT_POST_ID_FOR_EDIT));
                    jsonObjectMO.put("sequenceNo", "1");
                    break;
                case CLOSE_POA:
                    jsonObjectMO.put("siteId", Prefs.getInt(PrefConstants.CURRENT_SITE));
                    jsonObjectMO.put("riskId", riskId);
                    jsonObjectMO.put("poaId", Prefs.getInt(PrefConstants.POA_ID_FOR_ATTACHMENT));
                    break;
                case GUARD_FULL_PHOTO:
                    jsonObjectMO.put("siteId", Prefs.getInt(PrefConstants.CURRENT_SITE));
                    jsonObjectMO.put("postId", ""); //ToDo: Add postID
                    jsonObjectMO.put("taskId", serverIdAsTaskId); //ToDo: Add postID
                    break;
            }
            updatedAttachmentMetaData.set(jsonObjectMO.toString());
            onAttachmentMetadataInsertedSuccessfully();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onAttachmentMetadataInsertedSuccessfully() {
        message.what = NavigationConstants.ON_ATTACHMENT_METADATA_INSERTED;
        liveData.postValue(message);
    }

    //-------------------FOR SITE POST and SPI-----------------------//
    private void generateFileNameAndPathForPostAndSpiImages() {
        addDisposable(taskDao.getFilePathForAddPostAndSpi(Prefs.getInt(PrefConstants.CURRENT_SITE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    //{attachmentSourceTypeId_siteId_uuid.jpg}
                    StringBuilder builder = new StringBuilder();
                    builder.append(attachmentSourceId.get()).append("_")
                            .append(model.getSiteId()).append("_")
                            .append(generatedUUID.get()).append(FileUtils.EXT_JPG);
                    generatedFileName.set(builder.toString());
                    //{zone}/{region}/{branch}/{site}/post/{FileName.jpg}
                    builder = new StringBuilder();
                    generatedStoragePath = builder.append(model.getZoneCode()).append("/")
                            .append(model.getRegionCode()).append("/")
                            .append(model.getBranchCode()).append("/")
                            .append(model.getSiteCode()).append("/")
                            .append("@ReplacePostName").append("/")
                            .append(generatedFileName.get()).toString();

                    Timber.e("FileName : %s", generatedFileName.get());
                    Timber.e("StoragePath %s", generatedStoragePath);
                    onFileNameGeneratedSuccessfully();
                }, Throwable::printStackTrace));
    }

    //-------------------FOR AI Profile Image-----------------------//
    private void generateFileNameAndPathForAIProfile() {
        // {attachmentSourceTypeId_employeeId_uuid}.jpg"
        generatedFileName.set(attachmentSourceId.get() + "_" + Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID) + "_" +
                generatedUUID.get() + FileUtils.EXT_JPG);
        generatedStoragePath = "Profiles/" + generatedFileName.get();
        Timber.e("FileName : %s", generatedFileName.get());
        Timber.e("StoragePath %s", generatedStoragePath);
        onFileNameGeneratedSuccessfully();
    }

    //-------------------FOR EDIT SITE POST and SPI----------------//
    private void generateFileNameAndPathForEditSitePostAndSpiPic() {
        addDisposable(taskDao.getFilePathForEditPostAndSPI(Prefs.getInt(PrefConstants.CURRENT_SITE),
                Prefs.getInt(PrefConstants.CURRENT_POST_ID_FOR_EDIT))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    //{attachmentSourceTypeId_siteId_postId_uuid.jpg}
                    StringBuilder builder = new StringBuilder();
                    builder.append(attachmentSourceId.get()).append("_")
                            .append(model.getSiteId()).append("_")
                            .append(model.getPostId()).append("_")
                            .append(generatedUUID.get()).append(FileUtils.EXT_JPG);
                    generatedFileName.set(builder.toString());
                    //{zone}/{region}/{branch}/{site}/post/{FileName.jpg}
                    builder = new StringBuilder();
                    generatedStoragePath = builder.append(model.getStoragePath()).append("/")
                            .append(generatedFileName.get()).toString();

                    Timber.e("FileName : %s", generatedFileName.get());
                    Timber.e("StoragePath %s", generatedStoragePath);

                    onFileNameGeneratedSuccessfully();
                }, Throwable::printStackTrace));
    }

    //-------------------FOR Tasks (Bill Submission, Others)--------------------//
    private void generateFileNameAndPathForTasks() {
        addDisposable(taskDao.getFilePathValuesViaTaskId(Prefs.getInt(PrefConstants.CURRENT_TASK))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                            //{attachmentSourceTypeId_siteId_taskId_uuid}
                            serverIdAsTaskId = model.getTaskId();
                            StringBuilder builder = new StringBuilder();
                            builder.append(attachmentSourceId.get()).append("_")
                                    .append(model.getSiteId()).append("_")
                                    .append(model.getTaskId()).append("_").append(generatedUUID.get()).append(FileUtils.EXT_JPG);
                            generatedFileName.set(builder.toString());
                            //{zone}/{region}/{branch}/{site}/task/{taskId}
                            builder = new StringBuilder();
                            generatedStoragePath = builder.append(model.getStoragePath()).append("/")
                                    .append(generatedFileName.get()).toString();

//                            Timber.e("TaskFileName : %s", generatedFileName.get());
//                            Timber.e("Task StoragePath %s", generatedStoragePath);
                            onFileNameGeneratedSuccessfully();
                        },
                        Throwable::printStackTrace));
    }

    //-------------------FOR BarrackInspection ({Bed,Kit,Mess,Outside,Landlord,Custodian}})--------------------//
    private void generateFileNameAndPathForBarrackTask() {
        addDisposable(taskDao.getFilePathForBarracksImages(Prefs.getInt(PrefConstants.CURRENT_TASK))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                            //{attachmentSourceTypeId_barrackId_taskId_uuid}
                            barrackId = model.getBarrackId();
                            serverIdAsTaskId = model.getTaskId();
                            StringBuilder builder = new StringBuilder();
                            builder.append(attachmentSourceId.get()).append("_")
                                    .append(model.getSiteId()).append("_")
                                    .append(generatedUUID.get()).append(FileUtils.EXT_JPG);
                            generatedFileName.set(builder.toString());
                            //{zone}/{region}/{branch}/{barrack}/task/{taskid}/{Filename.jpg}"
                            builder = new StringBuilder();
                            generatedStoragePath = builder.append(model.getZoneCode()).append("/")
                                    .append(model.getRegionCode()).append("/")
                                    .append(model.getBranchCode()).append("/")
                                    .append(model.getBarrackCode()).append("/")
                                    .append(model.getTaskName()).append("/")
                                    .append(model.getTaskId()).append("/")
                                    .append(generatedFileName.get()).toString();

                            Timber.e("Barrack FileName : %s", generatedFileName.get());
                            Timber.e("Barrack StoragePath %s", generatedStoragePath);
                            onFileNameGeneratedSuccessfully();
                        },
                        Throwable::printStackTrace));
    }

    //-------------------FOR CLOSING POA--------------------//
    /*private void generateFileNameAndPathForClosePOA() {
        addDisposable(taskDao.getFilePathForClosePOA(Prefs.getInt(PrefConstants.POA_ID_FOR_ATTACHMENT))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                            //{attachmentSourceTypeId_siteId_riskId_poaId_uuid}.jpg
                            riskId = model.getPoaId();
                            StringBuilder builder = new StringBuilder();
                            builder.append(attachmentSourceId.get()).append("_")
                                    .append(model.getSiteId()).append("_")
                                    .append(model.getRiskId()).append("_")
                                    .append(model.getPoaId()).append("_")
                                    .append(generatedUUID.get()).append(FileUtils.EXT_JPG);
                            generatedFileName.set(builder.toString());
                            //{zone}/{region}/{branch}/{site}/siterisk/{riskId}/{Filename.jpg}"
                            builder = new StringBuilder();
                            generatedStoragePath = builder.append(model.getStoragePath()).append("/")
                                    .append(generatedFileName.get()).toString();

                            Timber.e("POA FileName : %s", generatedFileName.get());
                            Timber.e("POA StoragePath %s", generatedStoragePath);
                            onFileNameGeneratedSuccessfully();
                        },
                        Throwable::printStackTrace));
    }*/

    //-------------------------------------------TEMP WORK----------------------------------------------//
    // Kindly remove this method once actual file name generator is implemented against selected attachment type
    private boolean isTempFileRequest = false;

    private void tempFileNameGeneratorReplaceWithActualGenerator() {
        isTempFileRequest = true;
        generatedFileName.set(attachmentSourceId.get() + "_" + generatedUUID.get() + FileUtils.EXT_JPG);
        onFileNameGeneratedSuccessfully();
    }

    private void onTempAttachmentMetadataInsertedSuccessfully() {
        message.what = NavigationConstants.ON_TEMP_ATTACHMENT_METADATA_INSERTED;
        liveData.postValue(message);
    }

    public void onConfirmClick(View view) {
        message.what = NavigationConstants.ON_IMAGE_CAPTURE_CONFIRM;
        liveData.postValue(message);
    }

    public void onRetakeClick(View view) {
        message.what = NavigationConstants.ON_IMAGE_RETAKE_CLICK;
        liveData.postValue(message);
    }

    public void updateAttachmentWithLatestData() {
        try {
            Objects.requireNonNull(obsAttachmentEntity.get()).fileSize = Objects.requireNonNull(compressedFileSize.get());
            Objects.requireNonNull(obsAttachmentEntity.get()).localFilePath = Objects.requireNonNull(imageUri.get()).toString();
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(obsAttachmentEntity.get()).attachmentMetaData);
            jsonObject.put("fileSize", compressedFileSize.get() + "KB");
            Objects.requireNonNull(obsAttachmentEntity.get()).attachmentMetaData = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}