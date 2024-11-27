package com.sisindia.ai.android.features.billsubmit.sheet

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetBillChecklistBinding
import com.sisindia.ai.android.uimodels.billsubmit.BillCheckListMO

/**
 * Created by Ashu Rajput on 4/1/2020.
 */
class BillChecklistBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetBillChecklistBinding
    lateinit var viewModel: BillChecklistViewModel

    companion object {
        fun newInstance(list: ArrayList<BillCheckListMO>) = BillChecklistBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(IntentConstants.BS_CHECKLIST, list)
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_bill_checklist
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetBillChecklistBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
        val checkList =
            requireArguments().getParcelableArrayList<BillCheckListMO>(IntentConstants.BS_CHECKLIST)
        viewModel.billCheckListAdapter.updateBillChecklist(checkList!!)
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_BILL_CHECKLIST_DONE -> {
                    message.what = NavigationConstants.ON_BILL_CHECKLIST_OPTED
                    message.obj = message.obj
                    liveData.value = message
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    override fun onCreated() {
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BillChecklistViewModel::class.java)
                as BillChecklistViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
}