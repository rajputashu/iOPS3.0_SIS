package com.sisindia.ai.android.features.billsubmit.sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowBillChecklistBinding
import com.sisindia.ai.android.uimodels.billsubmit.BillCheckListMO

/**
 * Created by Ashu Rajput on 4/1/2020.
 */
class BillCheckListAdapter : Adapter<BillCheckListAdapter.BillChecklistVH>() {

    private lateinit var list: ArrayList<BillCheckListMO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillChecklistVH {
        val inflater = LayoutInflater.from(parent.context)
        val view: RowBillChecklistBinding = DataBindingUtil.inflate(inflater,
            R.layout.row_bill_checklist, parent, false)
        return BillChecklistVH(view)
    }

    fun updateBillChecklist(list: ArrayList<BillCheckListMO>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }

    override fun onBindViewHolder(holder: BillChecklistVH, position: Int) {
        holder.bindView()
    }

    inner class BillChecklistVH(private val binder: RowBillChecklistBinding) :
        ViewHolder(binder.root) {
        fun bindView() {
            binder.checklist = list[layoutPosition].lookUpName
            binder.billCheckListCheckBox.isChecked = list[layoutPosition].isOptionOpted
            binder.billCheckListCheckBox.setOnClickListener {
                list[layoutPosition].isOptionOpted = !list[layoutPosition].isOptionOpted
            }
        }
    }

    fun getOptedCheckList(): ArrayList<BillCheckListMO> {
        return list
    }
}