package com.sisindia.ai.android.features.issues.grievance

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import com.droidcommons.permissions.RunTimePermissions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingBottomSheetFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_GUARD_SCAN
import com.sisindia.ai.android.constants.NavigationConstants.*
import com.sisindia.ai.android.databinding.ActivityCreateGrievanceBinding
import com.sisindia.ai.android.features.addgrievances.AddedGrievanceDialogFragment
import com.sisindia.ai.android.mlcore.ScanQRActivity

class CreateGrievanceIssueActivity : IopsBaseActivity() {

    private lateinit var viewModel: CreateGrievanceIssueViewModel
    private lateinit var binding: ActivityCreateGrievanceBinding

    override fun extractBundle() {}

    companion object {
        fun newIntent(activity: Activity): Intent =
            Intent(activity, CreateGrievanceIssueActivity::class.java)
    }

    override fun initViewState() {
        liveData.observe(this, Observer {
            when (it.what) {

                ON_SCAN_GUARD_QR_CLICK -> openScanQrScreen()
                ON_ADD_AUDIO_CLIP_CLICK -> openAddAudioClipBottomSheet()

                ON_AUDIO_RECORDED_FOR_GRIEVANCE -> {
                    val audioFile: String = it.obj as String
                    viewModel.onAudioRecorded(audioFile)
                }

                ON_ISSUE_GRIEVANCE_CREATED -> openGrievanceAddedDialog(it.arg1)
                ON_GRIEVANCE_ADDED_CONTINUE -> onAddedGrievanceContinue()
            }
        })
    }

    private fun onAddedGrievanceContinue() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

    private fun openGrievanceAddedDialog(grievanceId: Int) {
        if (supportFragmentManager.findFragmentByTag(AddedGrievanceDialogFragment::class.java.simpleName) == null) {
            val fragment =
                AddedGrievanceDialogFragment.newInstance(grievanceId)
            fragment.isCancelable = false
            fragment.show(supportFragmentManager,
                AddedGrievanceDialogFragment::class.java.simpleName)
        }
    }

    private fun openAddAudioClipBottomSheet() {
        if (!RunTimePermissions.checkAudioRecordPermissions(this)) {
            requestPermissions(RunTimePermissions.getAudioRecordPermissions(), RunTimePermissions.RC_AUDIO_RECORD)
            return
        }
        if (supportFragmentManager.findFragmentByTag(AudioRecordingBottomSheetFragment::class.java.simpleName) == null) {
            val fragment: BottomSheetDialogFragment = AudioRecordingBottomSheetFragment.newInstance()
            fragment.isCancelable = false
            fragment.show(supportFragmentManager, AudioRecordingBottomSheetFragment::class.java.simpleName)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RunTimePermissions.RC_AUDIO_RECORD -> {
                if (RunTimePermissions.checkAudioRecordPermissions(this))
                    openAddAudioClipBottomSheet()
                else
                    showToast("Please enable permissions from app settings..")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun openScanQrScreen() {
        startActivityForResult(ScanQRActivity.newIntent(this),
            REQUEST_CODE_OPEN_GUARD_SCAN)
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbAddNewGrievance)
        viewModel.initViewModel()
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityCreateGrievanceBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(CreateGrievanceIssueViewModel::class.java) as CreateGrievanceIssueViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_OPEN_GUARD_SCAN -> {
                if (resultCode == Activity.RESULT_OK && data != null && data.extras != null &&
                    data.extras!!.containsKey(IntentConstants.ON_QR_SCANNED)) {
                    val qrRawData = data.extras!!.getString(IntentConstants.ON_QR_SCANNED)
                    viewModel.onGuardQrScanned(qrRawData!!)
                } else {
                    Toast.makeText(this, "Error in Qr Scan.. Please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_create_grievance
}