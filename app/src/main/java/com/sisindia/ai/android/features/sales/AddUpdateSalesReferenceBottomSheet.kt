package com.sisindia.ai.android.features.sales

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetAddUpdateSalesBinding
import com.sisindia.ai.android.models.sales.SalesReferenceMO
import com.sisindia.ai.android.utils.TimeUtils
//import kotlinx.android.synthetic.main.bottom_sheet_add_update_sales.*
import org.threeten.bp.LocalDate

/**
 * Created by Ashu Rajput on 5/1/2021.
 */
class AddUpdateSalesReferenceBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddUpdateSalesBinding
    lateinit var viewModel: SalesReferenceViewModel
    var isComingToAdd: Boolean = true

    companion object {
        fun newInstance(isComingToAdd: Boolean) = AddUpdateSalesReferenceBottomSheet().apply {
            arguments = Bundle().apply {
                putBoolean(IntentConstants.IS_COMING_TO_ADD_SALES, isComingToAdd)
            }
        }

        fun newInstanceWithData(isComingToAdd: Boolean, salesMO: SalesReferenceMO) =
            AddUpdateSalesReferenceBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(IntentConstants.IS_COMING_TO_ADD_SALES, isComingToAdd)
                    putParcelable(IntentConstants.SALES_MO, salesMO)
                }
            }
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_add_update_sales
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater,
            container) as BottomSheetAddUpdateSalesBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
        arguments?.apply {
            isComingToAdd = this.getBoolean(IntentConstants.IS_COMING_TO_ADD_SALES)
            if (this.containsKey(IntentConstants.SALES_MO)) {
                viewModel.obsSelectedSalesItemForUpdate.set(this.getParcelable(IntentConstants.SALES_MO))
            }
        }
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_DATE_PICKER -> openDatePicker()
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
        viewModel.initAddOrUpdateUI(isComingToAdd)
        binding.siteCodeET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 11) {
                    viewModel.obsIsContactsAvailable.set(false)
                    viewModel.obsSiteName.set("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(SalesReferenceViewModel::class.java) as SalesReferenceViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun openDatePicker() {
        val date = LocalDate.now()
        val datePickerDialog = DatePickerDialog(requireActivity(),
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                viewModel.obsRecruitDOB.set(TimeUtils.TASK_DATE_FORMAT(selectedDate))
                viewModel.obsDateForAPI.set(TimeUtils.DOB_DATE_FORMAT(selectedDate))
            }, date.year, date.monthValue - 1, date.dayOfMonth)
        //        datePickerDialog.datePicker.minDate = Instant.now().toEpochMilli()
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
    }
}