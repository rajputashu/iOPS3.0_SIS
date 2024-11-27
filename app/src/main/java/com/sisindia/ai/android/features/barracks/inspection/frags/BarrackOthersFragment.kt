package com.sisindia.ai.android.features.barracks.inspection.frags

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.commons.CaptureImageType
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes.*
import com.sisindia.ai.android.constants.NavigationConstants.*
import com.sisindia.ai.android.databinding.FragmentBarrackOthersBinding
import com.sisindia.ai.android.features.issues.complaints.CreateComplaintIssueActivity
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity

/**
 * Created by Ashu Rajput on 4/21/2020.
 */
class BarrackOthersFragment : IopsBaseFragment() {

    private lateinit var viewModel: BarrackOthersViewModel
    private lateinit var binding: FragmentBarrackOthersBinding

    companion object {
        fun newInstance(): BarrackOthersFragment = BarrackOthersFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_barrack_others
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackOthersViewModel::class.java)
                as BarrackOthersViewModel
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
                    ON_TAKING_BARRACK_BEDDING_PIC -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                            CaptureImageType.BARRACK_BED), REQUEST_CODE_BARRACK_BED)
                    }
                    ON_TAKING_BARRACK_KIT_PIC -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                            CaptureImageType.BARRACK_KIT), REQUEST_CODE_BARRACK_KIT)
                    }
                    ON_TAKING_BARRACK_MESS_PIC -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                            CaptureImageType.BARRACK_MESS), REQUEST_CODE_BARRACK_MESS)
                    }
                    ON_TAKING_BARRACK_OUTSIDE_PIC -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                            CaptureImageType.BARRACK_OUTSIDE), REQUEST_CODE_BARRACK_OUTSIDE)
                    }
                    ON_BARRACKS_COMPLAINT -> openAddComplaint()
                }
            })
    }

    override fun onCreated() {
        viewModel.apply {
            fetchCacheBI()
        }
    }

    private fun openAddComplaint() {
        startActivityForResult(CreateComplaintIssueActivity.newIntent(requireActivity()),
            REQUEST_CODE_CREATE_COMPLAINT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_BARRACK_BED -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.extras!!.apply {
                    viewModel.apply {
                        isBedPhotoNewOrUpdated.set(true)
                        barrackBedPhotoUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        barrackBedPhotoMetadata.set(getString(IntentConstants.FILE_METADATA))
                    }
                }
//                viewModel.barrackBedPhotoUri.set(data.data)
            }
            REQUEST_CODE_BARRACK_KIT -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.extras!!.apply {
                    viewModel.apply {
                        isKitPhotoNewOrUpdated.set(true)
                        barrackKitUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        barrackKitPhotoMetadata.set(getString(IntentConstants.FILE_METADATA))
                    }
                }
            }
            REQUEST_CODE_BARRACK_MESS -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.extras!!.apply {
                    viewModel.apply {
                        isMessPhotoNewOrUpdated.set(true)
                        barrackMessUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        barrackMessPhotoMetadata.set(getString(IntentConstants.FILE_METADATA))
                    }
                }
            }
            REQUEST_CODE_BARRACK_OUTSIDE -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.extras!!.apply {
                    viewModel.apply {
                        isOutsidePhotoNewOrUpdated.set(true)
                        barrackOutsideUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        barrackOutsidePhotoMetadata.set(getString(IntentConstants.FILE_METADATA))
                    }
                }
            }
            REQUEST_CODE_CREATE_COMPLAINT -> if (resultCode == Activity.RESULT_OK) {
                viewModel.fetchAddedComplaintsFromDB()
            }
        }
    }
}