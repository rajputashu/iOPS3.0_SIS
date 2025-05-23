package com.sisindia.ai.android.features.uar.closepoa

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Color
import android.os.Message
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.CaptureImageType
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.databinding.ActivityClosePoaBinding
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity
import com.sisindia.ai.android.features.uar.dialog.POADialogFragment
import com.sisindia.ai.android.uimodels.uarpoa.POADetailsMO
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate

/**
 * Created by Ashu Rajput on 4/4/2020.
 */
class ClosePOAActivity : IopsBaseActivity() {

    private var viewModel: ClosePOAViewModel? = null
    private lateinit var binding: ActivityClosePoaBinding
    private lateinit var poaMO: POADetailsMO

    override fun getLayoutResource(): Int {
        return R.layout.activity_close_poa
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ClosePOAViewModel::class.java) as ClosePOAViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.poaMO = poaMO
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbPOAUnitsName)
        viewModel!!.apply {
            poaId.set(poaMO.poaId)
        }
    }

    override fun extractBundle() {
        poaMO = intent.getParcelableExtra(IntentConstants.POA_DATA)!!
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_DASH_BOARD -> {
                    }
                    NavigationConstants.OPEN_DATE_PICKER -> {
                        openDatePicker()
                    }
                    NavigationConstants.OPEN_POA_CONFIRM_BY_PHOTO -> {
                        openPhotoCapture()
                    }
                    NavigationConstants.ON_POA_INSERTED_SUCCESSFULLY -> {
                        openClosedPOADialog()
                    }
                    NavigationConstants.ON_VIEW_ALL_POA -> {
                        message.what = NavigationConstants.ON_VIEW_ALL_POA
                        liveData.postValue(message)
                        finish()
                    }
                }
            })
    }

    private fun openDatePicker() {
        val date = LocalDate.now()
        val datePickerDialog = DatePickerDialog(this,
            OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                viewModel!!.closeDate.set(selectedDate)
            }, date.year, date.monthValue - 1, date.dayOfMonth)
        datePickerDialog.datePicker.minDate = Instant.now().toEpochMilli()
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
    }

    private fun openPhotoCapture() {
        Prefs.putInt(PrefConstants.POA_ID_FOR_ATTACHMENT, poaMO.poaId)
        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
            CaptureImageType.CLOSE_POA), IntentRequestCodes.REQUEST_CODE_POA_CONFIRM_PHOTO)
    }

    private fun openClosedPOADialog() {
        viewModel!!.apply {
            var pendingPOALeft = 0
            var totalPOALeft = 0
            if (poaMO.pendingPoaCount!!.isNotEmpty()) {
                pendingPOALeft = (poaMO.pendingPoaCount)!!.toInt()
                if (pendingPOALeft > 0)
                    pendingPOALeft -= 1
            }
            if (poaMO.totalPoaCount!!.isNotEmpty()) {
                totalPOALeft = (poaMO.totalPoaCount)!!.toInt()
                if (totalPOALeft > 0)
                    totalPOALeft -= 1
            }
            poaMO.pendingPoaCount = pendingPOALeft.toString()
            poaMO.totalPoaCount = totalPOALeft.toString()

            val dialog = POADialogFragment.newInstance(poaMO)
            dialog.show(supportFragmentManager, POADialogFragment::class.java.simpleName)
            dialog.initDialogListener(listener)
            dialog.isCancelable = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == Activity.RESULT_OK) {
            showToast("Photo captured successfully...")
            /*data.extras!!.apply {
                viewModel!!.apply {
                    photoImageUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                    closePoaImageMetaData.set(getString(IntentConstants.FILE_METADATA))
                }
            }*/
            viewModel!!.photoImageUri.set(data.data)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}