package com.sisindia.ai.android.features.recruitment.sheet

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetAddRecruitmentBinding
import com.sisindia.ai.android.utils.TimeUtils
import org.threeten.bp.LocalDate

/**
 * Created by Ashu Rajput on 5/7/2020.
 */
class AddRecruitmentBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddRecruitmentBinding
    lateinit var viewModel: AddRecruitmentViewModel

    companion object {
        fun newInstance() = AddRecruitmentBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_add_recruitment
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetAddRecruitmentBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_DATE_PICKER -> openDatePicker()
                    NavigationConstants.ON_RECRUITMENT_ADDED_SUCCESSFULLY -> {
                        message.what = NavigationConstants.ON_REFRESHING_RECRUIT_ADDED
                        liveData.postValue(message)
                        dismissAllowingStateLoss()
                    }
                }
            })
    }

    override fun onCreated() {
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AddRecruitmentViewModel::class.java)
                as AddRecruitmentViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun openDatePicker() {
        val date = LocalDate.now()
        val datePickerDialog = DatePickerDialog(requireActivity(),
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
//                viewModel.obsRecruitDOB.set(selectedDate.toString())
                viewModel.obsRecruitDOB.set(TimeUtils.TASK_DATE_FORMAT(selectedDate))
                viewModel.obsDateOfBirthForAPI.set(TimeUtils.DOB_DATE_FORMAT(selectedDate))
            }, date.year, date.monthValue - 1, date.dayOfMonth)
//        datePickerDialog.datePicker.minDate = Instant.now().toEpochMilli()
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
    }
}