package com.sisindia.ai.android.features.uar.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentAddSiteRiskBinding

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
class AddSiteAtRiskFragment : IopsBaseFragment() {

    private lateinit var viewModel: AddPoaAndIpViewModel
    private lateinit var binding: FragmentAddSiteRiskBinding

    companion object {
        fun newInstance(): AddSiteAtRiskFragment = AddSiteAtRiskFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_site_risk
    }

    override fun initViewModel() {
//        viewModel = getAndroidViewModel(AddPoaAndIpViewModel::class.java) as AddPoaAndIpViewModel
        viewModel = requireActivity().run {
            ViewModelProvider(this)[AddPoaAndIpViewModel::class.java]
        }
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
        /*liveData.observe(this) { message: Message ->
            when (message.what) {
            }
        }*/
    }

    override fun onCreated() {
        viewModel.initSiteAtRiskUI()
    }
}