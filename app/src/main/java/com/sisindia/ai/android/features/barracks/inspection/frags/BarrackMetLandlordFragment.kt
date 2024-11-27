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
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentBarrackMetlandlordBinding
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity

/**
 * Created by Ashu Rajput on 4/21/2020.
 */
class BarrackMetLandlordFragment : IopsBaseFragment() {

    private lateinit var viewModel: BarrackMetLandlordViewModel
    private lateinit var binding: FragmentBarrackMetlandlordBinding

    companion object {
        fun newInstance(): BarrackMetLandlordFragment = BarrackMetLandlordFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_barrack_metlandlord
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackMetLandlordViewModel::class.java)
                as BarrackMetLandlordViewModel
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
                    NavigationConstants.ON_TAKING_LANDLORD_PIC -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                            CaptureImageType.LANDLORD),
                            IntentRequestCodes.REQUEST_CODE_PHOTO_LANDLORD)
                    }
                    NavigationConstants.ON_TAKING_CUSTODIAN_PIC -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                            CaptureImageType.CUSTODIAN),
                            IntentRequestCodes.REQUEST_CODE_PHOTO_CUSTODIAN)
                    }
                }
            })
    }

    override fun onCreated() {
        viewModel.apply {
            fetchCacheBI()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            IntentRequestCodes.REQUEST_CODE_PHOTO_LANDLORD -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.extras!!.apply {
                    viewModel.apply {
                        isLandlordPhotoNewOrUpdated.set(true)
                        photoWithLandlordUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        landlordPhotoMetadata.set(getString(IntentConstants.FILE_METADATA))
//                viewModel.photoWithLandlordUri.set(data.data)
                    }
                }
            }
            IntentRequestCodes.REQUEST_CODE_PHOTO_CUSTODIAN -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.extras!!.apply {
                    viewModel.apply {
                        isCustodianPhotoNewOrUpdated.set(true)
                        photoWithCustodianUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        custodianPhotoMetadata.set(getString(IntentConstants.FILE_METADATA))
                    }
                }
            }
        }
    }
}