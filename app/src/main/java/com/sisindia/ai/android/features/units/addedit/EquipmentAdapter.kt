package com.sisindia.ai.android.features.units.addedit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowEquipmentsBinding

/**
 * Created by Ashu Rajput on 3/25/2020.
 */
class EquipmentAdapter : RecyclerView.Adapter<EquipmentAdapter.EquipmentVH>() {

    private lateinit var list: ArrayList<EquipmentsMO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentVH {
        val inflater = LayoutInflater.from(parent.context)
        val view: RowEquipmentsBinding = DataBindingUtil.inflate(inflater,
            R.layout.row_equipments, parent, false)
        return EquipmentVH(view)
    }

    fun updateEquipmentsList(list: ArrayList<EquipmentsMO>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }

    override fun onBindViewHolder(holder: EquipmentVH, position: Int) {
        holder.bindViews()
    }

    inner class EquipmentVH(private val binder: RowEquipmentsBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindViews() {
            binder.equipmentMO = list[layoutPosition]
        }
    }
}