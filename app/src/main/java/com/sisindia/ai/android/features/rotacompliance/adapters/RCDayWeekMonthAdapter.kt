package com.sisindia.ai.android.features.rotacompliance.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.sisindia.ai.android.R
import com.sisindia.ai.android.commons.graph.RoundedBarChart
import com.sisindia.ai.android.databinding.RowRcDayWeekMonthBinding
import com.sisindia.ai.android.commons.RCViewType
import com.sisindia.ai.android.features.rotacompliance.entities.ComplianceTaskDetailsMO
import com.sisindia.ai.android.features.rotacompliance.entities.DayWeekMonthMO
import com.sisindia.ai.android.utils.toHtmlSpan

/**
 * Created by Ashu Rajput on 3/6/2020.
 */
class RCDayWeekMonthAdapter(val context: Context, private val list: ArrayList<DayWeekMonthMO>) :
    RecyclerView.Adapter<RCDayWeekMonthAdapter.DWMViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DWMViewHolder {
        val view: RowRcDayWeekMonthBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_rc_day_week_month, parent, false)
        return DWMViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DWMViewHolder, position: Int) {
        holder.bindViews()
    }

    inner class DWMViewHolder(private val rowBinder: RowRcDayWeekMonthBinding) :
        RecyclerView.ViewHolder(rowBinder.root) {
        fun bindViews() {
            rowBinder.complianceChartHeader.text = list[layoutPosition].chartHeader
            when (list[layoutPosition].chartTypeId) {
                RCViewType.DAY.viewType -> rowBinder.complianceChartPercentage.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, 0, 0)

                RCViewType.WEEKLY.viewType -> {
                    rowBinder.complianceChartPercentage.text =
                        context.resources.getString(R.string.dynamic_weekly_percentage,
                            list[layoutPosition].chartPercentage).toHtmlSpan()
                }
                RCViewType.MONTHLY.viewType -> {
                    rowBinder.complianceChartPercentage.text =
                        context.resources.getString(R.string.dynamic_monthly_percentage,
                            list[layoutPosition].chartPercentage).toHtmlSpan()
                }
            }
            setUpBarChart(rowBinder.complianceBarChart, list[layoutPosition].taskDetailsList!!)
        }
    }

    private fun setUpBarChart(barChart: RoundedBarChart, list: ArrayList<ComplianceTaskDetailsMO>) {
        barChart.setDrawBarShadow(false)
        barChart.setTouchEnabled(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.setRadius(16)
        barChart.setDrawMarkers(true)

        val achievedBarEntryList = ArrayList<BarEntry>()
        val targetBarEntryList = ArrayList<BarEntry>()
        val avgBarEntryList = ArrayList<BarEntry>()

        val barLabels = ArrayList<String>()
        for ((i, model) in list.withIndex()) {
            barLabels.add(model.taskType!!)
            achievedBarEntryList.add(BarEntry(i.toFloat(), model.achievedValue!!))
            targetBarEntryList.add(BarEntry(i.toFloat(), model.targetValue!!))
            avgBarEntryList.add(BarEntry(i.toFloat(), model.averageValue!!))
        }

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return barLabels[value.toInt()]
            }
        }

        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.granularity = 20f // only intervals of 1 day
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 100f

        val achievedBarDataSet = BarDataSet(achievedBarEntryList, "")
        achievedBarDataSet.color = ContextCompat.getColor(context, R.color.colorLightOrange)
        val targetBarDataSet = BarDataSet(targetBarEntryList, "")
        targetBarDataSet.color = ContextCompat.getColor(context, R.color.colorUltraLightBlue)
        val avgBarDataSet = BarDataSet(avgBarEntryList, "")
        avgBarDataSet.color = ContextCompat.getColor(context, R.color.colorIndigoBlue)

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(targetBarDataSet)
        dataSets.add(avgBarDataSet)
        dataSets.add(achievedBarDataSet)

        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.setDrawValues(false)
        data.barWidth = 0.15f
        barChart.data = data
    }
}