package com.sisindia.ai.android.workers;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.CommonMasterDataResponse;
import com.sisindia.ai.android.room.dao.ActionPlanDao;
import com.sisindia.ai.android.room.dao.AttachmentMetadataDefinitionDao;
import com.sisindia.ai.android.room.dao.BranchDao;
import com.sisindia.ai.android.room.dao.ClientHandShakeQuestionDao;
import com.sisindia.ai.android.room.dao.CompanyDao;
import com.sisindia.ai.android.room.dao.CountryDao;
import com.sisindia.ai.android.room.dao.DeploymentTypeDao;
import com.sisindia.ai.android.room.dao.HolidayDao;
import com.sisindia.ai.android.room.dao.IndustrySectorDao;
import com.sisindia.ai.android.room.dao.KitItemDao;
import com.sisindia.ai.android.room.dao.LanguageDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.dao.OrganizationDao;
import com.sisindia.ai.android.room.dao.RankDao;
import com.sisindia.ai.android.room.dao.RegionDao;
import com.sisindia.ai.android.room.dao.SiteTypeDao;
import com.sisindia.ai.android.room.dao.TaskTypeDao;
import com.sisindia.ai.android.room.entities.ActionPlanEntity;
import com.sisindia.ai.android.room.entities.ClientHandShakeQuestionEntity;
import com.sisindia.ai.android.room.entities.DynamicFormEntity;
import com.sisindia.ai.android.room.entities.KitItemEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CommonMasterDataWorker extends BaseWorker {

    @Inject
    public ActionPlanDao actionPlanDao;

    @Inject
    public BranchDao branchDao;

    @Inject
    public ClientHandShakeQuestionDao clientHandShakeQuestionDao;

    @Inject
    public CompanyDao companyDao;

    @Inject
    public CountryDao countryDao;

    @Inject
    public DeploymentTypeDao deploymentTypeDao;

    @Inject
    public HolidayDao holidayDao;

    @Inject
    public IndustrySectorDao industrySectorDao;

    @Inject
    public KitItemDao kitItemDao;

    @Inject
    public LanguageDao languageDao;

    @Inject
    public LookUpDao lookUpDao;

    @Inject
    public OrganizationDao organizationDao;

    @Inject
    public AttachmentMetadataDefinitionDao metadataDefinitionDao;

    @Inject
    public RankDao rankDao;

    @Inject
    public RegionDao regionDao;

    @Inject
    public SiteTypeDao siteTypeDao;

    @Inject
    public TaskTypeDao taskTypeDao;


    @Inject
    public CommonMasterDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        Timber.i("Common Master Data Called");
        addDisposable(mastersApi.getCommonMasterData().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Prefs.putBoolean(PrefConstants.COMMON_MASTER_DATA_SYNC, true);
                    onCommonMasterDataResponse(response);
                }, throwable -> Prefs.putBoolean(PrefConstants.COMMON_MASTER_DATA_SYNC, false)));
        return Result.success();
    }

    private void onCommonMasterDataResponse(CommonMasterDataResponse response) {
        if (response != null && response.commonMasterData != null) {
            CommonMasterDataResponse.CommonMasterData data = response.commonMasterData;

            //Action Plan
            if (data.actionPlans != null && data.actionPlans.size() != 0) {
                addDisposable(actionPlanDao.deleteActionPlan()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("Action Plans deleted...");
                            addDisposable(actionPlanDao.insertAllActionPlan(data.actionPlans)
                                    .compose(transformSingle()).subscribe((apRows, throwable) -> {
                                        if (throwable != null) {
                                            Timber.e(throwable);
                                        }
                                    }));
                        }, Timber::e));
            }

            if (data.actionPlans != null && data.actionPlans.size() != 0) {
                addDisposable(actionPlanDao.deleteActionPlanMap()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("Action Plan Maps deleted...");
                            for (ActionPlanEntity ap : data.actionPlans) {

                                if (ap.actionPlanMaps != null) {

                                    addDisposable(actionPlanDao
                                            .insertActionPlanMaps(ap.actionPlanMaps)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe((apmRows, throwable1) -> {

                                                if (throwable1 != null) {
                                                    Timber.e(throwable1);
                                                }
//                                                Timber.i("Action Plan Map inserted... %s", apmRows);

                                            }));
                                }
                            }
                        }, Timber::e));
            }

            //branches
            if (data.branches != null && data.branches.size() != 0) {
                addDisposable(branchDao.deleteBranch()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("branches deleted...");

                            addDisposable(branchDao
                                    .insertAllBranch(response.commonMasterData.branches)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(brRows -> {
//                                        Timber.i("branches inserted... %s", brRows);
                                        //currentBranch
                                        if (data.currentBranch != null) {
                                            data.currentBranch.isCurrentBranch = true;
                                            addDisposable(branchDao
                                                    .insert(data.currentBranch)
                                                    .subscribeOn(Schedulers.computation())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(brRow -> {
                                                    }, Timber::e));
                                        }
                                    }, Timber::e));
                        }, Timber::e));

            }

            //clientHandshakeQuestions
            if (response.commonMasterData.clientHandshakeQuestions != null) {
                addDisposable(clientHandShakeQuestionDao.deleteClientHandShakeQuestion()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("clientHandshakeQuestions deleted...");
                            addDisposable(clientHandShakeQuestionDao
                                    .insertAllClientHandShakeQuestion(response.commonMasterData.clientHandshakeQuestions)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(chRows -> {
//                                        Timber.i("clientHandshakeQuestions inserted... %s", chRows);
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.clientHandshakeQuestions != null) {
                addDisposable(clientHandShakeQuestionDao.deleteClientHandshakeRatingMaps()
                        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("clientHandshakeRatingMaps deleted...");
                            for (ClientHandShakeQuestionEntity model : response.commonMasterData.clientHandshakeQuestions) {
                                if (model.clientHandshakeRatingMaps != null) {
                                    addDisposable(clientHandShakeQuestionDao
                                            .insertAllQuesRatingMap(model.clientHandshakeRatingMaps)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(ratingRows -> {
//                                                Timber.i("clientHandshakeRatingMaps inserted... %s", ratingRows);
                                            }, Timber::e));
                                }
                            }
                        }, Timber::e));
            }

            //Company
            if (response.commonMasterData.company != null)
                addDisposable(companyDao.deleteCompany()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> addDisposable(companyDao
                                .insertCompany(response.commonMasterData.company)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(comRow -> {
                                }, Timber::e)), Timber::e));

            //countries
            if (response.commonMasterData.countries != null && response.commonMasterData.countries.size() != 0) {
                addDisposable(countryDao.deleteCountry()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("countries deleted...");
                            addDisposable(countryDao.insertAllCountry(response.commonMasterData.countries)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(counRows -> {
//                                        Timber.i("countries inserted... %s", counRows);
                                    }, Timber::e));
                        }, Timber::e));
            }

            //deploymentTypes
            if (response.commonMasterData.deploymentTypes != null && response.commonMasterData.deploymentTypes.size() != 0) {
                addDisposable(deploymentTypeDao.deleteDeploymentType()
                        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            //deploymentTypes
                            addDisposable(deploymentTypeDao
                                    .insertAllDeploymentType(response.commonMasterData.deploymentTypes)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("Deployment Types inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            //holidayDao
            if (response.commonMasterData.holidays != null && response.commonMasterData.holidays.size() != 0) {
                addDisposable(holidayDao.deleteHoliday().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("holidays deleted...");
                            addDisposable(holidayDao
                                    .insertAllHoliday(response.commonMasterData.holidays)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("holidays  inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.industrySectors != null && response.commonMasterData.industrySectors.size() != 0) {
                addDisposable(industrySectorDao.deleteIndustrySector()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> addDisposable(industrySectorDao
                                .insertAllIndustrySector(response.commonMasterData.industrySectors)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(rows -> {
//                                        Timber.i("industrySectors  inserted...");
                                }, Timber::e)), Timber::e));
            }

            if (response.commonMasterData.kitItems != null && response.commonMasterData.kitItems.size() != 0) {
                addDisposable(kitItemDao.deleteKitItem()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("kitItems deleted...");
                            //kitItems
                            addDisposable(kitItemDao
                                    .insertAllKitItems(response.commonMasterData.kitItems)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("kitItems inserted..." + rows);
                                    }, Timber::e));

                        }, Timber::e));
            }

            if (response.commonMasterData.kitItems != null && response.commonMasterData.kitItems.size() != 0) {
                List<KitItemEntity> kitItems = response.commonMasterData.kitItems;
                addDisposable(kitItemDao.deleteKitItemSize().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("kitItemSizes deleted...");
                            for (KitItemEntity item : kitItems) {
                                if (item != null && item.kitItemSizes != null && item.kitItemSizes.size() != 0) {
                                    addDisposable(kitItemDao
                                            .insertAllKitItemSizes(item.kitItemSizes)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(sizes -> {
//                                                Timber.i("kitItemSizes inserted..." + sizes);
                                            }, Timber::e));
                                }
                            }
                        }, Timber::e));
            }

            if (response.commonMasterData.languages != null && response.commonMasterData.languages.size() != 0) {
                addDisposable(languageDao.deleteLanguage()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("languages deleted...");
                            addDisposable(languageDao
                                    .insertAllLanguage(response.commonMasterData.languages)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("languages inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.lookups != null && response.commonMasterData.lookups.size() != 0) {
                addDisposable(lookUpDao.deleteLookUp().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("lookups deleted...");
                            //lookups
                            addDisposable(lookUpDao
                                    .insertAllLookUp(response.commonMasterData.lookups)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("lookups inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.metadataDefinition != null && response.commonMasterData.metadataDefinition.size() != 0) {
                addDisposable(metadataDefinitionDao.deleteAttachmentMetadataDefinition().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("metadataDefinition deleted...");
                            //ATTACHMENT METADATA DEFINITIONS
                            addDisposable(metadataDefinitionDao
                                    .insertAllMetadataDefinitions(response.commonMasterData.metadataDefinition)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("metadataDefinition inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.organization != null) {
                addDisposable(organizationDao.deleteOrganization().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("organization deleted...");
                            addDisposable(organizationDao
                                    .insertOrganization(response.commonMasterData.organization)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(row -> {
//                                        Timber.i("organization inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.ranks != null && response.commonMasterData.ranks.size() != 0) {
                addDisposable(rankDao.deleteRank().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("ranks deleted...");
                            addDisposable(rankDao
                                    .insertAllRanks(response.commonMasterData.ranks)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("ranks inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.regions != null && response.commonMasterData.regions.size() != 0) {
                addDisposable(regionDao.deleteRegion().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("regions deleted...");
                            addDisposable(regionDao
                                    .insertAllRegion(response.commonMasterData.regions)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("regions inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.siteTypes != null && response.commonMasterData.siteTypes.size() != 0) {
                addDisposable(siteTypeDao.deleteSiteType().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("siteTypes deleted...");
                            addDisposable(siteTypeDao
                                    .insertAllSiteType(response.commonMasterData.siteTypes)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("siteTypes inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            if (response.commonMasterData.taskTypes != null && response.commonMasterData.taskTypes.size() != 0) {
                addDisposable(taskTypeDao.deleteTaskType().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Timber.i("taskTypes deleted...");
                            addDisposable(taskTypeDao
                                    .insertAllTaskTypes(response.commonMasterData.taskTypes)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(rows -> {
//                                        Timber.i("taskTypes inserted...");
                                    }, Timber::e));
                        }, Timber::e));
            }

            //INSERTING DYNAMIC FORM AGAINST TASK TYPE
            if (response.commonMasterData.taskTypes != null && response.commonMasterData.taskTypes.size() != 0) {
                addDisposable(taskTypeDao.deleteDynamicForm()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            ArrayList<DynamicFormEntity> formList = new ArrayList<>();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                response.commonMasterData.taskTypes.forEach(taskMO -> {
                                    if (taskMO.taskTypeConfig != null)
                                        formList.add(new DynamicFormEntity(taskMO.id, taskMO.taskTypeConfig));
                                });

                                if (!formList.isEmpty()) {
                                    addDisposable(taskTypeDao.insertAllDynamicForms(formList)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(rows -> {
                                            }, Timber::e));
                                }
                            }
                        }, Timber::e));
            }

        } else {
            Prefs.putBoolean(PrefConstants.COMMON_MASTER_DATA_SYNC, false);
        }
    }
}
