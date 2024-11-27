package com.sisindia.ai.android.features.improvementplans

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityImprovePlansListBinding
import com.sisindia.ai.android.uimodels.uarpoa.IPPoaPendingCompletedMO
import com.sisindia.ai.android.uimodels.uarpoa.SitesWithImprovePlansMO

/**
 * Created by Ashu Rajput on 12/21/2020.
 */
class ImprovementPoaListActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityImprovePlansListBinding
    private var viewModel: ImprovePoaListViewModel? = null
    private lateinit var poaHeaderDetails: SitesWithImprovePlansMO
    private var isSelectedPOAClosed = false

    override fun getLayoutResource(): Int {
        return R.layout.activity_improve_plans_list
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ImprovePoaListViewModel::class.java) as ImprovePoaListViewModel
    }

    override fun extractBundle() {
        poaHeaderDetails = intent.getParcelableExtra(IntentConstants.IP_POA)!!
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbPOAUnitsName)
        viewModel!!.apply {
            selectedSiteId.set(poaHeaderDetails.siteId)
            //            updatePoaCountsCardAndList(poaHeaderDetails)
            getUpdatedPOACountAfterClose()
        }
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_CLOSE_IP_POA_SCREEN -> {
                        val poaDataToClose = message.obj as IPPoaPendingCompletedMO
                        //                        poaDataToClose.siteName = poaHeaderDetails.siteName
                        //                        poaDataToClose. = poaHeaderDetails.pendingIP
                        //                        poaDataToClose.totalIP = poaHeaderDetails.totalIP
                        startActivity(Intent(this@ImprovementPoaListActivity, CloseImprovePlansActivity::class.java)
                            .putExtra(IntentConstants.POA_DATA, poaDataToClose))
                    }
                    NavigationConstants.ON_VIEW_ALL_IP_POA -> {
                        isSelectedPOAClosed = true
                        viewModel!!.apply {
                            getUpdatedPOACountAfterClose()
                            //                            updatePoaCountsCardAndList(poaHeaderDetails)
                        }
                    }
                }
            })
    }

    override fun onBackPressed() {
        if (isSelectedPOAClosed) {
            setResult(Activity.RESULT_OK)
            finish()
        } else
            super.onBackPressed()
    }
}