package com.sisindia.ai.android.features.billcollection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowBillCollectionBinding
import com.sisindia.ai.android.room.entities.BillCollectionsEntity

/**
 * Created by Ashu Rajput on 3/13/2020.
 */
class BillCollectionAdapter : BaseRecyclerAdapter<BillCollectionsEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BCViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: RowBillCollectionBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_bill_collection, parent, false)
        return BCViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder: BCViewHolder = holder as BCViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class BCViewHolder(private val binder: RowBillCollectionBinding) :
        BaseViewHolder<BillCollectionsEntity>(binder) {
        override fun onBind(item: BillCollectionsEntity) {
            binder.billCollectionMO = item
            binder.bsCheckBox.setOnClickListener {
                items[layoutPosition].isBillSelected = !items[layoutPosition].isBillSelected
            }
        }
    }
}