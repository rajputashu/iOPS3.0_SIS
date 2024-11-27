package com.sisindia.ai.android.features.units;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.features.units.entity.PendingQRCountsMO;
import com.sisindia.ai.android.features.units.entity.SiteTaskDetailsMO;
import com.sisindia.ai.android.models.site.MySitesResponseMO;
import com.sisindia.ai.android.room.customdao.SiteAndTasksDao;
import com.sisindia.ai.android.room.dao.PostCheckListDao;
import com.sisindia.ai.android.room.dao.PostRegistersDao;
import com.sisindia.ai.android.room.dao.SiteCheckListDao;
import com.sisindia.ai.android.room.dao.SiteDao;
import com.sisindia.ai.android.room.dao.SiteExtensionDao;
import com.sisindia.ai.android.room.dao.SitePostDao;
import com.sisindia.ai.android.room.dao.SiteStrengthDao;
import com.sisindia.ai.android.room.entities.PostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.PostRegisterEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.SiteConfiguration;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.room.entities.SitePostEntity;
import com.sisindia.ai.android.room.entities.SiteStrengthEntity;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_DRAWER;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_UNITS_DETAILS;

public class DashBoardUnitsViewModel extends IopsBaseViewModel {

    public ObservableField<String> taggedQRCodeCount = new ObservableField<>("");
    public ObservableField<String> qrTaggedPercentage = new ObservableField<>("");
    public ObservableField<Integer> progressBarValue = new ObservableField<>(0);
    private boolean isSyncing = false;
    public UnitListener listener = () -> {
        message.what = OPEN_UNITS_DETAILS;
        liveData.postValue(message);
    };

    @Inject
    SiteAndTasksDao siteAndTaskDao;

    @Inject
    SitePostDao sitePostDao;

    @Inject
    public SiteDao siteDao;

    @Inject
    public SiteCheckListDao siteCheckListDao;

    @Inject
    public SiteExtensionDao siteExtensionDao;

    @Inject
    public SiteStrengthDao strengthDao;

    @Inject
    public PostRegistersDao postRegistersDao;

    @Inject
    public PostCheckListDao postCheckListDao;

    private UnitsAdapter unitsAdapter;

    @Inject
    public DashBoardUnitsViewModel(@NonNull Application application) {
        super(application);
    }

    public void updateQRCount(String qrCount) {
        taggedQRCodeCount.set(qrCount);
    }

