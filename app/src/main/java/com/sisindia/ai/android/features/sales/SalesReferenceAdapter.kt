package com.sisindia.ai.android.features.sales

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowSalesReferenceBinding
import com.sisindia.ai.android.models.sales.SalesReferenceMO

/**
 * Created by Ashu Rajput on 4/30/2021.
 */
class SalesReferenceAdapter : BaseRecyclerAdapter<SalesReferenceMO>() {

    private lateinit var listener: SalesListener

    fun initListener(listener: SalesListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowSalesReferenceBinding = getViewDataBinding(R.layout.row_sales_reference, parent)
                as RowSalesReferenceBinding
        view.executePendingBindings()
        return SalesRefViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: SalesRefViewHolder = holder as SalesRefViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class SalesRefViewHolder(val view: RowSalesReferenceBinding) : BaseViewHolder<SalesReferenceMO>(view) {
        override fun onBind(item: SalesReferenceMO) {
            view.model = item
            view.root.setOnClickListener {
                listener.onItemSelected(item)
            }
        }
    }
}