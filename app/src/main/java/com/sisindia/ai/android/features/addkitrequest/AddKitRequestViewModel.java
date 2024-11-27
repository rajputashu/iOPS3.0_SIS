package com.sisindia.ai.android.features.addkitrequest;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.issues.grievance.GuardSuggestionViewListeners;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.EmployeeSiteDao;
import com.sisindia.ai.android.room.dao.GuardKitRequestDao;
import com.sisindia.ai.android.room.dao.KitRequestItemDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.GuardKitRequestEntity;
import com.sisindia.ai.android.room.entities.KitRequestItemEntity;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;
import com.sisindia.ai.android.uimodels.KitItemModel;
import com.sisindia.ai.android.uimodels.attachments.KitRequestAttachmentMetaData;
import com.sisindia.ai.android.utils.FileUtils;
import com.sisindia.ai.android.workers.AttachmentsUploadWorker;
import com.sisindia.ai.android.workers.KitRequestWorker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sisindia.ai.android.constants.PrefConstants.CURRENT_SITE;
import static com.sisindia.ai.android.room.entities.AttachmentEntity.AttachmentSourceType.KIT_REQUEST_PHOTO;
import static com.sisindia.ai.android.room.entities.AttachmentEntity.AttachmentSourceType.KIT_REQUEST_SIGNATURE;
import static com.sisindia.ai.android.utils.FileUtils.EXT_JPG;

public class AddKitRequestViewModel extends IopsBaseViewModel {

    public ObservableField<List<GuardSuggestionItem>> allGuards = new ObservableField<>(new ArrayList<>());

    public ObservableField<GuardSuggestionItem> selectedGuardObs = new ObservableField<>(new GuardSuggestionItem());

    public ObservableField<AttachmentEntity> photoAttachmentObs = new ObservableField<>(new AttachmentEntity(KIT_REQUEST_PHOTO));
    public ObservableField<AttachmentEntity> signAttachmentObs = new ObservableField<>(new AttachmentEntity(KIT_REQUEST_SIGNATURE));

    public ObservableField<KitRequestType> kitRequestType = new ObservableField<>(KitRequestType.SITE);

    public ObservableField<List<KitItemModel.KitItemSizeModel>> options = new ObservableField<>(new ArrayList<>());

    public AddKitItemRecyclerAdapter recyclerAdapter = new AddKitItemRecyclerAdapter();

    @Inject
    public EmployeeSiteDao employeeSiteDao;

    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public GuardKitRequestDao guardKitRequestDao;

    @Inject
    public KitRequestItemDao kitRequestItemDao;

    @Inject
    public TaskDao taskDao;

    private KitItemModel kitItemForSizes;

    public AddKitRequestViewListeners viewListeners = new AddKitRequestViewListeners() {

        @Override
        public void showSizesForKitItem(KitItemModel item) {
            kitItemForSizes = item;
            options.set(item.sizes);
            message.what = NavigationConstants.ON_SHOW__KIT_ITEM_SIZES;
            liveData.postValue(message);
        }
    };
    public GuardSuggestionViewListeners guardSuggestionViewListeners = new GuardSuggestionViewListeners() {

        @Override
        public void fetchGuardFromServer(@NotNull String employeeNo) {
//            EmployeeSiteEntity emp = new EmployeeSiteEntity();
//            emp.employeeNo = employeeNo;
//            fetchEmployeeFromServer(emp);
        }

        @Override
        public void onGuardSelected(@NotNull GuardSuggestionItem item) {
            kitItemForSizes = null;
            photoAttachmentObs.set(new AttachmentEntity(KIT_REQUEST_PHOTO));
            signAttachmentObs.set(new AttachmentEntity(KIT_REQUEST_SIGNATURE));
            selectedGuardObs.set(item);
            fetchKitItems();
        }
    };
    private TaskTimer tik = new TaskTimer(0);


    @Inject
    public AddKitRequestViewModel(@NonNull Application application) {
        super(application);
    }


    public void fetchKitItems() {
        recyclerAdapter.clear();
        addDisposable(guardKitRequestDao.fetAllKitItems()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(kitItems -> {
                    for (KitItemModel kitItem : kitItems) {
                        addDisposable(guardKitRequestDao.fetAllKitItemSizes(kitItem.id)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(sizes -> {
                                    kitItem.sizes = sizes;
                                    recyclerAdapter.add(kitItem);
                                }));
                    }
                }));
    }

