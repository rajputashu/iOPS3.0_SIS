package com.sisindia.ai.android.features.civil

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowNominationBinding
import com.sisindia.ai.android.databinding.RowSalesReferenceBinding
import com.sisindia.ai.android.models.civil.CivilNominationMO

/**
 * Created by Ashu Rajput on 4/30/2021.
 */
class CivilNominationAdapter : BaseRecyclerAdapter<CivilNominationMO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowNominationBinding = getViewDataBinding(R.layout.row_nomination, parent)
                as RowNominationBinding
        view.executePendingBindings()
        return CivilNominationVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: CivilNominationVH = holder as CivilNominationVH
        viewHolder.onBind(getItem(position))
    }

    inner class CivilNominationVH(val view: RowNominationBinding) :
        BaseViewHolder<CivilNominationMO>(view) {
        override fun onBind(item: CivilNominationMO) {
            view.model = item
        }
    }
}