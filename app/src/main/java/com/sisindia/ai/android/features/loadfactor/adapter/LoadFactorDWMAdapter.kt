package com.sisindia.ai.android.features.loadfactor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowLoadfactorDwmBinding
import com.sisindia.ai.android.commons.RCViewType
import com.sisindia.ai.android.features.rotacompliance.entities.DayWeekMonthMO
import com.sisindia.ai.android.utils.toHtmlSpan

/**
 * Created by Ashu Rajput on 3/11/2020.
 */
class LoadFactorDWMAdapter(val context: Context, private val list: ArrayList<DayWeekMonthMO>) :
    RecyclerView.Adapter<LoadFactorDWMAdapter.LoadFactorDWMViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadFactorDWMViewHolder {
        val view: RowLoadfactorDwmBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_loadfactor_dwm, parent, false)
        return LoadFactorDWMViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LoadFactorDWMViewHolder, position: Int) {
        holder.bindViews()
    }

    inner class LoadFactorDWMViewHolder(private val rowBinder: RowLoadfactorDwmBinding) :
        RecyclerView.ViewHolder(rowBinder.root) {
        fun bindViews() {
            rowBinder.loadFactorChartHeader.text = list[layoutPosition].chartHeader
            when (list[layoutPosition].chartTypeId) {
                RCViewType.DAY.viewType -> rowBinder.loadFactorChartPercentage.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, 0, 0)

                RCViewType.WEEKLY.viewType -> {
                    rowBinder.loadFactorChartPercentage.text =
                        context.resources.getString(R.string.dynamic_weekly_percentage,
                            list[layoutPosition].chartPercentage).toHtmlSpan()
                }
                RCViewType.MONTHLY.viewType -> {
                    rowBinder.loadFactorChartPercentage.text =
                        context.resources.getString(R.string.dynamic_monthly_percentage,
                            list[layoutPosition].chartPercentage).toHtmlSpan()
                }
            }

            rowBinder.progressDescription.text =
                context.resources.getString(R.string.dynamic_load_factor_string,
                    "07/14").toHtmlSpan()
        }
    }
}