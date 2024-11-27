package com.sisindia.ai.android.features.units.details.strength

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
import com.sisindia.ai.android.databinding.RowUnitStrengthBinding
import com.sisindia.ai.android.uimodels.mysite.SiteStrengthMO

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class UnitStrengthAdapter : RecyclerView.Adapter<UnitStrengthAdapter.UnitStrengthVH>() {

    private lateinit var context: Context
    private lateinit var list: List<SiteStrengthMO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitStrengthVH {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view: RowUnitStrengthBinding = DataBindingUtil.inflate(inflater,
            R.layout.row_unit_strength, parent, false)
        return UnitStrengthVH(view)
    }

    fun updateStrength(list: List<SiteStrengthMO>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }

    override fun onBindViewHolder(holder: UnitStrengthVH, position: Int) {
        holder.bindViews()
    }

    inner class UnitStrengthVH(private val binder: RowUnitStrengthBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindViews() {
            binder.strengthChartHeader.text = list[layoutPosition].grade
            setUpBarChart(binder.strengthBarChart, list[layoutPosition])
        }
    }

    private fun setUpBarChart(barChart: RoundedBarChart, strengthMO: SiteStrengthMO) {
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
        //        barChart.axisLeft.isEnabled = false
        barChart.setRadius(16)
        barChart.setDrawMarkers(true)

        val strengthBarList = ArrayList<BarEntry>()
        strengthBarList.add(BarEntry(0f, strengthMO.actualStrength!!.toFloat()))
        strengthBarList.add(BarEntry(1f, strengthMO.authorizedStrength!!.toFloat()))
        strengthBarList.add(BarEntry(2f, strengthMO.deficience!!.toFloat()))

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.typeface = ResourcesCompat.getFont(context, R.font.helvetica_medium)

        val barLabels = ArrayList<String>()
        barLabels.add("Actual")
        barLabels.add("Authorized")
        barLabels.add("Deficiency")

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return barLabels[value.toInt()]
            }
        }
        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.granularity = 2f // only intervals of 1 day
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.axisMinimum = 0f

        val strengthBarDataSet = BarDataSet(strengthBarList, "")
        strengthBarDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() == 0) "" else value.toInt().toString()
            }
        }
        strengthBarDataSet.colors = arrayListOf(ContextCompat.getColor(context, R.color.colorLightOrange),
            ContextCompat.getColor(context, R.color.colorIndigoBlue),
            ContextCompat.getColor(context, R.color.textColorRed_less_48))

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(strengthBarDataSet)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.setDrawValues(true)
        data.barWidth = 0.15f
        barChart.data = data
    }
}