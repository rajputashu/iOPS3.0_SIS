package com.sisindia.ai.android.features.uar

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentUarPoaBinding
import com.sisindia.ai.android.features.uar.poa.POAActivity
import com.sisindia.ai.android.uimodels.uarpoa.AtRiskAndPoaDetailsMO

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
class UnitAtRiskFragment : IopsBaseFragment() {

    private lateinit var viewModel: UnitAtRiskViewModel
    private lateinit var binding: FragmentUarPoaBinding

    companion object {
        fun newInstance(): UnitAtRiskFragment = UnitAtRiskFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_uar_poa
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(UnitAtRiskViewModel::class.java) as UnitAtRiskViewModel
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
                    NavigationConstants.OPEN_POA_SCREEN -> openPOAScreen(message.obj as AtRiskAndPoaDetailsMO)
                }
            })
    }

    override fun onCreated() {
        updateUarUI()
    }

    private fun updateUarUI() {
        viewModel.apply {
            getHeaderUnitsAndPOAsCount()
            getDueDatesFromDB()
        }
    }

    private fun openPOAScreen(poaDetailMO: AtRiskAndPoaDetailsMO) {
        startActivityForResult(Intent(activity, POAActivity::class.java)
            .putExtra(IntentConstants.UAR_POA_DATA, poaDetailMO),
            IntentRequestCodes.REQUEST_OPEN_POA_SCREEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentRequestCodes.REQUEST_OPEN_POA_SCREEN && resultCode == Activity.RESULT_OK)
            updateUarUI()
    }

    fun refreshUarUI() {
        viewModel.onPOAsSyncClick()
    }
}