    public void initViewModel(int employeeId) {
//        int siteId = Prefs.getInt(CURRENT_SITE);
        if (employeeId == 0) {
            kitRequestType.set(KitRequestType.SITE);
            addDisposable(employeeSiteDao.fetchAllGuardsForKitRequest()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(guards -> allGuards.set(guards)));
        } else {
            kitRequestType.set(KitRequestType.EMPLOYEE);
            addDisposable(employeeSiteDao.fetchGuardsForKitRequest(employeeId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(guard -> {
                        kitItemForSizes = null;
                        photoAttachmentObs.set(new AttachmentEntity(KIT_REQUEST_PHOTO));
                        signAttachmentObs.set(new AttachmentEntity(KIT_REQUEST_SIGNATURE));
                        selectedGuardObs.set(guard);
                        fetchKitItems();
                    }, Timber::e));
        }
    }

    public void onAddKitRequestClick(View view) {

        GuardSuggestionItem guard = selectedGuardObs.get();
        int taskId = Prefs.getInt(PrefConstants.TASK_SERVER_ID);
        int siteId = Prefs.getInt(CURRENT_SITE);
        int employeeId = Objects.requireNonNull(guard).employeeId;
        if (guard.employeeId == 0) {
            showToast("Please select Guard");
            return;
        }

        List<KitItemModel> selectedKitItems = recyclerAdapter.getSelectedItems();

        if (selectedKitItems.size() == 0) {
            showToast("Please select item to replace");
            return;
        }

        AttachmentEntity photoAttachment = photoAttachmentObs.get();
        AttachmentEntity signAttachment = signAttachmentObs.get();

        if (signAttachment == null || TextUtils.isEmpty(signAttachment.localFilePath)) {
            showToast("Please Add guard Signature");
            return;
        }

        kitRequestSignatureAttachment(guard, signAttachment);

        GuardKitRequestEntity guardKitRequestItem = new GuardKitRequestEntity(employeeId, signAttachment.attachmentGuid, taskId, siteId);
        if (photoAttachment != null && !TextUtils.isEmpty(photoAttachment.localFilePath)) {
            kitRequestPicAttachment(guard, photoAttachment);
            guardKitRequestItem.imageAttachmentGuid = photoAttachment.attachmentGuid;
        }

        addDisposable(guardKitRequestDao.insert(guardKitRequestItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    List<KitRequestItemEntity> items = new ArrayList<>();
                    for (KitItemModel kitItem : selectedKitItems) {
                        KitRequestItemEntity item = new KitRequestItemEntity(row.intValue(), employeeId, kitItem.id, taskId, siteId);
                        if (kitItem.selectedSize != null) {
                            item.kitSizeId = kitItem.selectedSize.id;
                        }
                        items.add(item);
                    }

                    addDisposable(kitRequestItemDao.insertAllKitRequestItems(items)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((kitItemsRows, throwable) -> {
                                if (throwable == null) {
                                    onKitRequestItemDone(row);
                                } else {
                                    showToast("Unable to create Kit Request...");
                                }
                            }));
                }, Timber::e));
    }

    private void kitRequestPicAttachment(GuardSuggestionItem guard, AttachmentEntity photoAttachment) {
        int employeeId = guard.employeeId;
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(
                guardKitRequestDao.attachmentFileAndStorageForKitRequestSignature(photoAttachment.attachmentSourceType, employeeId, photoAttachment.attachmentGuid, taskId)
                        .compose(transformSingle())
                        .subscribe(attachmentModel -> {
                            photoAttachment.storagePath = attachmentModel.getStoragePath().concat(FileUtils.EXT_PNG);
                            KitRequestAttachmentMetaData metaData = new KitRequestAttachmentMetaData();
                            metaData.attachmentTypeId = 1;//for image
                            metaData.attachmentSourceTypeId = photoAttachment.attachmentSourceType;
                            metaData.uuid = photoAttachment.attachmentGuid;
                            metaData.fileName = attachmentModel.getFileName().concat(EXT_JPG);
                            metaData.fileSize = String.valueOf(photoAttachment.fileSize);
                            metaData.storagePath = photoAttachment.storagePath;
                            metaData.siteId = guard.siteId;
                            metaData.taskId = attachmentModel.getServerId();
                            metaData.employeeId = guard.employeeId;
                            metaData.employeeNo = guard.employeeNo;

                            photoAttachment.attachmentMetaData = new Gson().toJson(metaData);
                            photoAttachment.isDone = true;
                            addDisposable(attachmentDao.insert(photoAttachment)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(row -> Timber.d("Attachment Added"), Timber::e));

                        }, Timber::e));
    }

