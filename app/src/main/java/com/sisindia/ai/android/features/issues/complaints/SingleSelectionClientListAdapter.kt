package com.sisindia.ai.android.features.issues.complaints

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.AdapterItemClientBinding
import com.sisindia.ai.android.features.issues.ClientSelectionListener
import com.sisindia.ai.android.uimodels.ClientModel

class SingleSelectionClientListAdapter : BaseRecyclerAdapter<ClientModel?>() {

    private var lastCheckedPosition = -1
    private var listener: ClientSelectionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): RecyclerView.ViewHolder {
        val binding =
            getViewDataBinding(R.layout.adapter_item_client,
                parent) as AdapterItemClientBinding
        return SingleSelectionClientListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
        position: Int) {
        val viewHolder =
            holder as SingleSelectionClientListViewHolder
        viewHolder.itemBinding.rbClient.isChecked = position == lastCheckedPosition
        viewHolder.onBind(getItem(position)!!)
    }

    fun setListener(listener: ClientSelectionListener) {
        this.listener = listener
    }

    fun clearClientAdapter(){
        lastCheckedPosition = -1
        clear()
    }

    internal inner class SingleSelectionClientListViewHolder(val itemBinding: AdapterItemClientBinding) :
        BaseViewHolder<ClientModel>(itemBinding) {
        override fun onBind(item: ClientModel) {
            itemBinding.adapterItem = item
            itemBinding.rbClient.setOnClickListener {
                lastCheckedPosition = adapterPosition
                listener!!.onClientSelected(item)
                notifyDataSetChanged()
            }
        }

    }
}