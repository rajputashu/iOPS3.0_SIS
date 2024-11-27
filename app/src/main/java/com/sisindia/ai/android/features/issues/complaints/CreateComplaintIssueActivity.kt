package com.sisindia.ai.android.features.issues.complaints

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.lifecycle.Observer
import com.droidcommons.permissions.RunTimePermissions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingBottomSheetFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityCreateComplaintBinding
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.AddClientsBottomSheet
import com.sisindia.ai.android.utils.toast

class CreateComplaintIssueActivity : IopsBaseActivity() {

    lateinit var viewModel: CreateComplaintIssueViewModel
    lateinit var binding: ActivityCreateComplaintBinding

    override fun extractBundle() {}

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.ON_ADD_AUDIO_CLIP_CLICK -> openAddAudioClipBottomSheet()
                    NavigationConstants.ON_AUDIO_RECORDED_FOR_GRIEVANCE -> {
                        val audioFile = message.obj as String
                        viewModel.onAudioRecorded(audioFile)
                    }

                    NavigationConstants.OPEN_ADD_CLIENT_BOTTOM_SHEET -> {
                        openAddClientsBottomSheet()
                    }

                    NavigationConstants.ON_CLIENT_ADDED_SUCCESSFULLY -> {
                        toast("Client added successfully")
                        viewModel.getAllSiteClients()
                    }

                    NavigationConstants.ON_CLIENT_COMPLAINT_ADDED -> {
                        openAddedComplaintDialog(message.arg1)
                    }

                    NavigationConstants.ON_COMPLAINT_ADDED_CONTINUE -> {
                        onAddedComplaintContinue()
                    }
                }
            })
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

    private fun onAddedComplaintContinue() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun openAddedComplaintDialog(complaintId: Int) {
        if (supportFragmentManager.findFragmentByTag(AddedComplaintDialogFragment::class.java.simpleName) == null) {
            val fragment =
                AddedComplaintDialogFragment.newInstance(complaintId)
            fragment.isCancelable = false
            fragment.show(supportFragmentManager,
                AddedComplaintDialogFragment::class.java.simpleName)
        }
    }


    private fun openAddAudioClipBottomSheet() {
        if (!RunTimePermissions.checkAudioRecordPermissions(this)) {
            requestPermissions(RunTimePermissions.getAudioRecordPermissions(),
                RunTimePermissions.RC_AUDIO_RECORD)
            return
        }
        if (supportFragmentManager.findFragmentByTag(
                AudioRecordingBottomSheetFragment::class.java.simpleName) == null) {
            val fragment: BottomSheetDialogFragment =
                AudioRecordingBottomSheetFragment.newInstance()
            fragment.isCancelable = false
            fragment.show(supportFragmentManager,
                AudioRecordingBottomSheetFragment::class.java.simpleName)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RunTimePermissions.RC_AUDIO_RECORD -> {

                if (RunTimePermissions.checkAudioRecordPermissions(this)) {
                    openAddAudioClipBottomSheet()
                } else {
                    toast("Please enable permissions from app settings..")
                }
            }
        }
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbAddNewComplaint)
        viewModel.initViewModel()
    }

    private fun openAddClientsBottomSheet() {
        if (supportFragmentManager.findFragmentByTag(AddClientsBottomSheet::class.java.simpleName) == null) {
            AddClientsBottomSheet.newInstance().show(supportFragmentManager,
                AddClientsBottomSheet::class.java.simpleName)
        }
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityCreateComplaintBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(CreateComplaintIssueViewModel::class.java) as CreateComplaintIssueViewModel
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun getLayoutResource(): Int = R.layout.activity_create_complaint

    companion object {
        fun newIntent(activity: Activity?): Intent {
            return Intent(activity, CreateComplaintIssueActivity::class.java)
        }
    }
}