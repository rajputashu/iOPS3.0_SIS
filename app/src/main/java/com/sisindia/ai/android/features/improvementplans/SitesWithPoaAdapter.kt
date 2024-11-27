package com.sisindia.ai.android.features.improvementplans

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowSitesWithPoaBinding
import com.sisindia.ai.android.databinding.RowUarHeaderBinding
import com.sisindia.ai.android.features.uar.UARListener
import com.sisindia.ai.android.uimodels.uarpoa.SitesWithImprovePlansMO

/**
 * Created by Ashu Rajput on 12/21/2020.
 */
class SitesWithPoaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: List<Any>
    private lateinit var listener: UARListener

    companion object {
        private const val TYPE_HEADER: Int = 1
        private const val TYPE_ITEM: Int = 2
    }

    fun setListener(listener: UARListener) {
        this.listener = listener
    }

    fun updateSitesWithPoaData(list: List<Any>) {
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
                val view: RowSitesWithPoaBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_sites_with_poa, parent, false)
                SitesWithPoaItemsViewHolder(view)
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
            is SitesWithPoaItemsViewHolder -> holder.bindItemsView()
        }
    }

    inner class UARHeaderViewHolder(private val binder: RowUarHeaderBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindHeaderView() {
            binder.dueDate = list[layoutPosition] as String
        }
    }

    inner class SitesWithPoaItemsViewHolder(private val binder: RowSitesWithPoaBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindItemsView() {
            binder.model = list[layoutPosition] as SitesWithImprovePlansMO
            binder.root.setOnClickListener { listener.onUnitAtRiskSelected(list[layoutPosition]) }
        }
    }
}