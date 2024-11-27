package com.sisindia.ai.android.features.timline

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R

object TimeLineViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["setTimeLineAdapter"], requireAll = false)
    fun RecyclerView.setTimeLineAdapter(adapter: TimeLineAdapter) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setLayoutManager(layoutManager)
        val dividerItemDecoration =
            DividerItemDecoration(context, layoutManager.orientation)
        val item = ContextCompat.getDrawable(context, R.drawable.grey_divider_1sdp)

        if (item != null) {
            dividerItemDecoration.setDrawable(item)
            addItemDecoration(dividerItemDecoration)
        }
        setAdapter(adapter)
    }

}