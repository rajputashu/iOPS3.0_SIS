package com.sisindia.ai.android.features.akr.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowKitItemReplaceBinding
import com.sisindia.ai.android.uimodels.akr.KitToReplaceItemsMO

/**
 * Created by Ashu Rajput on 4/17/2020.
 */
class KitItemsAdapter : BaseRecyclerAdapter<KitToReplaceItemsMO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowKitItemReplaceBinding = getViewDataBinding(R.layout.row_kit_item_replace,
            parent) as RowKitItemReplaceBinding
        view.executePendingBindings()
        return KitItemsReplaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: KitItemsReplaceViewHolder = holder as KitItemsReplaceViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class KitItemsReplaceViewHolder(val view: RowKitItemReplaceBinding) :
        BaseViewHolder<KitToReplaceItemsMO>(view) {
        override fun onBind(item: KitToReplaceItemsMO) {
            view.model = item
            getItem(layoutPosition).isSelected = true
            /*view.kitItemCheckBox.setOnClickListener {
                getItem(layoutPosition).isSelected = !getItem(layoutPosition).isSelected
            }*/
        }
    }
}