package com.sisindia.ai.android.features.performance.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowPerformanceResultsBinding
import com.sisindia.ai.android.features.performance.PerformanceListener
import com.sisindia.ai.android.models.performance.PerformanceResultsMO

/**
 * Created by Ashu Rajput on 5/8/2020.
 */
class PerformanceResultsAdapter : BaseRecyclerAdapter<PerformanceResultsMO>() {

    private lateinit var listener: PerformanceListener

    fun initListener(listener: PerformanceListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowPerformanceResultsBinding =
            getViewDataBinding(R.layout.row_performance_results,
                parent) as RowPerformanceResultsBinding
        view.executePendingBindings()
        return PerformanceResultsVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: PerformanceResultsVH = holder as PerformanceResultsVH
        viewHolder.onBind(getItem(position))
    }

    inner class PerformanceResultsVH(val view: RowPerformanceResultsBinding) :
        BaseViewHolder<PerformanceResultsMO>(view) {
        override fun onBind(item: PerformanceResultsMO) {
            view.model = item
            view.root.setOnClickListener {
                listener.onResultsItemSelected(layoutPosition)
            }
        }
    }
}