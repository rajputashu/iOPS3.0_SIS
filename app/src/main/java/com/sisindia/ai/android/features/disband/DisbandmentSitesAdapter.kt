package com.sisindia.ai.android.features.disband

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowDisbandmentSitesBinding

/**
 * Created by Ashu Rajput on 30-03-2023
 */
class DisbandmentSitesAdapter : BaseRecyclerAdapter<DisbandmentSitesMO>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view: RowDisbandmentSitesBinding =
            getViewDataBinding(R.layout.row_disbandment_sites, parent) as RowDisbandmentSitesBinding
        view.executePendingBindings()
        return DisbandmentSitesVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: DisbandmentSitesVH = holder as DisbandmentSitesVH
        viewHolder.onBind(getItem(position))
    }

    inner class DisbandmentSitesVH(val view: RowDisbandmentSitesBinding) :
        BaseViewHolder<DisbandmentSitesMO>(view) {
        override fun onBind(item: DisbandmentSitesMO) {
            view.model = item
            view.dynamicCheckBox.setOnCheckedChangeListener { _, isChecked ->
                item.isBoxChecked = isChecked
            }
        }
    }
}