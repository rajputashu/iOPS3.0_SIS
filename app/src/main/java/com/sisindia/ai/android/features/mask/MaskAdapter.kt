package com.sisindia.ai.android.features.mask

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowMaskItemBinding
import com.sisindia.ai.android.models.mask.DistributedMaskMO

class MaskAdapter : BaseRecyclerAdapter<DistributedMaskMO>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view: RowMaskItemBinding = getViewDataBinding(R.layout.row_mask_item, parent)
                as RowMaskItemBinding
        view.executePendingBindings()
        return MaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: MaskViewHolder = holder as MaskViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class MaskViewHolder(val view: RowMaskItemBinding) :
        BaseViewHolder<DistributedMaskMO>(view) {
        override fun onBind(item: DistributedMaskMO) {
            view.model = item
        }
    }
}