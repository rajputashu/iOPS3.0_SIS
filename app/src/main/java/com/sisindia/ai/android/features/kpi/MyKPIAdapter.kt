package com.sisindia.ai.android.features.kpi

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowMyKpiBinding

/**
 * Created by Ashu Rajput on 9/22/2020.
 */
class MyKPIAdapter : BaseRecyclerAdapter<MyKpiMO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowMyKpiBinding = getViewDataBinding(R.layout.row_my_kpi, parent) as RowMyKpiBinding
        view.executePendingBindings()
        return MyKPIViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: MyKPIViewHolder = holder as MyKPIViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class MyKPIViewHolder(val view: RowMyKpiBinding) :
        BaseViewHolder<MyKpiMO>(view) {
        override fun onBind(item: MyKpiMO) {
            view.model = item
        }
    }
}