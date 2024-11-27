package com.sisindia.ai.android.features.improvementplans

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowImprovePlansPoaBinding
import com.sisindia.ai.android.databinding.RowUarHeaderBinding
import com.sisindia.ai.android.features.uar.UARListener
import com.sisindia.ai.android.uimodels.uarpoa.IPPoaPendingCompletedMO

/**
 * Created by Ashu Rajput on 12/22/2020.
 */
class IPPoaDetailsAdapter(private val isPendingRequest: Boolean = true) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: List<Any>
    private lateinit var listener: UARListener

    companion object {
        private const val TYPE_HEADER: Int = 1
        private const val TYPE_ITEM: Int = 2
    }

    fun initListener(listener: UARListener) {
        this.listener = listener
    }

    fun updatePOAData(list: List<Any>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_HEADER -> {
                val view: RowUarHeaderBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_uar_header, parent, false)
                UARHeaderViewHolder(view)
            }
            TYPE_ITEM -> {
                val view: RowImprovePlansPoaBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_improve_plans_poa, parent, false)
                UARItemsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            list[position] is String -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UARHeaderViewHolder -> holder.bindHeaderView()
            is UARItemsViewHolder -> holder.bindItemsView()
        }
    }

    inner class UARHeaderViewHolder(private val binder: RowUarHeaderBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindHeaderView() {
            binder.dueDate = list[layoutPosition] as String
        }
    }

    inner class UARItemsViewHolder(private val binder: RowImprovePlansPoaBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindItemsView() {
            binder.model = list[layoutPosition] as IPPoaPendingCompletedMO
            if (::listener.isInitialized && isPendingRequest)
                binder.root.setOnClickListener { listener.onPendingPOASelected(list[layoutPosition]) }
        }
    }
}