    private void kitRequestSignatureAttachment(GuardSuggestionItem guard, AttachmentEntity signAttachment) {
        int employeeId = guard.employeeId;
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(
                guardKitRequestDao.attachmentFileAndStorageForKitRequestSignature(signAttachment.attachmentSourceType, employeeId, signAttachment.attachmentGuid, taskId)
                        .compose(transformSingle())
                        .subscribe(attachmentModel -> {
                            signAttachment.storagePath = attachmentModel.getStoragePath().concat(FileUtils.EXT_PNG);
                            KitRequestAttachmentMetaData metaData = new KitRequestAttachmentMetaData();
                            metaData.attachmentTypeId = 1;//for image
                            metaData.attachmentSourceTypeId = signAttachment.attachmentSourceType;
                            metaData.uuid = signAttachment.attachmentGuid;
                            metaData.fileName = attachmentModel.getFileName().concat(EXT_JPG);
                            metaData.fileSize = String.valueOf(signAttachment.fileSize);
                            metaData.storagePath = signAttachment.storagePath;
                            metaData.siteId = guard.siteId;
                            metaData.taskId = attachmentModel.getServerId();
                            metaData.employeeId = guard.employeeId;
                            metaData.employeeNo = guard.employeeNo;

                            signAttachment.attachmentMetaData = new Gson().toJson(metaData);
                            signAttachment.isDone = true;
                            addDisposable(attachmentDao.insert(signAttachment)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(row -> Timber.d("Attachment Added"), Timber::e));

                        }, Timber::e));
    }


    private void onKitRequestItemDone(Long row) {
        oneTimeWorkerWithNetwork(KitRequestWorker.class);
        oneTimeWorkerWithNetwork(AttachmentsUploadWorker.class);
        message.what = NavigationConstants.ON_KIT_REQUEST_DONE;
        message.arg1 = row.intValue();
        liveData.postValue(message);
    }

    public void onImageClear(View view) {
        photoAttachmentObs.set(new AttachmentEntity(KIT_REQUEST_PHOTO));
    }

    public void onSignatureClear(View view) {
        signAttachmentObs.set(new AttachmentEntity(KIT_REQUEST_SIGNATURE));
    }

    public void onKitRequestCaptureImageClick(View view) {
        AttachmentEntity photoAttachment = photoAttachmentObs.get();
        message.what = NavigationConstants.ON_KIT_REQUEST_CAPTURE_IMAGE;
        message.obj = photoAttachment;
        liveData.postValue(message);
    }

    public void onKitRequestCaptureSignatureClick(View view) {
        AttachmentEntity signAttachment = signAttachmentObs.get();
        message.what = NavigationConstants.ON_KIT_REQUEST_CAPTURE_SIGNATURE;
        message.obj = signAttachment;
        liveData.postValue(message);
    }

    public void onKitItemSizeSelected(View view) {
        kitItemForSizes.isSelected = true;
        recyclerAdapter.updateItem(kitItemForSizes);
    }

    public void onKitItemSizeCancelled(View view) {
        kitItemForSizes.isSelected = false;
        kitItemForSizes.selectedSize = null;
        recyclerAdapter.updateItem(kitItemForSizes);
    }

    public void onCheckedChange(RadioGroup group, int check) {
        Timber.e("");
        for (int i = 0; i < group.getChildCount(); i++) {
            AppCompatRadioButton rb = (AppCompatRadioButton) group.getChildAt(i);
            if (rb.isChecked()) {
                Timber.e("");
                KitItemModel.KitItemSizeModel size = (KitItemModel.KitItemSizeModel) rb.getTag();
                if (size != null)
                    kitItemForSizes.selectedSize = size;
                return;
            } else {
                kitItemForSizes.selectedSize = null;
            }
        }
    }

    /*public void fetchEmployeeFromServer(EmployeeSiteEntity item) {
        setIsLoading(true);
        addDisposable(coreApi.getEmployeeByEmployeeNo(item.employeeNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((empResponse, throwable) -> {
                    if (throwable != null) {
                        this.onApiError(throwable);
                        Timber.e(throwable);
                        return;
                    }
                    setIsLoading(false);
                    onGuardFound(empResponse.emp);
                }));
    }*/

    /*public void onGuardFound(EmployeeSiteEntity emp) {
        if (emp != null && emp.employeeId != 0) {
            addDisposable(employeeSiteDao.insert(emp)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> {
                        Timber.d("employee added");
                    }, Timber::e));

//            message.arg1 = emp.employeeId;
//            message.what = NavigationConstants.ON_OPEN_ADD_GRIEVANCE_DETAIL;
//            liveData.postValue(message);
        }
    }*/

    void startTimer() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(taskDao.fetchTimeSpentViaTaskId(taskId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    tik = new TaskTimer(model.getTimeElapsed() - findTimeDifference(model.getLastElapsedTime()));
                    tik.start();
                }, Timber::e));
    }

    void stopTimer() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik, getCurrentMilliOfTheDay())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> tik.cancel(), Timber::e));
    }

    public enum KitRequestType {
        SITE, EMPLOYEE
    }
}