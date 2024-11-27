package com.sisindia.ai.android.features.conveyance

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowConveyanceBinding
import com.sisindia.ai.android.models.performance.ConveyanceReportDataMO

/**
 * Created by Ashu Rajput on 12/30/2020.
 */
class MonthlyConveyanceAdapter : BaseRecyclerAdapter<ConveyanceReportDataMO>() {
    private lateinit var listener: ConveyanceListener

    fun initListener(listener: ConveyanceListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowConveyanceBinding = getViewDataBinding(R.layout.row_conveyance, parent) as RowConveyanceBinding
        view.executePendingBindings()
        return ConveyanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ConveyanceViewHolder = holder as ConveyanceViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class ConveyanceViewHolder(val view: RowConveyanceBinding) : BaseViewHolder<ConveyanceReportDataMO>(view) {
        override fun onBind(item: ConveyanceReportDataMO) {
            view.model = item
            view.root.setOnClickListener {
                items[layoutPosition].taskDate?.let { date -> listener.onConveyanceSelected(date) }
            }
        }
    }
}