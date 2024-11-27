package com.sisindia.ai.android.features.clientcoordination.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowCcrPerformanceRatingBinding
import com.sisindia.ai.android.features.clientcoordination.entity.PerformanceRatingMO

/**
 * Created by Ashu Rajput on 3/16/2020.
 */
class CCRPerformanceRatingAdapter(val list: ArrayList<PerformanceRatingMO>) :

    RecyclerView.Adapter<CCRPerformanceRatingAdapter.PerformanceRatingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformanceRatingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: RowCcrPerformanceRatingBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_ccr_performance_rating, parent, false)
        return PerformanceRatingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PerformanceRatingViewHolder, position: Int) {
        holder.bindRatingViews()
    }

    inner class PerformanceRatingViewHolder(private val binder: RowCcrPerformanceRatingBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindRatingViews() {
            binder.performanceMO = list[layoutPosition]
        }
    }
}