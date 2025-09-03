package com.sisindia.ai.android.features.uar

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowPoaAndImprovePlansBinding

/**
 * Created by Ashu Rajput on 04/08/2024.
 */
class PoaAndImprovePlanAdapter : BaseRecyclerAdapter<PoaAndImprovePlansMO>() {

    private lateinit var listener: UARListener
    private var isComingFromRiskPoa = false

    fun initListener(listener: UARListener, isComingFromRiskPoa: Boolean = true) {
        this.listener = listener
        this.isComingFromRiskPoa = isComingFromRiskPoa
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowPoaAndImprovePlansBinding =
            getViewDataBinding(R.layout.row_poa_and_improve_plans, parent) as RowPoaAndImprovePlansBinding
        view.executePendingBindings()
        return PoaAndImprovementVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: PoaAndImprovementVH = holder as PoaAndImprovementVH
        viewHolder.onBind(getItem(position))
    }

    inner class PoaAndImprovementVH(val binder: RowPoaAndImprovePlansBinding) :
        BaseViewHolder<PoaAndImprovePlansMO>(binder) {
        override fun onBind(item: PoaAndImprovePlansMO) {
            binder.model = item
            binder.root.setOnClickListener {
                if (isComingFromRiskPoa)
                    listener.onPendingPOASelected(item)
                else
                    listener.onUnitAtRiskSelected(item)
            }
        }
    }
}