package com.sisindia.ai.android.features.barracks.listing

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowBarracksListingBinding
import com.sisindia.ai.android.uimodels.barracks.listing.BarrackListingMO

/**
 * Created by Ashu Rajput on 4/18/2020.
 */
class BarrackListingAdapter : BaseRecyclerAdapter<BarrackListingMO>() {

    private lateinit var barrackListener: BarrackListener

    fun initListener(barrackListener: BarrackListener) {
        this.barrackListener = barrackListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowBarracksListingBinding = getViewDataBinding(R.layout.row_barracks_listing,
            parent) as RowBarracksListingBinding
        view.executePendingBindings()
        return BarrackListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BarrackListingViewHolder).onBind(getItem(position))
    }

    inner class BarrackListingViewHolder(val view: RowBarracksListingBinding) :
        BaseViewHolder<BarrackListingMO>(view) {
        override fun onBind(item: BarrackListingMO) {
            view.barrackModel = item
            view.root.setOnClickListener {
//                barrackListener.onBarrackSelected(layoutPosition)
            }
            view.barrackQR.setOnClickListener {
                barrackListener.onTagQRSelected(item.id!!)
            }
        }
    }
}