    @SuppressLint("DefaultLocale")
    public void calculatePercentage(int qrPending, int qrTotal) {
        try {
            double percentage = (((float) qrPending / (float) qrTotal)) * 100;
            if (!Double.isNaN(percentage))
                qrTaggedPercentage.set(String.format("%.2f", percentage) + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateProgress(int qrPending, int qrTotal) {
//        int taggingDoneCount = qrTotal - qrPending;
        float percentage = (((float) qrPending / (float) qrTotal)) * 100;
        int progress = Math.round(percentage);
        progressBarValue.set(progress);
    }

    public void ivRotaDrawerClick(View view) {
        if (view.getId() == R.id.myUnitsMenuIcon) {
            message.what = OPEN_DASH_BOARD_DRAWER;
            liveData.postValue(message);
        } else if (view.getId() == R.id.myUnitsRefreshIcon) {
            getSiteDetailsFromAPI();
        }
    }

    public UnitsAdapter unitAdapter() {
        unitsAdapter = new UnitsAdapter(listener);
        return unitsAdapter;
    }

    public void getUnitsDetailsFromDB() {
        setIsLoading(true);
        addDisposable(siteAndTaskDao.fetchAllMySites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUnitTaskFetch, this::onError));
    }

    public void getQRCountsFromDB() {
        setIsLoading(true);
        addDisposable(sitePostDao.getPendingQRCounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onQRCountsFetch, this::onError));
    }

    private void onUnitTaskFetch(List<SiteTaskDetailsMO> list) {
        setIsLoading(false);
        unitsAdapter.updateSiteTaskList(list);
    }

    private void onQRCountsFetch(PendingQRCountsMO entity) {
        setIsLoading(false);
        updateQRCount(getApplication().getResources().getString(R.string.dynamic_qr_tagged_at_post,
                entity.getQrCount(), entity.getTotalPost()));
        calculatePercentage(entity.getQrCount(), entity.getTotalPost());
        calculateProgress(entity.getQrCount(), entity.getTotalPost());
    }

    private void onError(Throwable throwable) {
        setIsLoading(false);
        Timber.e(throwable);
        Toast.makeText(getApplication(), "Error while fetching details...", Toast.LENGTH_SHORT).show();
    }

    private void getSiteDetailsFromAPI() {
        if (isSyncing) {
            showToast("Syncing in progress, please wait!!!");
            return;
        }

        isSyncing = true;
        setIsLoading(true);
        addDisposable(coreApi.getRefreshedSiteList()
                .compose(transformSingle())
                .subscribe(this::onRefreshedSiteList,
                        e -> {
                            setIsLoading(false);
                            isSyncing = false;
                            e.printStackTrace();
                        }
                ));
    }

    private void onRefreshedSiteList(MySitesResponseMO response) {
        isSyncing = false;
        if (response.statusCode == 200) {
//            if (response.getMySitesData() != null && response.getMySitesData().size() != 0) {
            if (response.getMySitesData() != null) {
                deleteAndInsertSites(response.getMySitesData().getMySitesList());
                deleteAndInsertSiteStrength(Objects.requireNonNull(response.getMySitesData()).getSiteStrengthList());
                deleteAndInsertSitePosts(Objects.requireNonNull(response.getMySitesData()).getSitePostList());
            }

            new Handler().postDelayed(() -> {
                setIsLoading(false);
                getUnitsDetailsFromDB();
                getQRCountsFromDB();
            }, 3000);
        } else {
            setIsLoading(false);
        }
    }

    private void deleteAndInsertSites(List<SiteEntity> mySiteList) {
        if (mySiteList != null && mySiteList.size() > 0) {
            addDisposable(siteDao.deleteSite()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
//                        Timber.i("sites deleted");
                        addDisposable(siteDao
//                                    .insertAllSites(response.getMySitesData())
                                .insertAllSites(mySiteList)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(rows -> {
                                }, Timber::e));
                    }, Timber::e));

            addDisposable(siteExtensionDao.deleteSiteExtension()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        for (SiteEntity site : mySiteList) {
                            if (site.siteExtension != null) {
                                site.siteExtension.siteId = site.id;
                                addDisposable(siteExtensionDao
                                        .insertSiteExtension(site.siteExtension)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(row -> {
                                        }, Timber::e));
                            }
                        }
                    }));

            addDisposable(siteCheckListDao.deleteSiteCheckList()
                    .mergeWith(siteCheckListDao.deleteSiteCheckListOption())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
//                        Timber.i("siteChecklists deleted");
//                                for (SiteEntity site : response.getMySitesData()) {
                        for (SiteEntity site : mySiteList) {
                            if (site.siteExtension != null && site.siteExtension.siteConfiguration != null) {
                                SiteConfiguration siteConfiguration = site.siteExtension.siteConfiguration;
                                if (siteConfiguration.siteChecklists != null && siteConfiguration.siteChecklists.size() != 0) {
                                    for (SiteCheckListEntity checkList : siteConfiguration.siteChecklists) {
                                        checkList.siteId = site.id;
                                        addDisposable(siteCheckListDao.insertSiteCheckList(checkList)
                                                .subscribeOn(Schedulers.computation())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(row -> {
                                                    if (checkList.siteChecklistOptions != null && checkList.siteChecklistOptions.size() != 0) {
                                                        for (SiteCheckListOptionEntity option : checkList.siteChecklistOptions) {
                                                            option.siteId = site.id;
                                                            option.siteChecklistId = checkList.siteChecklistId;
                                                            addDisposable(siteCheckListDao.insertSiteChecklistOption(option)
                                                                    .subscribeOn(Schedulers.newThread())
                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                    .subscribe(opRow -> {
                                                                    }, Timber::e));
                                                        }
                                                    }
                                                }, Timber::e));
                                    }
                                }
                            }
                        }
                    }));
        }
    }

    private void deleteAndInsertSiteStrength(List<SiteStrengthEntity> siteStrengthList) {
        if (siteStrengthList != null && siteStrengthList.size() > 0) {
            addDisposable(strengthDao.deleteSiteStrength()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
//                        Timber.i("siteStrengths deleted");
                        addDisposable(strengthDao.insertAllSiteStrength(siteStrengthList)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(rows -> {
                                }, Timber::e));
                    }, Timber::e));
        }
    }

    private void deleteAndInsertSitePosts(List<SitePostEntity> sitePostList) {
        if (sitePostList != null && sitePostList.size() > 0) {
            addDisposable(sitePostDao.deleteSitePost()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        for (SitePostEntity postEntity : sitePostList) {
                            if (postEntity.isActive) {
                                addDisposable(sitePostDao.insert(postEntity)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(rows -> {
                                        }, Timber::e));
                            }
                        }
                        /*addDisposable(sitePostDao.insertAllSitePost(sitePostList)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(rows -> Timber.i("sitePosts inserted"), Timber::e));*/

                    }, Timber::e));

            addDisposable(postRegistersDao.deletePostRegister()
                    .mergeWith(postCheckListDao.deletePostCheckList())
                    .mergeWith(postCheckListDao.deletePostCheckListOption())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        for (SitePostEntity post : sitePostList) {
                            if (post.isActive) {
                                if (post.postConfiguration != null && post.postConfiguration.postRegisters != null) {
                                    for (PostRegisterEntity pr : post.postConfiguration.postRegisters) {
                                        pr.siteId = post.siteId;
                                        pr.postId = post.id;
                                        addDisposable(postRegistersDao.insertPostRegister(pr)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(prRow -> Timber.i("postRegister inserted"), Timber::e));
                                    }
                                }

                                if (post.postConfiguration != null && post.postConfiguration.postChecklists != null) {
                                    for (PostCheckListEntity pc : post.postConfiguration.postChecklists) {
                                        pc.siteId = post.siteId;
                                        pc.postId = post.id;
                                        addDisposable(postCheckListDao.insertPostCheckList(pc)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(row -> {
//                                                Timber.i("postChecklist inserted");
                                                    if (pc.postChecklistOptions != null) {
                                                        for (PostCheckListOptionEntity pco : pc.postChecklistOptions) {
                                                            pco.siteId = post.siteId;
                                                            pco.postId = post.id;
                                                            pco.postChecklistId = pc.postChecklistId;
                                                            addDisposable(postCheckListDao.insertPostCheckListOption(pco)
                                                                    .subscribeOn(Schedulers.io())
                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                    .subscribe(pcoRow -> Timber.i("postChecklistOption inserted"), Timber::e));
                                                        }
                                                    }
                                                }, Timber::e));
                                    }
                                }
                            }
                        }
                    }, Timber::e));
        }
    }
}