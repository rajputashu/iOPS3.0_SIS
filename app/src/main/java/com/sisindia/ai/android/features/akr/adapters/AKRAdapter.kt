package com.sisindia.ai.android.features.akr.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.databinding.RowAkrBinding
import com.sisindia.ai.android.features.akr.AKRListener
import com.sisindia.ai.android.uimodels.akr.KitBySiteDetailsMO

/**
 * Created by Ashu Rajput on 4/16/2020.
 */
class AKRAdapter : BaseRecyclerAdapter<KitBySiteDetailsMO>() {
    private lateinit var akrListener: AKRListener

    fun initListener(akrListener: AKRListener) {
        this.akrListener = akrListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowAkrBinding = getViewDataBinding(R.layout.row_akr, parent) as RowAkrBinding
        view.executePendingBindings()
        return AKRViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: AKRViewHolder = holder as AKRViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class AKRViewHolder(val view: RowAkrBinding) : BaseViewHolder<KitBySiteDetailsMO>(view) {
        override fun onBind(item: KitBySiteDetailsMO) {
            view.model = item
            view.root.setOnClickListener {
                akrListener.onAkrSelected()
                Prefs.putInt(PrefConstants.AKR_SITE_ID, getItem(layoutPosition).siteId!!)
            }
        }
    }
}