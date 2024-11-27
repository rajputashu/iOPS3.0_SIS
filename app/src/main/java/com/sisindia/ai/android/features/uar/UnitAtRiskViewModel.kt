package com.sisindia.ai.android.features.uar

import android.app.Application
import android.text.Spanned
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.room.customdao.UarAndPoaDao
import com.sisindia.ai.android.room.dao.SiteAtRiskDao
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao
import com.sisindia.ai.android.uimodels.uarpoa.AtRiskAndPoaDetailsMO
import com.sisindia.ai.android.utils.TimeUtils
import com.sisindia.ai.android.utils.toHtmlSpan
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
class UnitAtRiskViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var dao: UarAndPoaDao

    @Inject
    lateinit var siteRiskPoaDao: SiteRiskPoaDao

    @Inject
    lateinit var siteAtRiskDao: SiteAtRiskDao

    var unitAtRiskCount = ObservableField(getUpdatedUARCount("00 Units"))
    var planOfActionCount = ObservableField(getUpdatedPOACount("00/00"))
    var labelUARCount = ObservableField(getUpdatedUARLabelCount("00"))
    val uarAdapter = UARAdapter()
    val isAtRiskDataAvailable = ObservableBoolean(false)

    val uarListener = object : UARListener {
        override fun onUnitAtRiskSelected(selectedUAR: Any) {
            message.what = NavigationConstants.OPEN_POA_SCREEN
            message.obj = selectedUAR
            liveData.postValue(message)
        }

        override fun onPendingPOASelected(selectedPOA: Any) {
        }
    }

    private fun getUpdatedUARCount(uarCount: String): Spanned {
        return app.getString(R.string.dynamic_string, uarCount,
            app.getString(R.string.string_at_risk)).toHtmlSpan()
    }

    private fun getUpdatedPOACount(poaCount: String): Spanned {
        return app.getString(R.string.dynamic_string, poaCount,
            app.getString(R.string.string_poas)).toHtmlSpan()
    }

    private fun getUpdatedUARLabelCount(uarCount: String): String {
        return app.getString(R.string.dynamic_string_uar_count, uarCount)
    }

    fun onClickDelegate(view: View) {
        when (view.id) {
            R.id.headerUAR -> {
                message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
                liveData.postValue(message)
            }
        }
    }

    fun getHeaderUnitsAndPOAsCount() {
        setIsLoading(true)
        addDisposable(dao.fetchSiteAndPoaCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model ->
                setIsLoading(false)
                unitAtRiskCount.set(getUpdatedUARCount("${model.TotalSite} Units"))
                planOfActionCount.set(getUpdatedPOACount("${model.TotalCompletePoAs}/${model.TotalPOAs}"))
                labelUARCount.set(getUpdatedUARLabelCount("${model.TotalSite}"))
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    lateinit var dueDatesList: List<String>
    fun getDueDatesFromDB() {
        addDisposable(dao.fetchAllDueDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dueDatesMO ->
                if (dueDatesMO.isNotEmpty()) {
                    this.dueDatesList = dueDatesMO
                    getUARAndPOAsDetails()
                }
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun getUARAndPOAsDetails() {
        setIsLoading(true)
        addDisposable(dao.fetchUARDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ modelList ->
                setIsLoading(false)
                onSuccessFetchDetails(modelList)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun onSuccessFetchDetails(detailList: List<AtRiskAndPoaDetailsMO>) {
        if (::dueDatesList.isInitialized) {
            val finalList = ArrayList<Any>()
            for (date in dueDatesList) {
                finalList.add(date)
                for (detailModel in detailList) {
                    if (date == detailModel.targetCompletionDate) {
                        finalList.add(detailModel)
                    }
                }
            }
            //Finally Checking if List contains any data or not
            if (finalList.size > 1) {
                isAtRiskDataAvailable.set(true)
                uarAdapter.updateUARData(finalList)
            } else
                isAtRiskDataAvailable.set(false)
        }
    }

    private fun onError(throwable: Throwable) {
        setIsLoading(false)
        Timber.e(throwable)
        showToast("Error while fetching details")
    }

    fun onPOAsSyncClick() {
        setIsLoading(true)
        addDisposable(coreApi.getMonthlyAtRiskPOAs(TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now())).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ atRiskResponse ->
                if (atRiskResponse.statusCode == 200 && !atRiskResponse.siteRisks.isNullOrEmpty()) {
                    addDisposable(siteAtRiskDao.deleteSiteAtRisk().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            addDisposable(siteAtRiskDao
                                .insertAllSiteAtRisk(atRiskResponse.siteRisks)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ rows: List<Long?>? ->
                                    Timber.i("siteRisks inserted")
                                }, { t: Throwable? -> Timber.e(t) }))
                        }, { t: Throwable? -> Timber.e(t) }))

                    addDisposable(siteRiskPoaDao.deleteSiteRiskPoa()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            for (risk in atRiskResponse.siteRisks) {
                                if (risk.siteRiskPos != null && risk.siteRiskPos.size != 0) {
                                    addDisposable(siteRiskPoaDao.insertAllAtRiskPOA(risk.siteRiskPos)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ rows: List<Long?>? ->
                                            Timber.i("siteRiskPos inserted")
                                        }, { t: Throwable? -> Timber.e(t) }))
                                }
                            }
                        }, { t: Throwable? -> Timber.e(t) }))

                    //Updating UI after {syncing data from API and operating on DB}
                    updateUIAfterSync()
                } else {
                    deleteAtRiskAndPoaDataFromTable()
                }
            }) { throwable: Throwable? ->
                setIsLoading(false)
                onApiError(throwable)
            })
    }

    private fun deleteAtRiskAndPoaDataFromTable() {
        addDisposable(Single.zip(siteAtRiskDao.deleteSiteAtRisk().toSingleDefault(""),
            siteRiskPoaDao.deleteSiteRiskPoa().toSingleDefault(""),
            BiFunction<String, String, Boolean> { _, _ ->
                return@BiFunction true
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setIsLoading(false)
            }, {
                it.printStackTrace()
            }))
    }

    private fun updateUIAfterSync() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1500)
            withContext(Dispatchers.Main) {
                setIsLoading(false)
                getHeaderUnitsAndPOAsCount()
                getDueDatesFromDB()
            }
        }
    }
}