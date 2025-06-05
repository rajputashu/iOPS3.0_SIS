package com.sisindia.ai.android.features.civil

import android.app.Activity
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetAddNominationBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2.Companion.newIntent
import com.sisindia.ai.android.room.entities.AttachmentEntity
import org.parceler.Parcels.unwrap

/**
 * Created by Ashu Rajput on 5/06/2025.
 */
class AddCivilNominationBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddNominationBinding
    lateinit var viewModel: CivilDefenceViewModel

    companion object {
        fun newInstance() = AddCivilNominationBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_add_nomination
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater,
            container) as BottomSheetAddNominationBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_TAKE_PICTURE -> {
                    val intent = newIntent(requireActivity(), message.obj as AttachmentEntity)
                    nominationPicLauncher.launch(intent)
                }
                NavigationConstants.ON_SUCCESSFUL_ADD_NOMINATION -> {
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    override fun onCreated() {
        viewModel.initAddNominationUI()
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(CivilDefenceViewModel::class.java) as CivilDefenceViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private var nominationPicLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.extras.apply {
                    if (this!!.containsKey(AttachmentEntity::class.java.simpleName)) {
                        viewModel.apply {
                            photoAttachmentObs.set(unwrap(getParcelable(AttachmentEntity::class.java.simpleName)))
                        }
                    }
                }
            }
        }
}