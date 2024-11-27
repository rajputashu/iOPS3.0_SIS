package com.sisindia.ai.android.features.sync

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowManualSyncOptionsBinding

/**
 * Created by Ashu Rajput on 10/6/2020.
 */
class ManualSyncAdapter : BaseRecyclerAdapter<SyncOptionsMO>() {

    lateinit var listener: ManualSyncListener

    fun initListener(listener: ManualSyncListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowManualSyncOptionsBinding =
            getViewDataBinding(R.layout.row_manual_sync_options, parent) as RowManualSyncOptionsBinding
        view.executePendingBindings()
        return SyncViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SyncViewHolder).onBind(getItem(position))
    }

    inner class SyncViewHolder(val view: RowManualSyncOptionsBinding) :
        BaseViewHolder<SyncOptionsMO>(view) {
        override fun onBind(item: SyncOptionsMO) {
            view.apply {
                model = item
                if (layoutPosition == 7 || layoutPosition == 8) {
                    if (!item.dutySyncStatus.isNullOrEmpty())
                        dutyStatusMsgId.visibility = View.VISIBLE
                    else
                        dutyStatusMsgId.visibility = View.GONE
                } else
                    dutyStatusMsgId.visibility = View.GONE

                root.setOnClickListener {
                    listener.onChoosingOption(item.optionId)
                }
            }
        }
    }

    interface ManualSyncListener {
        fun onChoosingOption(position: Int)
    }
}