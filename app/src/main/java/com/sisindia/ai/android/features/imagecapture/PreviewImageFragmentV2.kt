package com.sisindia.ai.android.features.imagecapture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentPreviewImageBinding

class PreviewImageFragmentV2 : IopsBaseFragment() {

    lateinit var viewModel: CaptureImageViewModel
    lateinit var binding: FragmentPreviewImageBinding

    override fun extractBundle() {}

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[CaptureImageViewModel::class.java]
    }


    override fun initViewBinding(inflater: LayoutInflater,
                                 container: ViewGroup): View {
        binding = bindFragmentView(layoutResource, inflater, container) as FragmentPreviewImageBinding
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewState() {}

    override fun onCreated() {
        viewModel.validateHumanBodyViaMLKit()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_preview_image
    }

    companion object {
        @JvmStatic
        fun newInstance(): PreviewImageFragmentV2 {
            return PreviewImageFragmentV2()
        }
    }
}