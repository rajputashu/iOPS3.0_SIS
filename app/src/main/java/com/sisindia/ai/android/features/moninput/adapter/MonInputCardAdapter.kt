package com.sisindia.ai.android.features.moninput.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowMoninputCardBinding
import com.sisindia.ai.android.databinding.RowStringHeaderBinding
import com.sisindia.ai.android.features.moninput.MonInputListener
import com.sisindia.ai.android.uimodels.moninput.MonInputCardDetailMO

/**
 * Created by Ashu Rajput on 6/2/2020.
 */
class MonInputCardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: List<Any>
    private lateinit var listener: MonInputListener

    companion object {
        private const val TYPE_HEADER: Int = 1
        private const val TYPE_ITEM: Int = 2
    }

    fun initListener(listener: MonInputListener) {
        this.listener = listener
    }

    fun updateMonInputData(list: List<Any>) {
        this.list = list
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
                val view: RowMoninputCardBinding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_moninput_card, parent, false)
                ItemsViewHolder(view)
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

    inner class ItemsViewHolder(private val binder: RowMoninputCardBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindItemsView() {
            val model = list[layoutPosition] as MonInputCardDetailMO
            binder.model = model
            if (::listener.isInitialized)
                binder.root.setOnClickListener {
                    if (model.isPending) listener.onMonInputSelected(model.taskId)
                }
        }
    }
}