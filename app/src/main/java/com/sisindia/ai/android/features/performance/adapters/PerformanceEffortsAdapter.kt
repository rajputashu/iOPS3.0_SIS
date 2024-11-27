package com.sisindia.ai.android.features.performance.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowPerformanceEffortsBinding
import com.sisindia.ai.android.features.performance.PerformanceListener
import com.sisindia.ai.android.models.performance.PerformanceEffortsMO

/**
 * Created by Ashu Rajput on 5/8/2020.
 */
class PerformanceEffortsAdapter : BaseRecyclerAdapter<PerformanceEffortsMO>() {

    private lateinit var listener: PerformanceListener

    fun initListener(listener: PerformanceListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowPerformanceEffortsBinding = getViewDataBinding(
            R.layout.row_performance_efforts, parent
        ) as RowPerformanceEffortsBinding
        view.executePendingBindings()
        return PerformanceEffortsVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: PerformanceEffortsVH = holder as PerformanceEffortsVH
        viewHolder.onBind(getItem(position))
    }

    inner class PerformanceEffortsVH(val view: RowPerformanceEffortsBinding) :
        BaseViewHolder<PerformanceEffortsMO>(view) {
        override fun onBind(item: PerformanceEffortsMO) {
            view.model = item
        }
    }
}