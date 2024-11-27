package com.sisindia.ai.android.features.units.sheet

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetAddEquipmentBinding
import com.sisindia.ai.android.features.units.addedit.EquipmentsMO

/**
 * Created by Ashu Rajput on 3/25/2020.
 */
class AddEquipmentBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddEquipmentBinding
    lateinit var viewModel: AddEquipmentViewModel

    companion object {
        fun newInstance() = AddEquipmentBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_add_equipment
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetAddEquipmentBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this, { message: Message ->
            when (message.what) {
                NavigationConstants.EQUIPMENT_SAVE -> {
                    message.what = NavigationConstants.UPDATE_SAVED_EQUIPMENT
                    message.obj = message.obj as EquipmentsMO
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }
            }
        })
    }

    override fun onCreated() {
        viewModel.initUI()
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AddEquipmentViewModel::class.java) as AddEquipmentViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
}