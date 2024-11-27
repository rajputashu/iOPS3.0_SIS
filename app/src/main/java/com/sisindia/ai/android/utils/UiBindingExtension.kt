package com.sisindia.ai.android.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.features.disband.DisbandDashboardAdapter
import com.sisindia.ai.android.features.disband.DisbandmentSitesAdapter

/**
 * Created by Ashu Rajput on 31-03-2023
 */

@BindingAdapter(value = ["disbandSiteAdapter"])
fun RecyclerView.bindDisbandSitesAdapter(disbandSiteAdapter: DisbandmentSitesAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = disbandSiteAdapter
}

@BindingAdapter(value = ["disbandDashAdapter"])
fun RecyclerView.bindDisbandDashAdapter(disbandDashAdapter: DisbandDashboardAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = disbandDashAdapter
}