package com.sisindia.ai.android.features.performance.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowIncentiveBinding
import com.sisindia.ai.android.models.performance.IncentiveDataMO
import com.sisindia.ai.android.models.performance.IncentiveRowData

/**
 * Created by Ashu Rajput on 17-01-2022
 */
class IncentiveAdapter : BaseRecyclerAdapter<IncentiveRowData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowIncentiveBinding =
            getViewDataBinding(R.layout.row_incentive, parent) as RowIncentiveBinding
        view.executePendingBindings()
        return IncentiveViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: IncentiveViewHolder = holder as IncentiveViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class IncentiveViewHolder(val binder: RowIncentiveBinding) :
        BaseViewHolder<IncentiveRowData>(binder) {
        override fun onBind(item: IncentiveRowData) {
            binder.model = item
        }
    }
}