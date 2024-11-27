package com.sisindia.ai.android.features.addgrievances

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object GrievanceItemBindings {

    @JvmStatic
    @BindingAdapter(value = ["setGrievanceRecyclerAdapter", "setGrievanceItemListener"],
        requireAll = false)
    fun RecyclerView.setGrievanceAdapter(adapter: GrievanceRecyclerAdapter,
        listeners: GrievanceItemListener?) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setLayoutManager(layoutManager)
        adapter.setItemListener(listeners)
        setAdapter(adapter)
    }

}