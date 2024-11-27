package com.sisindia.ai.android.features.recruitment.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowRecruitedItemBinding
import com.sisindia.ai.android.models.recruit.RecruitedMO

/**
 * Created by Ashu Rajput on 5/7/2020.
 */
class RecommendedRecruitAdapter : BaseRecyclerAdapter<RecruitedMO>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view: RowRecruitedItemBinding = getViewDataBinding(R.layout.row_recruited_item, parent)
                as RowRecruitedItemBinding
        view.executePendingBindings()
        return RecruitmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: RecruitmentViewHolder = holder as RecruitmentViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class RecruitmentViewHolder(val view: RowRecruitedItemBinding) :
        BaseViewHolder<RecruitedMO>(view) {
        override fun onBind(item: RecruitedMO) {
            view.model = item
        }
    }
}