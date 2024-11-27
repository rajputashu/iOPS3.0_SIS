package com.sisindia.ai.android.features.performance.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowSiteTaskSummaryBinding
import com.sisindia.ai.android.models.site.TaskSummaryDataMO

/**
 * Created by Ashu Rajput on 11/18/2020.
 */
class SiteTaskSummaryAdapter : BaseRecyclerAdapter<TaskSummaryDataMO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowSiteTaskSummaryBinding = getViewDataBinding(R.layout.row_site_task_summary, parent) as RowSiteTaskSummaryBinding
        view.executePendingBindings()
        return SiteTaskSummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: SiteTaskSummaryViewHolder = holder as SiteTaskSummaryViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class SiteTaskSummaryViewHolder(val binder: RowSiteTaskSummaryBinding) :
        BaseViewHolder<TaskSummaryDataMO>(binder) {
        override fun onBind(item: TaskSummaryDataMO) {
            binder.srNo = layoutPosition + 1
            binder.model = item
        }
    }
}