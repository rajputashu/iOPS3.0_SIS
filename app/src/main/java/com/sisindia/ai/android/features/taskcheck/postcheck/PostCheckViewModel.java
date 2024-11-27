package com.sisindia.ai.android.features.taskcheck.postcheck;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.room.EmptyResultSetException;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.commons.GroupCompany;
import com.sisindia.ai.android.commons.MenuNavigationItem;
import com.sisindia.ai.android.commons.MenuNavigationListeners;
import com.sisindia.ai.android.commons.MenuNavigationRecyclerAdapter;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.addkitrequest.KitRequestRecyclerAdapter;
import com.sisindia.ai.android.room.dao.AddSecurityRiskDao;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.GuardKitRequestDao;
import com.sisindia.ai.android.room.dao.RegistersDao;
import com.sisindia.ai.android.room.dao.SitePostDao;
import com.sisindia.ai.android.room.dao.SiteStrengthDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.room.entities.CheckedPostEntity;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.uimodels.BreadCumItemModel;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;
import com.sisindia.ai.android.uimodels.KitRequestModel;
import com.sisindia.ai.android.uimodels.PostCheckModel;
import com.sisindia.ai.android.uimodels.PostValidationModel;
import com.sisindia.ai.android.uimodels.SecurityRiskModel;
import com.sisindia.ai.android.uimodels.SitePostModel;
import com.sisindia.ai.android.workers.AttachmentsUploadWorker;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PostCheckViewModel extends IopsBaseViewModel {

    public ObservableField<CheckedPostEntity> selectedPostObs = new ObservableField<>(new CheckedPostEntity());
    public GuardCheckRecyclerAdapter guardCheckRecyclerAdapter = new GuardCheckRecyclerAdapter();
    public SecurityCheckRecyclerAdapter securityCheckRecyclerAdapter = new SecurityCheckRecyclerAdapter();
    public KitRequestRecyclerAdapter kitRequestRecyclerAdapter = new KitRequestRecyclerAdapter();
    public PostCheckListRecyclerAdapter postCheckListAdapter = new PostCheckListRecyclerAdapter();
    public RegisterCheckRecyclerAdapter registerCheckRecyclerAdapter = new RegisterCheckRecyclerAdapter();
    public ObservableField<List<BreadCumItemModel>> breadCums = new ObservableField<>(new ArrayList<>());
    public MenuNavigationRecyclerAdapter menuNavigationAdapter = new MenuNavigationRecyclerAdapter();
    public MenuNavigationListeners menuNavigationListeners = PostCheckViewModel.this::onMenuNavigationClick;
    public ObservableField<PostCheckModel> itemObs = new ObservableField<>(new PostCheckModel());

    public ObservableInt noOfGuards = new ObservableInt(0);
    public ObservableInt noOfSecurityRisks = new ObservableInt(0);
    public ObservableInt noOfKitRequests = new ObservableInt(0);
    public ObservableInt noOfRegisters = new ObservableInt(0);
    public ObservableInt noOfCheckLists = new ObservableInt(0);
    //    public int countryId = Prefs.getInt(PrefConstants.COUNTRY_ID);
    public int companyId = Prefs.getInt(PrefConstants.COMPANY_ID);

    @Inject
    public DayCheckDao dayCheckDao;
    @Inject
    public AddSecurityRiskDao riskDao;
    @Inject
    public TaskDao taskDao;
    @Inject
    public GuardKitRequestDao kitRequestDao;
    @Inject
    public SitePostDao sitePostDao;
    @Inject
    public RegistersDao registersDao;
    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public SiteStrengthDao siteStrengthDao;

    public PostCheckViewListeners postCheckViewListeners = new PostCheckViewListeners() {

        @Override
        public void onSitePostItemClick(SitePostModel item) {

        }

        @Override
        public void onCheckedGuardClick(CheckedGuardItemModel item) {
            if (item.checkedGuardStatus == CheckedGuardEntity.CheckedGuardStatus.COMPLETED.getCheckedGuardStatus()) {
                showToast("Guard is already checked");
                return;
            }
            onPendingCheckedGuardClick(item);
        }

        @Override
        public void onPostCheckListItemClick(CheckedPostCheckListEntity item) {
            message.obj = item;
            message.what = NavigationConstants.ON_POST_CHECK_LIST_ITEM_CLICK;
            liveData.postValue(message);
        }

        @Override
        public void onCheckedRegisterClick() {
            message.what = NavigationConstants.OPEN_CHECK_REGISTER_SCREEN;
            liveData.postValue(message);
        }
    };

    private TaskTimer tik = new TaskTimer(0);

    @Inject
    public PostCheckViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        /* Get updated task item */
        addDisposable(taskDao.fetchPostCheckModel(taskId, postId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPostCheckModelFetch, Timber::e));

        addDisposable(dayCheckDao.fetchAllCheckedGuardByPostId(postId, taskId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCheckedGuardFetch, this::onFetchError));

        addDisposable(riskDao.fetchAllCheckedSecurityRiskByPostId(postId, taskId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSecurityFetch, this::onFetchError));

        addDisposable(kitRequestDao.fetchAllSiteKitRequests(siteId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onKitRequestFetch, this::onFetchError));

        /* get All post check list*/
        addDisposable(dayCheckDao.fetchAllCheckedPostCheckListByTaskAndSite(taskId, siteId, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCheckedPostCheckListFetch, Timber::e));

        addDisposable(registersDao.fetchAllRegistersForPost(taskId, siteId, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRegistersFetched, Timber::e));

        addDisposable(dayCheckDao.fetchPostById(postId, siteId, taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((checkedPost, postNotFoundEx) -> {
                    if (checkedPost == null && (postNotFoundEx instanceof EmptyResultSetException)) {
                        checkedPost = new CheckedPostEntity(postId, siteId, taskId);
                    } else if (checkedPost != null) {
                        checkedPost.updatedDateTime = LocalDateTime.now().toString();
                    }
                    updateCheckedPost(checkedPost);
                }));

        /*if (!isGuardCheckPercentCalculated)
            addDisposable(Single.zip(siteStrengthDao.getSiteAuthStrength(siteId),
                    siteStrengthDao.getNoOfPostAtSite(siteId), (authStrength, noOfPost) -> {
                        float strength;
                        if (noOfPost == 0)
                            strength = ((float) authStrength * 10) / 100;
                        else
                            strength = (((float) authStrength / noOfPost) * 10) / 100;

                        if (strength > 1)
                            noOfGuardsNeedsToCheck = Math.round(strength);
                        else
                            noOfGuardsNeedsToCheck = 1;

                        return true;
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isCalculated -> {
                        isGuardCheckPercentCalculated = isCalculated;
                    }, Timber::e));*/
    }

    private void onPostCheckModelFetch(PostCheckModel model) {
        itemObs.set(model);

        List<BreadCumItemModel> items = new ArrayList<>();
        menuNavigationAdapter.clear();

        items.add(BreadCumItemModel.guardItem(model.noOfCheckedGuards));
        items.add(BreadCumItemModel.registerItem(model.availableRegisters, model.noOfCheckedRegisters, model.availableRegisters == 0));
        items.add(BreadCumItemModel.postCheckListItem(model.availablePostCheckList, model.postCheckListDone, model.availablePostCheckList == 0));

        menuNavigationAdapter.add(MenuNavigationItem.checkGuard());
        if (model.availableRegisters != 0) {
            menuNavigationAdapter.add(MenuNavigationItem.checkRegister());
        }

        menuNavigationAdapter.add(MenuNavigationItem.addSecurity());
        menuNavigationAdapter.add(MenuNavigationItem.addGrievance());

        if (companyId == GroupCompany.SIS.getCompanyId()) {
            menuNavigationAdapter.add(MenuNavigationItem.addKitRequest());
        }

        breadCums.set(items);
    }

    private void onPendingCheckedGuardClick(CheckedGuardItemModel item) {
        Prefs.putInt(PrefConstants.CURRENT_TASK, item.taskId);
        Prefs.putInt(PrefConstants.CURRENT_POST, item.postId);
        Prefs.putInt(PrefConstants.CURRENT_SITE, item.siteId);
        Prefs.putInt(PrefConstants.SELECTED_EMPLOYEE_ID, item.employeeId);
        Prefs.putString(PrefConstants.SELECTED_EMPLOYEE_NO, item.employeeNo); // adding

        message.what = NavigationConstants.ON_PENDING_GUARD_CLICK;
        liveData.postValue(message);
    }

    private void onMenuNavigationClick(MenuNavigationItem model) {

        switch (model.itemType) {
            case CHECK_GUARD:
                message.what = NavigationConstants.OPEN_CHECK_GUARD_SCREEN;
                break;
            case CHECK_REGISTER:
                message.what = NavigationConstants.OPEN_CHECK_REGISTER_SCREEN;
                break;
            case ADD_SECURITY:
                message.what = NavigationConstants.OPEN_CHECK_SECURITY_RISK_SCREEN;
                break;
            case ADD_GRIEVANCE:
                message.what = NavigationConstants.ON_ADD_GRIEVANCE_CLICK;
                break;
            case ADD_KIT_REQUEST:
                message.what = NavigationConstants.OPEN_CHECK_KIT_REQUEST_SCREEN;
                break;
        }

        liveData.postValue(message);
    }

    public void onContinueBtnClick(View view) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        addDisposable(dayCheckDao.validatePostOnCompleted(taskId, siteId, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPostValidationFetch, Timber::e));
    }

    private void onPostValidationFetch(PostValidationModel model) {

        if (model.countOfCheckedGuard == 0) {
            showToast("Check minimum 1 guard to complete");
            return;
        }

        /*if (model.countOfCheckedGuard < noOfGuardsNeedsToCheck) {
            showToast("Check minimum " + noOfGuardsNeedsToCheck + " guard(s) to complete");
            return;
        }*/

        /*if (model.countOfCheckedGuard < model.minGuardCheckCount) {
            showToast("Check minimum " + model.minGuardCheckCount + " guard(s) to complete");
            return;
        }*/

        /*if (model.countOfCheckedPost < model.minPostCheckCount) {
            showToast("Check minimum " + model.minPostCheckCount + " post(s) to complete");
            return;
        }*/

        if (model.totalRegisters != 0 && model.totalRegisters != model.countOfCheckedRegister) {
            showToast("Register check is pending..");
            return;
        }

        if (model.totalPostCheckList != 0 && model.editedPostCheckList != model.totalPostCheckList) {
            showToast("Post Check list is pending..");
            return;
        }

        //update all the checked registers
        /*addDisposable(registersDao.fetchAllRegisterAttachments(taskId, siteId, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    for (RegisterAttachmentEntity item : items) {
                        addDisposable(attachmentDao.insert(new AttachmentEntity(item.localPath))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(registerAttachmentId -> {
                                    item.registerAttachmentId = registerAttachmentId.intValue();
                                    updateRegisterAttachment(item);
                                }, Timber::e));
                    }
                }, Timber::e));*/

        //update post check status
        addDisposable(dayCheckDao.updateCheckedPostStatus(model.checkedPostId,
                CheckedPostEntity.CheckedPostStatus.COMPLETED.getPostStatus())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    oneTimeWorkerWithNetwork(AttachmentsUploadWorker.class);
                    message.what = NavigationConstants.ON_POST_CHECK_DONE;
                    liveData.postValue(message);
                }, Timber::e));
    }

    /*private boolean isMinGuardChecked() {
        return true;
    }

    private void updateRegisterAttachment(RegisterAttachmentEntity item) {
        addDisposable(registersDao.insertRegisterAttachments(item)
                .compose(transformSingle()).subscribe(row -> Timber.d("item updated"), Timber::e));
    }*/

    private void onCheckedPostCheckListFetch(List<CheckedPostCheckListEntity> list) {
        noOfCheckLists.set(list.size());
        postCheckListAdapter.clearAndSetItems(list);
    }

    private void onKitRequestFetch(List<KitRequestModel> list) {
        noOfKitRequests.set(list.size());
        kitRequestRecyclerAdapter.clearAndSetItems(list);
    }

    private void onSecurityFetch(List<SecurityRiskModel> list) {
        noOfSecurityRisks.set(list.size());
        securityCheckRecyclerAdapter.clearAndSetItems(list);
    }

    private void onFetchError(Throwable throwable) {
        Timber.e(throwable);
    }

    private void onCheckedGuardFetch(List<CheckedGuardItemModel> list) {
        noOfGuards.set(list.size());
        guardCheckRecyclerAdapter.clearAndSetItems(list);
    }

    private void onRegistersFetched(List<CheckedRegisterEntity> items) {
        noOfRegisters.set(items.size());
        registerCheckRecyclerAdapter.clear();
        for (CheckedRegisterEntity item : items) {
            addDisposable(registersDao.fetchLastTwoRegisterDocuments(item.taskId, item.siteId, item.postId, item.registerTypeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((documents, throwable) -> {
                        item.documents = documents;
                        registerCheckRecyclerAdapter.add(item);
                    }));
        }
    }

    private void updateCheckedPost(CheckedPostEntity checkedPost) {
        selectedPostObs.set(checkedPost);
        addDisposable(dayCheckDao.insertCheckedPost(checkedPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> Timber.d("updated %s", row), Timber::e));
    }

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
}