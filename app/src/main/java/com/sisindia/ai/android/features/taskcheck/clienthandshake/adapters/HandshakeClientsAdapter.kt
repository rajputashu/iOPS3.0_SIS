package com.sisindia.ai.android.features.taskcheck.clienthandshake.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowHandshakeClientsBinding
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeListener
import com.sisindia.ai.android.room.entities.CustomerContactEntity

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
class HandshakeClientsAdapter : BaseRecyclerAdapter<CustomerContactEntity>() {

    private lateinit var listener: ClientHandshakeListener

    private var lastCheckedPosition = -1

    fun initListener(listener: ClientHandshakeListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowHandshakeClientsBinding =
            getViewDataBinding(R.layout.row_handshake_clients, parent) as RowHandshakeClientsBinding
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder =
            holder as ClientViewHolder
        viewHolder.view.rbClient.isChecked = position == lastCheckedPosition
        viewHolder.onBind(getItem(position)!!)
    }

    inner class ClientViewHolder(val view: RowHandshakeClientsBinding) :
        BaseViewHolder<CustomerContactEntity>(view) {

        override fun onBind(item: CustomerContactEntity?) {
            view.adapterItem = item
            view.rbClient.setOnClickListener {
                lastCheckedPosition = adapterPosition
                listener.onClientSelected(item!!)
                notifyDataSetChanged()
            }
        }
    }
}