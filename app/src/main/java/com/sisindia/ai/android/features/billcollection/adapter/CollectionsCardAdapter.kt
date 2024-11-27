package com.sisindia.ai.android.features.billcollection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowBillCollectionAtsiteBinding
import com.sisindia.ai.android.databinding.RowStringHeaderBinding
import com.sisindia.ai.android.features.billcollection.BillCollectionListener
import com.sisindia.ai.android.uimodels.collection.CollectionCardDetailsMO

/**
 * Created by Ashu Rajput on 5/30/2020.
 */
class CollectionsCardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var collectionList: List<Any>
    private lateinit var listener: BillCollectionListener

    companion object {
        private const val TYPE_HEADER: Int = 1
        private const val TYPE_ITEM: Int = 2
    }

    fun initListener(listener: BillCollectionListener) {
        this.listener = listener
    }

    fun updateCollectionData(collectionList: List<Any>) {
        this.collectionList = collectionList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_HEADER -> {
                val view: RowStringHeaderBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_string_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_ITEM -> {
                val view: RowBillCollectionAtsiteBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_bill_collection_atsite, parent, false)
                ItemsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return if (::collectionList.isInitialized) collectionList.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            collectionList[position] is String -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bindHeaderView()
            is ItemsViewHolder -> holder.bindItemsView()
        }
    }

    inner class HeaderViewHolder(private val binder: RowStringHeaderBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindHeaderView() {
            binder.header = collectionList[layoutPosition] as String
        }
    }

    inner class ItemsViewHolder(private val binder: RowBillCollectionAtsiteBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindItemsView() {
            val collectionCardModel = collectionList[layoutPosition] as CollectionCardDetailsMO
            binder.model = collectionCardModel
            if (::listener.isInitialized)
                binder.root.setOnClickListener {
                    if (collectionCardModel.isPending)
                        listener.onBillSelected(collectionList[layoutPosition])
                }
        }
    }
}