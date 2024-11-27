package com.sisindia.ai.android.features.rotacompliance.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowRotaComplianceBinding
import com.sisindia.ai.android.models.rota.RotaComplianceResultMO

/**
 * Created by Ashu Rajput on 6/1/2020.
 */
class RotaComplianceAdapter : BaseRecyclerAdapter<RotaComplianceResultMO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowRotaComplianceBinding = getViewDataBinding(R.layout.row_rota_compliance,
            parent) as RowRotaComplianceBinding
        view.executePendingBindings()
        return RotaComplianceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: RotaComplianceViewHolder = holder as RotaComplianceViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class RotaComplianceViewHolder(val view: RowRotaComplianceBinding) :
        BaseViewHolder<RotaComplianceResultMO>(view) {
        override fun onBind(item: RotaComplianceResultMO) {
            view.model = item
        }
    }
}