package com.sisindia.ai.android.features.units

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.databinding.RowUnitsBinding
import com.sisindia.ai.android.features.units.entity.SiteTaskDetailsMO

/**
 * Created by Ashu Rajput on 3/20/2020.
 */
//class UnitsAdapter(val list: ArrayList<UnitsMO>, val listener: UnitListener) :
class UnitsAdapter(val listener: UnitListener) : Adapter<UnitsAdapter.UnitsViewHolder>() {

    private lateinit var list: List<SiteTaskDetailsMO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: RowUnitsBinding = DataBindingUtil.inflate(inflater, R.layout.row_units, parent, false)
        return UnitsViewHolder(view)
    }

    fun updateSiteTaskList(list: List<SiteTaskDetailsMO>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }

    override fun onBindViewHolder(holder: UnitsViewHolder, position: Int) {
        holder.bindViews()
    }

    inner class UnitsViewHolder(private val binder: RowUnitsBinding) : ViewHolder(binder.root) {
        fun bindViews() {
            binder.unitMO = list[layoutPosition]

            //COMMENTING TEMPORARY
            /*if (list[layoutPosition].isExpandable)
                binder.upcomingActivityMainLayout.visibility = View.VISIBLE
            else
                binder.upcomingActivityMainLayout.visibility = View.GONE*/

            binder.root.setOnClickListener {
                Prefs.putInt(PrefConstants.CURRENT_SITE, list[layoutPosition].UnitId)
                listener.onUnitClick()
            }
            /*binder.expandableArrow.setOnClickListener {
                list[layoutPosition].isExpandable = !list[layoutPosition].isExpandable
                notifyDataSetChanged()
            }*/
        }
    }
}