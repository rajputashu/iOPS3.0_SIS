package com.sisindia.ai.android.features.loadfactor

import android.app.Application
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.RCViewType
import com.sisindia.ai.android.features.rotacompliance.entities.ComplianceTaskDetailsMO
import com.sisindia.ai.android.features.rotacompliance.entities.DayWeekMonthMO
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/11/2020.
 */
class LoadFactorDWMViewModel @Inject constructor(application: Application) :
    IopsBaseViewModel(application) {

    fun getData(type: Int): ArrayList<DayWeekMonthMO> {
        val list = ArrayList<DayWeekMonthMO>()
        val complianceTaskList = ArrayList<ComplianceTaskDetailsMO>()
        when (type) {
            RCViewType.DAY.viewType -> {
                complianceTaskList.add(ComplianceTaskDetailsMO("DC", 42f, 100f, 60f))
                complianceTaskList.add(ComplianceTaskDetailsMO("NC", 20f, 100f, 40f))
                complianceTaskList.add(ComplianceTaskDetailsMO("CC", 42f, 100f, 60f))
                complianceTaskList.add(ComplianceTaskDetailsMO("BS", 55f, 100f, 85f))
                list.add(DayWeekMonthMO("Yesterday", "", complianceTaskList, type))
                list.add(DayWeekMonthMO("Today", "", complianceTaskList, type))
            }
            RCViewType.WEEKLY.viewType -> {
                complianceTaskList.add(ComplianceTaskDetailsMO("DC", 42f, 100f, 60f))
                complianceTaskList.add(ComplianceTaskDetailsMO("NC", 20f, 100f, 40f))
                complianceTaskList.add(ComplianceTaskDetailsMO("CC", 42f, 100f, 60f))
                complianceTaskList.add(ComplianceTaskDetailsMO("BS", 55f, 100f, 85f))
                list.add(DayWeekMonthMO("02 JUl / 08 JUL",
                    "02%", complianceTaskList, type))
                list.add(DayWeekMonthMO("09 JUL / 15 JUL",
                    "02%", complianceTaskList, type))
            }
            RCViewType.MONTHLY.viewType -> {
                complianceTaskList.add(ComplianceTaskDetailsMO("DC", 42f, 100f, 60f))
                complianceTaskList.add(ComplianceTaskDetailsMO("NC", 20f, 100f, 40f))
                complianceTaskList.add(ComplianceTaskDetailsMO("CC", 42f, 100f, 60f))
                complianceTaskList.add(ComplianceTaskDetailsMO("BS", 55f, 100f, 85f))
                list.add(DayWeekMonthMO("JUNE", "02%", complianceTaskList, type))
                list.add(DayWeekMonthMO("JULY", "02%", complianceTaskList, type))
            }
        }
        return list
    }
}