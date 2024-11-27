package com.sisindia.ai.android.features.issues.grievance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.databinding.AdapterItemSiteBinding
import com.sisindia.ai.android.room.entities.SiteEntity

class SiteListSpinnerAdapter constructor(val ctx: Context,
    @field:LayoutRes private val resourceId: Int,
    private val sites: List<SiteEntity>) : ArrayAdapter<SiteEntity>(ctx, resourceId) {

    override fun getItem(position: Int): SiteEntity {
        return sites[position]
    }

    override fun getCount(): Int {
        return sites.size
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return rowView(convertView, position)
    }

    override fun getView(position: Int,
        convertView: View?,
        parent: ViewGroup): View {
        return rowView(convertView, position)
    }

    private fun rowView(convertView: View?, position: Int): View {
        var rowView = convertView
        if (rowView == null) {
            val binding: AdapterItemSiteBinding =
                DataBindingUtil.inflate(LayoutInflater.from(ctx), resourceId, null, false)
            rowView = binding.root
            rowView.tag = binding
        }
        val item = getItem(position)
        val binding =
            rowView.tag as AdapterItemSiteBinding
        binding.item = item
        return rowView
    }

}