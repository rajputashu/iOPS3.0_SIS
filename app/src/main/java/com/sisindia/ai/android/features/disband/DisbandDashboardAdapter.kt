package com.sisindia.ai.android.features.disband

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.databinding.RowDisbandDashboardBinding

/**
 * Created by Ashu Rajput on 03-04-2023
 */
class DisbandDashboardAdapter : BaseRecyclerAdapter<DashboardDisbandmentSitesData>() {

    private lateinit var context: Context
    /*private lateinit var listener: DisbandDashboardListener
    fun initListener(listener: DisbandDashboardListener) {
        this.listener = listener
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view: RowDisbandDashboardBinding =
            getViewDataBinding(R.layout.row_disband_dashboard, parent) as RowDisbandDashboardBinding
        view.executePendingBindings()
        return DisbandDashboardVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: DisbandDashboardVH = holder as DisbandDashboardVH
        viewHolder.onBind(getItem(position))
    }

    inner class DisbandDashboardVH(val view: RowDisbandDashboardBinding) :
        BaseViewHolder<DashboardDisbandmentSitesData>(view) {
        override fun onBind(item: DashboardDisbandmentSitesData) {
            view.model = item
            view.cvDisbandSites.setOnClickListener {
                Prefs.putInt(PrefConstants.CURRENT_SITE, item.siteId!!)
//                listener.disbandSiteCardSelected(layoutPosition)
            }
        }
    }
}