package com.sisindia.ai.android.features.billcollection.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowBillCollectionAtsiteBinding
import com.sisindia.ai.android.features.billcollection.BillCollectionListener
import com.sisindia.ai.android.uimodels.collection.DueBillCollectionMO

/**
 * Created by Ashu Rajput on 5/21/2020.
 */
class BillCollectionAtSiteAdapter : BaseRecyclerAdapter<DueBillCollectionMO>() {

    private lateinit var listener: BillCollectionListener

    fun initListener(listener: BillCollectionListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowBillCollectionAtsiteBinding =
            getViewDataBinding(R.layout.row_bill_collection_atsite, parent)
                    as RowBillCollectionAtsiteBinding
        view.executePendingBindings()
        return CollectionAtSiteVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: CollectionAtSiteVH = holder as CollectionAtSiteVH
        viewHolder.onBind(getItem(position))
    }

    inner class CollectionAtSiteVH(val view: RowBillCollectionAtsiteBinding) :
        BaseViewHolder<DueBillCollectionMO>(view) {
        override fun onBind(item: DueBillCollectionMO) {
//            view.model = item
//            view.root.setOnClickListener { listener.onBillSelected(item) }
        }
    }
}