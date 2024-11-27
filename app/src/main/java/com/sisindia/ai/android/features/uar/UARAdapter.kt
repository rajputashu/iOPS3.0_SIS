package com.sisindia.ai.android.features.uar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowUarHeaderBinding
import com.sisindia.ai.android.databinding.RowUarItemsBinding
import com.sisindia.ai.android.uimodels.uarpoa.AtRiskAndPoaDetailsMO

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
class UARAdapter : Adapter<ViewHolder>() {

    private lateinit var uarList: List<Any>
    private lateinit var listener: UARListener

    companion object {
        private const val TYPE_HEADER: Int = 1
        private const val TYPE_ITEM: Int = 2
    }

    fun setListener(listener: UARListener) {
        this.listener = listener
    }

    fun updateUARData(uarList: List<Any>) {
        this.uarList = uarList
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
                val view: RowUarItemsBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_uar_items, parent, false)
                UARItemsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return if (::uarList.isInitialized) uarList.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            uarList[position] is String -> TYPE_HEADER
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
            binder.dueDate = uarList[layoutPosition] as String
        }
    }

    inner class UARItemsViewHolder(private val binder: RowUarItemsBinding) :
        ViewHolder(binder.root) {
        fun bindItemsView() {
            binder.uarMO = uarList[layoutPosition] as AtRiskAndPoaDetailsMO
            binder.root.setOnClickListener { listener.onUnitAtRiskSelected(uarList[layoutPosition]) }
        }
    }
}