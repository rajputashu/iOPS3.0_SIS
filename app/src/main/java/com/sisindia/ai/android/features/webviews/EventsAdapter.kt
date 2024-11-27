package com.sisindia.ai.android.features.webviews

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowEventsBinding
import com.sisindia.ai.android.models.EventsDataMO

/**
 * Created by Ashu Rajput on 12-10-2022
 */
class EventsAdapter : BaseRecyclerAdapter<EventsDataMO>() {

    private lateinit var listener: EventClickListener

    fun initListener(listener: EventClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowEventsBinding = getViewDataBinding(R.layout.row_events, parent)
                as RowEventsBinding
        view.executePendingBindings()
        return EventsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: EventsViewHolder = holder as EventsViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class EventsViewHolder(val view: RowEventsBinding) :
        BaseViewHolder<EventsDataMO>(view) {
        override fun onBind(item: EventsDataMO) {
            view.model = item
            view.root.setOnClickListener {
                listener.onItemSelected(layoutPosition)
            }
        }
    }
}