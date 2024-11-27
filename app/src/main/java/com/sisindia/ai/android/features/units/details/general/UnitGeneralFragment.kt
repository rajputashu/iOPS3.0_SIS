package com.sisindia.ai.android.features.units.details.general

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentUnitGeneralBinding

/**
 * Created by Ashu Rajput on 3/20/2020.
 */
class UnitGeneralFragment : IopsBaseFragment() {

    private var viewModel: UnitGeneralViewModel? = null
    lateinit var binding: FragmentUnitGeneralBinding

    companion object {
        private const val TAB_TYPE = "ViewPagerTabType"
        fun newInstance(tabType: Int) = UnitGeneralFragment().apply {
            arguments = Bundle().apply { putInt(TAB_TYPE, tabType) }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_unit_general
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(UnitGeneralViewModel::class.java) as UnitGeneralViewModel
    }

    override fun onCreated() {
        viewModel!!.apply {
            fetchClientDetails()
            fetchGeneralSiteDetails()
        }
        binding.mapNavigationIcon.setOnClickListener { openGoogleNavigation() }
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!,
            layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    private fun openGoogleNavigation() {
        //        val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
        //        val gmmIntentUri = Uri.parse("google.navigation:q=${viewModel!!.siteAddressUI.get()}")
        val gmmIntentUri = Uri.parse("google.navigation:q=${viewModel!!.obsSiteGeoCode.get().toString()}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        }
    }
}