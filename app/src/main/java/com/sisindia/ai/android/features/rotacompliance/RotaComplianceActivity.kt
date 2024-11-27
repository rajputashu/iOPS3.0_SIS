package com.sisindia.ai.android.features.rotacompliance

import android.os.Message
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityRotaComplianceCardsBinding

/**
 * Created by Ashu Rajput on 6/1/2020.
 */
class RotaComplianceActivity : IopsBaseActivity() {

    private var viewModel: RotaComplianceViewModel? = null
    private lateinit var binding: ActivityRotaComplianceCardsBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_rota_compliance_cards
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(RotaComplianceViewModel::class.java)
                as RotaComplianceViewModel
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityRotaComplianceCardsBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbRotaCompliance)
        viewModel!!.getRotaComplianceDetailsFromAPI()
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_ADD_RECRUITMENT_SHEET -> {
                    }
                }
            })
    }
}