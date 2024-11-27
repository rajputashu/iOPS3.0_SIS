package com.sisindia.ai.android.features.billsubmit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowBillSubmissionCardBinding
import com.sisindia.ai.android.databinding.RowStringHeaderBinding
import com.sisindia.ai.android.features.billsubmit.BillSubmissionListener
import com.sisindia.ai.android.uimodels.billsubmit.BillSubmissionCardDetailsMO

/**
 * Created by Ashu Rajput on 6/1/2020.
 */
class BillSubmissionCardsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<Any>()
    private lateinit var listener: BillSubmissionListener

    companion object {
        private const val TYPE_HEADER: Int = 1
        private const val TYPE_ITEM: Int = 2
    }

    fun initListener(listener: BillSubmissionListener) {
        this.listener = listener
    }

    fun updateBillSubmissionData(updatedList: List<Any>) {
        list.clear()
        list.addAll(updatedList)
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
                val view: RowBillSubmissionCardBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_bill_submission_card, parent, false)
                ItemsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return if (list.isNotEmpty()) list.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            list[position] is String -> TYPE_HEADER
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
            binder.header = list[layoutPosition] as String
        }
    }

    inner class ItemsViewHolder(private val binder: RowBillSubmissionCardBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindItemsView() {
            val billModel = list[layoutPosition] as BillSubmissionCardDetailsMO
            binder.model = billModel
            if (::listener.isInitialized)
                binder.root.setOnClickListener {
                    if (billModel.isPending)
                        listener.onBillSelected(list[layoutPosition])
                }
        }
    }
}