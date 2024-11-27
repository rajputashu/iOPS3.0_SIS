package com.sisindia.ai.android.features.recruitment.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.sisindia.ai.android.R
import com.sisindia.ai.android.commons.graph.RoundedBarChart
import com.sisindia.ai.android.databinding.RowRecruitmentBinding
import com.sisindia.ai.android.models.recruit.RecruitmentDataMO
import com.sisindia.ai.android.utils.TimeUtils

/**
 * Created by Ashu Rajput on 5/6/2020.
 */
class RecruitmentGraphAdapter : BaseRecyclerAdapter<RecruitmentDataMO>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view: RowRecruitmentBinding = getViewDataBinding(R.layout.row_recruitment, parent)
                as RowRecruitmentBinding
        view.executePendingBindings()
        return RecruitmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: RecruitmentViewHolder = holder as RecruitmentViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class RecruitmentViewHolder(val view: RowRecruitmentBinding) :
        BaseViewHolder<RecruitmentDataMO>(view) {
        override fun onBind(item: RecruitmentDataMO) {
            setUpBarChart(view.recruitBarChart, item)
            view.chartTopLabel.text = TimeUtils.getMonth(item.month!!) + "," + item.year!!
        }
    }

    private fun setUpBarChart(barChart: RoundedBarChart, recruitMO: RecruitmentDataMO) {
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

        val recruitBarList = ArrayList<BarEntry>()
        recruitBarList.add(BarEntry(0f, recruitMO.selectedCount!!.toFloat()))
        recruitBarList.add(BarEntry(1f, recruitMO.rejectedCount!!.toFloat()))
//        recruitBarList.add(BarEntry(2f, recruitMO.inProcessCount!!.toFloat()))
        recruitBarList.add(BarEntry(2f, recruitMO.recommended!!.toFloat()))

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f

        val barLabels = ArrayList<String>()
        barLabels.add("Selected")
        barLabels.add("Rejected")
//        barLabels.add("InProcess")
        barLabels.add("Recommended")
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return barLabels[value.toInt()]
            }
        }
        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.granularity = 1f // only intervals of 1 day
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.axisMinimum = 0f

        val recruitBarDataSet = BarDataSet(recruitBarList, "")
        recruitBarDataSet.colors =
            arrayListOf(ContextCompat.getColor(context, R.color.colorCircularBlue),
                ContextCompat.getColor(context, R.color.colorLightRed),
                ContextCompat.getColor(context, R.color.colorStatusPending))
//        ContextCompat.getColor(context, R.color.colorLightOrange),// Commenting as inProgress == recommended
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(recruitBarDataSet)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.setDrawValues(false)
        data.barWidth = 0.16f
        barChart.data = data
    }
}