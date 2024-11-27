package com.sisindia.ai.android.features.taskcheck.clienthandshake.frags

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.droidcommons.permissions.RunTimePermissions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingBottomSheetFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentClientHandShakeAddComplaintBinding
import com.sisindia.ai.android.features.issues.complaints.AddedComplaintDialogFragment
import com.sisindia.ai.android.utils.toast

/**
 * Created by Ashu Rajput on 4/9/2020.
 */
class ClientHandShakeAddComplaintFragment : IopsBaseFragment() {

    private lateinit var viewModel: ClientHandShakeAddComplaintViewModel
    private lateinit var binding: FragmentClientHandShakeAddComplaintBinding

    companion object {
        fun newInstance(): ClientHandShakeAddComplaintFragment =
            ClientHandShakeAddComplaintFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_client_hand_shake_add_complaint
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ClientHandShakeAddComplaintViewModel::class.java)
                as ClientHandShakeAddComplaintViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
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
                    NavigationConstants.ON_ADD_AUDIO_CLIP_CLICK -> openAddAudioClipBottomSheet()
                    NavigationConstants.ON_AUDIO_RECORDED_FOR_GRIEVANCE -> {
                        val audioFile = message.obj as String
                        viewModel.onAudioRecorded(audioFile)
                    }
                    NavigationConstants.ON_CLIENT_COMPLAINT_ADDED -> {
                        openAddedComplaintDialog(message.arg1)
                    }
                }
            })
    }

    private fun openAddedComplaintDialog(complaintId: Int) {
        if (childFragmentManager.findFragmentByTag(AddedComplaintDialogFragment::class.java.simpleName) == null) {
            val fragment =
                AddedComplaintDialogFragment.newInstance(complaintId)
            fragment.isCancelable = false
            fragment.show(childFragmentManager,
                AddedComplaintDialogFragment::class.java.simpleName)
        }
    }


    private fun openAddAudioClipBottomSheet() {
        if (!RunTimePermissions.checkAudioRecordPermissions(requireActivity())) {
            requestPermissions(RunTimePermissions.getAudioRecordPermissions(),
                RunTimePermissions.RC_AUDIO_RECORD)
            return
        }
        if (childFragmentManager.findFragmentByTag(
                AudioRecordingBottomSheetFragment::class.java.simpleName) == null) {
            val fragment: BottomSheetDialogFragment =
                AudioRecordingBottomSheetFragment.newInstance()
            fragment.isCancelable = false
            fragment.show(childFragmentManager,
                AudioRecordingBottomSheetFragment::class.java.simpleName)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RunTimePermissions.RC_AUDIO_RECORD -> {

                if (RunTimePermissions.checkAudioRecordPermissions(requireActivity())) {
                    openAddAudioClipBottomSheet()
                } else {
                    toast("Please enable permissions from app settings..")
                }
            }
        }
    }

    override fun onCreated() {
        viewModel.initViewModel()
    }
}