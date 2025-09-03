package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.UserMasterDataResponseV2;
import com.sisindia.ai.android.room.dao.BarrackDao;
import com.sisindia.ai.android.room.dao.BarrackStrengthDao;
import com.sisindia.ai.android.room.dao.BillCollectionDao;
import com.sisindia.ai.android.room.dao.ContractsDao;
import com.sisindia.ai.android.room.dao.CustomerContactDao;
import com.sisindia.ai.android.room.dao.CustomerDao;
import com.sisindia.ai.android.room.dao.CustomerSiteContactDao;
import com.sisindia.ai.android.room.dao.EmployeeLeaveDao;
import com.sisindia.ai.android.room.dao.EmployeeSiteDao;
import com.sisindia.ai.android.room.dao.KitItemDao;
import com.sisindia.ai.android.room.dao.PostCheckListDao;
import com.sisindia.ai.android.room.dao.PostRegistersDao;
import com.sisindia.ai.android.room.dao.SiteAtRiskDao;
import com.sisindia.ai.android.room.dao.SiteBillingCheckListDao;
import com.sisindia.ai.android.room.dao.SiteCheckListDao;
import com.sisindia.ai.android.room.dao.SiteDao;
import com.sisindia.ai.android.room.dao.SiteExtensionDao;
import com.sisindia.ai.android.room.dao.SitePostDao;
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao;
import com.sisindia.ai.android.room.dao.SiteShiftDao;
import com.sisindia.ai.android.room.dao.SiteStrengthDao;
import com.sisindia.ai.android.room.entities.PostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.PostRegisterEntity;
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;
import com.sisindia.ai.android.room.entities.SiteConfiguration;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.room.entities.SitePostEntity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class UserMasterDataWorkerV2 extends BaseWorker {

    @Inject
    public BarrackDao barrackDao;

    @Inject
    public SiteDao siteDao;

    @Inject
    public EmployeeLeaveDao employeeLeaveDao;

    @Inject
    public BillCollectionDao billCollectionDao;

    @Inject
    public SiteExtensionDao siteExtensionDao;

    @Inject
    public SiteCheckListDao siteCheckListDao;

    @Inject
    public CustomerDao customerDao;

    @Inject
    public CustomerContactDao customerContactDao;

    @Inject
    public EmployeeSiteDao employeeSiteDao;

    @Inject
    public SiteStrengthDao strengthDao;

    @Inject
    public SiteShiftDao siteShiftDao;

    @Inject
    public CustomerSiteContactDao customerSiteContactDao;

    @Inject
    public SiteAtRiskDao siteAtRiskDao;

    @Inject
    public SiteRiskPoaDao siteRiskPoaDao;

    @Inject
    public SitePostDao sitePostDao;

    @Inject
    public PostRegistersDao postRegistersDao;

    @Inject
    public PostCheckListDao postCheckListDao;

    @Inject
    public SiteBillingCheckListDao siteBillingCheckListDao;

    @Inject
    public ContractsDao contractsDao;

    @Inject
    public BarrackStrengthDao barrackStrengthDao;

    @Inject
    public KitItemDao kitItemDao;

    @Inject
    public UserMasterDataWorkerV2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        Timber.e("User Master Data V2 Called");
        getUserMasterDataFromAPI();
//        getEmployeeSites(); // Commenting as we getting emp details from EmployeeRewardFineWorker
        return Result.success();
    }

    private void getUserMasterDataFromAPI() {
        addDisposable(mastersApi.getUserMasterDataV2()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Prefs.putBoolean(PrefConstants.USER_MASTER_DATA_SYNC, true);
                    onUserMasterDataResponse(response);
                }, throwable -> {
                    throwable.printStackTrace();
                    Prefs.putBoolean(PrefConstants.USER_MASTER_DATA_SYNC, false);
                }));
    }

    /*private void getEmployeeSites() {
        addDisposable(coreApi.getEmployeeSites()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && !response.getEmployees().isEmpty()) {
                        for (EmployeeSiteEntity item : response.getEmployees()) {
                            addDisposable(employeeSiteDao.updateDeployedDate(item.deployedDate, item.employeeId)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(row -> Timber.i("deployed date updated"), Timber::e));
                        }
                    }
                }, Timber::e));
    }*/

    private void onUserMasterDataResponse(UserMasterDataResponseV2 response) {
        if (response != null && response.userMasterData != null) {

            UserMasterDataResponseV2.UserMasterDataV2 data = response.userMasterData;

            //employeeLeaves
            if (data.employeeLeaves != null && data.employeeLeaves.size() != 0) {
                addDisposable(employeeLeaveDao.deleteEmployeeLeave()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
//                            Timber.i("employeeLeaves deleted");
                            addDisposable(employeeLeaveDao
                                    .insertAllEmployeeLeave(data.employeeLeaves)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {}, Timber::e));
                        }, Timber::e));
            }

            if (data.sites != null && data.sites.size() != 0) {
                addDisposable(siteDao.deleteSite()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            addDisposable(siteDao
                                    .insertAllSites(data.sites)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> Timber.i("sites inserted"), Timber::e));
                        }, Timber::e));

                addDisposable(siteExtensionDao.deleteSiteExtension()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("siteExtension deleted");
                            for (SiteEntity site : data.sites) {
                                if (site.siteExtension != null) {
                                    site.siteExtension.siteId = site.id;
                                    addDisposable(siteExtensionDao
                                            .insertSiteExtension(site.siteExtension)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(row -> Timber.i("siteExtension inserted"), Timber::e));
                                }
                            }
                        }));

                addDisposable(siteCheckListDao.deleteSiteCheckList()
                        .mergeWith(siteCheckListDao.deleteSiteCheckListOption())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                            Timber.i("siteChecklists deleted");
                            for (SiteEntity site : data.sites) {
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

            if (data.barracks != null && data.barracks.size() != 0) {
                addDisposable(barrackDao.deleteBarrack()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("Barrack deleted");
                            addDisposable(barrackDao
                                    .insertAllBarrack(data.barracks)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> Timber.i("Barrack inserted"), Timber::e));
                        }, Timber::e));
            }

            if (data.billCollections != null && data.billCollections.size() != 0) {
                addDisposable(billCollectionDao.deleteBillCollections()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("billCollections deleted");
                            addDisposable(billCollectionDao
                                    .insertAllBillCollections(data.billCollections)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            //customer
            if (data.customers != null && data.customers.size() != 0) {
                addDisposable(customerDao.deleteCustomer()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->
                                        addDisposable(customerDao.insertAllCustomer(data.customers)
                                                .subscribeOn(Schedulers.computation())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(rows -> Timber.e("CustomersDetail Inserted"), Timber::e)),
                                Timber::e));
            }

            if (data.customerContacts != null && data.customerContacts.size() != 0) {
                addDisposable(customerContactDao.deleteCustomerContact()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            addDisposable(customerContactDao.insertAllCustomerContact(data.customerContacts)
                                    .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }));
            }

            if (data.employeeSites != null && data.employeeSites.size() != 0) {
                addDisposable(employeeSiteDao.deleteEmployeeSite()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            addDisposable(employeeSiteDao.insertAllEmployeeSite(data.employeeSites)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (data.siteStrengths != null && data.siteStrengths.size() != 0) {
                addDisposable(strengthDao.deleteSiteStrength()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("siteStrengths deleted");
                            addDisposable(strengthDao.insertAllSiteStrength(data.siteStrengths)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                        Timber.i("siteStrengths inserted");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (data.siteShifts != null && data.siteShifts.size() != 0) {
                addDisposable(siteShiftDao.deleteSiteShift()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("siteShifts deleted");
                            addDisposable(siteShiftDao.insertAllSiteShift(data.siteShifts)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (data.siteBarrackMaps != null && data.siteBarrackMaps.size() != 0) {
                addDisposable(barrackDao.deleteSiteBarrackMaps().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                            Timber.i("siteBarrackMaps deleted");
                            addDisposable(barrackDao.insertAllSiteBarrackMaps(data.siteBarrackMaps)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (data.customerSiteContacts != null && data.customerSiteContacts.size() != 0) {
                addDisposable(customerSiteContactDao.deleteCustomerSiteContact().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            addDisposable(customerSiteContactDao
                                    .insertAllCustomerSiteContact(data.customerSiteContacts)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (data.siteRisks != null && data.siteRisks.size() != 0) {
                addDisposable(siteAtRiskDao.deleteSiteAtRisk().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("siteRisks deleted");
                            addDisposable(siteAtRiskDao
                                    .insertAllSiteAtRisk(data.siteRisks)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));

                addDisposable(siteRiskPoaDao.deleteSiteRiskPoa()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("siteRiskPos deleted");
                            for (SiteAtRiskEntity risk : data.siteRisks) {
                                if (risk != null && risk.siteRiskPos != null && risk.siteRiskPos.size() != 0) {
                                    addDisposable(siteRiskPoaDao.insertAllAtRiskPOA(risk.siteRiskPos)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(rows -> Timber.i("siteRiskPos inserted"), Timber::e));
                                }
                            }
                        }, Timber::e));

                addDisposable(siteRiskPoaDao.deleteSiteRiskReasons()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("SiteRiskReasons deleted");
                            for (SiteAtRiskEntity risk : data.siteRisks) {
                                if (risk != null && risk.siteRiskReasons != null && !risk.siteRiskReasons.isEmpty()) {
                                    addDisposable(siteRiskPoaDao.insertAllAtRiskReasons(risk.siteRiskReasons)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(rows -> Timber.i("siteRiskPos inserted"),
                                                    Timber::e));
                                }
                            }
                        }, Timber::e));
            }

            if (data.siteBillingChecklists != null && data.siteBillingChecklists.size() != 0) {
                addDisposable(siteBillingCheckListDao.deleteSiteBillingCheckList()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            addDisposable(siteBillingCheckListDao.insertAllSiteBillingCheckList(data.siteBillingChecklists)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (data.sitePosts != null && data.sitePosts.size() != 0) {
                addDisposable(sitePostDao.deleteSitePost()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            for (SitePostEntity postEntity : data.sitePosts) {
                                if (postEntity.isActive) {
                                    addDisposable(sitePostDao.insert(postEntity)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(rows -> {
                                            }, Timber::e));
                                }
                            }
                            /*addDisposable(sitePostDao.insertAllSitePost(data.sitePosts)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));*/

                        }, Timber::e));

                addDisposable(postRegistersDao.deletePostRegister()
                        .mergeWith(postCheckListDao.deletePostCheckList())
                        .mergeWith(postCheckListDao.deletePostCheckListOption())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            for (SitePostEntity post : data.sitePosts) {
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
                                                        if (pc.postChecklistOptions != null) {
                                                            for (PostCheckListOptionEntity pco : pc.postChecklistOptions) {
                                                                pco.siteId = post.siteId;
                                                                pco.postId = post.id;
                                                                pco.postChecklistId = pc.postChecklistId;
                                                                addDisposable(postCheckListDao.insertPostCheckListOption(pco)
                                                                        .subscribeOn(Schedulers.io())
                                                                        .observeOn(AndroidSchedulers.mainThread())
                                                                        .subscribe(pcoRow -> {
                                                                        }, Timber::e));
                                                            }
                                                        }
                                                    }, Timber::e));
                                        }
                                    }
                                }
                            }
                        }, Timber::e));
            }

            if (data.contracts != null && data.contracts.size() != 0) {
                addDisposable(contractsDao.deleteContracts()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            addDisposable(contractsDao.insertAllContracts(data.contracts)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (data.barrackStrengths != null && data.barrackStrengths.size() != 0) {
                addDisposable(barrackStrengthDao.deleteBarrackStrength()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            addDisposable(barrackStrengthDao.insertAllBarrackStrength(data.barrackStrengths)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(longs -> {
                                    }, Timber::e));
                        }, Timber::e));
            }

            //#AR : Deleting and Inserting data w.r.t Kit Distribution List and Items
            /*if (data.kitDistributions != null && data.kitDistributions.size() != 0) {
                addDisposable(kitItemDao.deleteKitDistributionList()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("Kit Distribution List deleted");
                            addDisposable(kitItemDao.insertAllKitDistributionList(data.kitDistributions)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
                                        Timber.i("Kit Distribution List inserted");
                                    }, Timber::e));
                        }, Timber::e));

                addDisposable(kitItemDao.deleteKitDistributionItems()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("Kit Distribution ITEMS deleted");
                            for (KitDistributionListEntity listMO : data.kitDistributions) {
                                if (listMO.kitDistributionItems != null && listMO.kitDistributionItems.size() > 0) {
                                    addDisposable(kitItemDao.insertAllKitDistributionItems(listMO.kitDistributionItems)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(rows -> {
                                                Timber.i("Kit Distribution ITEMS inserted");
                                            }, Timber::e));
                                }
                            }
                        }, Timber::e));
            }*/
        }
    }
}
