package com.sisindia.ai.android.features.nudges

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowNudgesDashboardBinding
import com.sisindia.ai.android.room.entities.NotificationDataEntity

/**
 * Created by Ashu Rajput on 11/19/2024.
 */
class NudgesDashboardAdapter : BaseRecyclerAdapter<NotificationDataEntity>() {

    private lateinit var listener: NudgesListener

    fun initListener(listener: NudgesListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowNudgesDashboardBinding =
            getViewDataBinding(R.layout.row_nudges_dashboard, parent) as RowNudgesDashboardBinding
        view.executePendingBindings()
        return NudgesDashboardVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NudgesDashboardVH).onBind(getItem(position))
    }

    inner class NudgesDashboardVH(val view: RowNudgesDashboardBinding) :
        BaseViewHolder<NotificationDataEntity>(view) {
        override fun onBind(item: NotificationDataEntity) {
            view.model = item
            view.root.setOnClickListener {
                listener.onDashboardNudgesSelection(item.ids)
            }
        }
    }
}