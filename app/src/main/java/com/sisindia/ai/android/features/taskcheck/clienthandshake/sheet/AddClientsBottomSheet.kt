package com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetAddClientBinding

//import kotlinx.android.synthetic.main.bottom_sheet_add_client.*

/**
 * Created by Ashu Rajput on 4/9/2020.
 */
class AddClientsBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddClientBinding
    lateinit var viewModel: AddClientViewModel

    companion object {
        fun newInstance() = AddClientsBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_add_client
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container) as BottomSheetAddClientBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_SELECT_NOT_MET_REASON -> {
                    message.what = NavigationConstants.ON_SELECT_NOT_MET_REASON
                    message.obj = message.obj
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }
                NavigationConstants.ON_CLIENT_ADDED_SUCCESSFULLY -> {
                    message.what = NavigationConstants.ON_CLIENT_ADDED_SUCCESSFULLY
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    override fun onCreated() {
        binding.sheetCloseButton.setOnClickListener { dismissAllowingStateLoss() }
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AddClientViewModel::class.java) as AddClientViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
}