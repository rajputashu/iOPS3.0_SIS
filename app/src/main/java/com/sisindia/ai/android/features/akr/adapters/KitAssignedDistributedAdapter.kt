package com.sisindia.ai.android.features.akr.adapters

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.databinding.RowKitAssignedDistributedBinding
import com.sisindia.ai.android.features.akr.AKRListener
import com.sisindia.ai.android.uimodels.akr.KitAssignedDistributedMO

/**
 * Created by Ashu Rajput on 4/16/2020.
 */
class KitAssignedDistributedAdapter : BaseRecyclerAdapter<KitAssignedDistributedMO>() {
    private lateinit var akrListener: AKRListener

    fun initListener(akrListener: AKRListener) {
        this.akrListener = akrListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowKitAssignedDistributedBinding =
            getViewDataBinding(R.layout.row_kit_assigned_distributed,
                parent) as RowKitAssignedDistributedBinding
        view.executePendingBindings()
        return KitAssignedDistributedVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: KitAssignedDistributedVH = holder as KitAssignedDistributedVH
        viewHolder.onBind(getItem(position))
    }

    inner class KitAssignedDistributedVH(val view: RowKitAssignedDistributedBinding) :
        BaseViewHolder<KitAssignedDistributedMO>(view) {
        override fun onBind(item: KitAssignedDistributedMO) {
            view.model = item
            // Checking if status is pending then only allowing card to click [not in case of status=3(completed)]
//            if (item.status == 1 || item.status == 2 || item.status == 3) {
            if (item.status == 1 || item.status == 2) {
                view.root.setOnClickListener {
                    if (getItem(layoutPosition).kitIssueNo.isNullOrEmpty()) {
                        Toast.makeText(view.root.context, "Kit issue no. not found",
                            Toast.LENGTH_LONG).show()
                    } else {
                        akrListener.onAssignedAkrForGuardSelected()
                        Prefs.putInt(PrefConstants.AKR_GUARD_ID, getItem(layoutPosition).guardId!!)
                        Prefs.putString(PrefConstants.AKR_KIT_ISSUE_NO,
                            getItem(layoutPosition).kitIssueNo!!)
                    }
                }
            }
        }
    }
}