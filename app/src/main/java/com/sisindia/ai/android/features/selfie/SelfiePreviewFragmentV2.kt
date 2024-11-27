package com.sisindia.ai.android.features.selfie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentPreviewImageBinding
import com.sisindia.ai.android.databinding.FragmentSelfiePreviewBinding
import com.sisindia.ai.android.features.imagecapture.PreviewImageFragmentV2

/**
 * Created by Ashu Rajput on 02-07-2022
 */
class SelfiePreviewFragmentV2 : IopsBaseFragment() {

    lateinit var viewModel: SelfieViewModel
    lateinit var binding: FragmentSelfiePreviewBinding

    override fun extractBundle() {}

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity(),
            viewModelFactory).get(SelfieViewModel::class.java)
    }


    override fun initViewBinding(inflater: LayoutInflater,
        container: ViewGroup): View {
        binding =
            bindFragmentView(layoutResource, inflater, container) as FragmentSelfiePreviewBinding
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewState() {}

    override fun onCreated() {}

    override fun getLayoutResource(): Int {
        return R.layout.fragment_selfie_preview
    }

    companion object {
        @JvmStatic
        fun newInstance(): SelfiePreviewFragmentV2 {
            return SelfiePreviewFragmentV2()
        }
    }
}