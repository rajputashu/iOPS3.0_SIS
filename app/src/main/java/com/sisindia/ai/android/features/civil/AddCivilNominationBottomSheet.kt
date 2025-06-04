package com.sisindia.ai.android.features.civil

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetAddNominationBinding

/**
 * Created by Ashu Rajput on 5/1/2021.
 */
class AddCivilNominationBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddNominationBinding
    lateinit var viewModel: CivilDefenceViewModel

    companion object {
        fun newInstance() = AddCivilNominationBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_add_nomination
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater,
            container) as BottomSheetAddNominationBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_UPDATING_SPINNER_POSITION -> {
                    binding.sectorOrStatusSpinner.setSelection(message.arg1)
                }
                NavigationConstants.ON_SALES_REF_ADDED_UPDATED_SUCCESSFULLY -> {
                    message.what = NavigationConstants.ON_REFRESHING_SALES_REFERENCE
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    override fun onCreated() {
        viewModel.initAddNominationUI()
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(CivilDefenceViewModel::class.java) as CivilDefenceViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

}