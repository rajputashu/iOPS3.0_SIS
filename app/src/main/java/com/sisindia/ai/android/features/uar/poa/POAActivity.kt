package com.sisindia.ai.android.features.uar.poa

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityPoaBinding
import com.sisindia.ai.android.features.uar.closepoa.ClosePOAActivity
import com.sisindia.ai.android.uimodels.uarpoa.AtRiskAndPoaDetailsMO
import com.sisindia.ai.android.uimodels.uarpoa.POADetailsMO

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
class POAActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityPoaBinding
    private var viewModel: POAViewModel? = null
    private lateinit var poaHeaderDetails: AtRiskAndPoaDetailsMO
    private var isSelectedPOAClosed = false

    override fun getLayoutResource(): Int {
        return R.layout.activity_poa
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(POAViewModel::class.java) as POAViewModel
    }

    override fun extractBundle() {
        poaHeaderDetails = intent.getParcelableExtra(IntentConstants.UAR_POA_DATA)!!
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        /*if (::poaHeaderDetails.isInitialized)
            binding.model = poaHeaderDetails*/
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbPOAUnitsName)
        viewModel!!.apply {
            selectedSiteId.set(poaHeaderDetails.SiteId)
            updatePoaCountsCardAndList(poaHeaderDetails)
        }
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_CLOSE_POA_SCREEN -> {
                        val poaDataToClose = message.obj as POADetailsMO
                        poaDataToClose.UnitName = poaHeaderDetails.SiteName
                        poaDataToClose.pendingPoaCount = poaHeaderDetails.Pending
                        poaDataToClose.totalPoaCount = poaHeaderDetails.TotalPOAs
                        startActivity(Intent(this@POAActivity, ClosePOAActivity::class.java)
                            .putExtra(IntentConstants.POA_DATA, poaDataToClose))
                    }
                    NavigationConstants.ON_VIEW_ALL_POA -> {
                        isSelectedPOAClosed = true
                        viewModel!!.apply {
                            getUpdatedPOACountAfterClose()
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