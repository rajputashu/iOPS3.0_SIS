package com.sisindia.ai.android.features.uar.poa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowPoaItemsBinding
import com.sisindia.ai.android.databinding.RowUarHeaderBinding
import com.sisindia.ai.android.features.uar.UARListener
import com.sisindia.ai.android.uimodels.uarpoa.POADetailsMO

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
/**
 * @POAAdapter {Used for both "Pending POA" recyclerview and "Completed" POA Recyclerview}
 */
class POAAdapter : Adapter<ViewHolder>() {

    private lateinit var poaList: List<Any>
    private lateinit var listener: UARListener

    companion object {
        private const val TYPE_HEADER: Int = 1
        private const val TYPE_ITEM: Int = 2
    }

    fun initListener(listener: UARListener) {
        this.listener = listener
    }

    fun updatePOAData(poaList: List<Any>) {
        this.poaList = poaList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_HEADER -> {
                val view: RowUarHeaderBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_uar_header, parent, false)
                UARHeaderViewHolder(view)
            }
            TYPE_ITEM -> {
                val view: RowPoaItemsBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_poa_items, parent, false)
                UARItemsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return if (::poaList.isInitialized) poaList.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            poaList[position] is String -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is UARHeaderViewHolder -> holder.bindHeaderView()
            is UARItemsViewHolder -> holder.bindItemsView()
        }
    }

    inner class UARHeaderViewHolder(private val binder: RowUarHeaderBinding) :
        ViewHolder(binder.root) {
        fun bindHeaderView() {
            binder.dueDate = poaList[layoutPosition] as String
        }
    }

    inner class UARItemsViewHolder(private val binder: RowPoaItemsBinding) :
        ViewHolder(binder.root) {
        fun bindItemsView() {
//            binder.poaMO = poaList[layoutPosition] as PlanOfActionMO
            binder.poaMO = poaList[layoutPosition] as POADetailsMO
            if (::listener.isInitialized)
                binder.root.setOnClickListener { listener.onPendingPOASelected(poaList[layoutPosition]) }
        }
    }
}