package com.sisindia.ai.android.features.rotacompliance.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sisindia.ai.android.features.rotacompliance.ComplianceDayWeekMonthFragment

/**
 * Created by Ashu Rajput on 3/6/2020.
 */
class RotaCompliancePagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ComplianceDayWeekMonthFragment()
            1 -> fragment = ComplianceDayWeekMonthFragment()
            2 -> fragment = ComplianceDayWeekMonthFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 3
    }
}