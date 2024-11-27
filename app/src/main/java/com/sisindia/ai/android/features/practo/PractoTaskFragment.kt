package com.sisindia.ai.android.features.practo

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentPractoTaskBinding

/**
 * Created by Ashu Rajput on 11-07-2023
 */
class PractoTaskFragment : IopsBaseFragment() {

    private lateinit var viewModel: PractoTaskViewModel
    private lateinit var binding: FragmentPractoTaskBinding

    companion object {
        fun newInstance(): PractoTaskFragment = PractoTaskFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_practo_task
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(PractoTaskViewModel::class.java) as PractoTaskViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {

            }
        }
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {

    }
}