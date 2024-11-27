package com.sisindia.ai.android.features.practo

import android.app.Activity
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.NavigationConstants.ON_PRACTO_QUESTION_COMPLETED
import com.sisindia.ai.android.databinding.BottomSheetPractoQuestionsBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2
import com.sisindia.ai.android.room.entities.AttachmentEntity
import org.parceler.Parcels

/**
 * Created by Ashu Rajput on 13-07-2023
 */
class PractoQuestionsBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetPractoQuestionsBinding
    lateinit var viewModel: PractoSheetViewModel

    companion object {
        fun newInstance() = PractoQuestionsBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_practo_questions
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetPractoQuestionsBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_CAPTURE_APP_INSTALL_IMAGE -> {
                    val intent = CaptureImageActivityV2.newIntent(requireActivity(),
                        viewModel.appInstalledAttachment.get()!!)
                    resultLauncher.launch(intent)
                }

                NavigationConstants.ON_CAPTURE_APP_NOT_INSTALL_IMAGE -> {
                    val intent = CaptureImageActivityV2.newIntent(requireActivity(),
                        viewModel.appNotInstalledAttachment.get()!!)
                    resultLauncherNotInstall.launch(intent)
                }

                ON_PRACTO_QUESTION_COMPLETED -> {
                    message.what = ON_PRACTO_QUESTION_COMPLETED
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }

                NavigationConstants.ON_CLOSE_SCREEN -> dismissAllowingStateLoss()
            }
        }
    }

    override fun onCreated() {
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(PractoSheetViewModel::class.java)
                as PractoSheetViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.apply {
                    viewModel.appInstalledAttachment.set(Parcels.unwrap(this.extras!!.getParcelable(
                        AttachmentEntity::class.java.simpleName)))
                    showMessage()
                }
            }
        }

    private var resultLauncherNotInstall =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.apply {
                    viewModel.appNotInstalledAttachment.set(Parcels.unwrap(this.extras!!.getParcelable(
                        AttachmentEntity::class.java.simpleName)))
                    showMessage()
                }
            }
        }

    private fun showMessage() {
        Toast.makeText(requireActivity(), "Captured successfully", Toast.LENGTH_LONG).show()
    }
}