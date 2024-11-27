package com.sisindia.ai.android.features.disband

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.AddDisbandmentSheetBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.utils.TimeUtils
import org.parceler.Parcels
import org.threeten.bp.LocalDate

/**
 * Created by Ashu Rajput on 30-03-2023
 */
class AddDisbandmentBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: AddDisbandmentSheetBinding
    lateinit var viewModel: DisbandmentViewModel

    companion object {
        fun newInstance() = AddDisbandmentBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.add_disbandment_sheet
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as AddDisbandmentSheetBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_DATE_PICKER -> openDatePicker()
                NavigationConstants.ON_CAPTURE_DOCUMENT -> {
                    val intent = CaptureImageActivityV2.newIntent(requireActivity(),
                        viewModel.obsAttachment.get()!!)
                    resultLauncher.launch(intent)
                }
                NavigationConstants.ON_ADDING_DISBANDMENT_SITE -> {
                    message.what = NavigationConstants.ON_REFRESHING_DISBAND_DASHBOARD
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    override fun onCreated() {
        viewModel.initUI()
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(DisbandmentViewModel::class.java) as DisbandmentViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun openDatePicker() {
        val date = LocalDate.now()
        val datePickerDialog = DatePickerDialog(requireActivity(),
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                viewModel.obsDate.set(TimeUtils.TASK_DATE_FORMAT(selectedDate))
                viewModel.obsDateForAPI.set(TimeUtils.DOB_DATE_FORMAT(selectedDate))
            }, date.year, date.monthValue - 1, date.dayOfMonth)
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.apply {
                    viewModel.obsAttachment.set(Parcels.unwrap(this.extras!!.getParcelable(
                        AttachmentEntity::class.java.simpleName)))
                    Toast.makeText(requireActivity(), "Captured successfully", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
}