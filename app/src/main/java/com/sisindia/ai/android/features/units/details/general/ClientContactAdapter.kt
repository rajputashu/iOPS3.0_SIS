package com.sisindia.ai.android.features.units.details.general

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowClientContactBinding
import com.sisindia.ai.android.room.entities.CustomerContactEntity

/**
 * Created by Ashu Rajput on 3/23/2020.
 */
class ClientContactAdapter : RecyclerView.Adapter<ClientContactAdapter.ClientContactVH>() {

    private lateinit var list: List<CustomerContactEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientContactVH {
        val inflater = LayoutInflater.from(parent.context)
        val view: RowClientContactBinding = DataBindingUtil.inflate(inflater,
            R.layout.row_client_contact, parent, false)
        return ClientContactVH(view)
    }

    fun updateContactInfoList(list: List<CustomerContactEntity>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }

    override fun onBindViewHolder(holder: ClientContactVH, position: Int) {
        holder.bindViews()
    }

    inner class ClientContactVH(private val binder: RowClientContactBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindViews() {
            binder.ccEntity = list[layoutPosition]
        }
    }
}