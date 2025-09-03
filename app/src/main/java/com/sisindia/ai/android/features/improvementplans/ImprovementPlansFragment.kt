package com.sisindia.ai.android.features.improvementplans

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentImprovementPlansBinding
import com.sisindia.ai.android.features.uar.add.AddRiskPoaAndIpActivity
import com.sisindia.ai.android.uimodels.uarpoa.SitesWithImprovePlansMO

/**
 * Created by Ashu Rajput on 12/14/2020.
 */
class ImprovementPlansFragment : IopsBaseFragment() {

    private lateinit var viewModel: ImprovementPlansViewModel
    private lateinit var binding: FragmentImprovementPlansBinding

    companion object {
        fun newInstance(): ImprovementPlansFragment = ImprovementPlansFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_improvement_plans
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(ImprovementPlansViewModel::class.java) as ImprovementPlansViewModel
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
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_IMPROVEMENT_PLAN_POA -> openPOAScreen(message.obj as SitesWithImprovePlansMO)

                NavigationConstants.ON_ADDING_NEW_IMPROVE_PLAN -> {
                    commonLauncher.launch(Intent(activity, AddRiskPoaAndIpActivity::class.java)
                        .putExtra(IntentConstants.IS_ADDING_POA, false))
                }
            }
        }
    }

    override fun onCreated() {
        updateUarUI()
    }

    private fun updateUarUI() {
        viewModel.apply {
            getHeaderUnitsAndPOAsCount()
            //            getDueDatesFromDB()
            getSitesWithImprovementPlans()
        }
    }

    private fun openPOAScreen(sitesWithPlansMO: SitesWithImprovePlansMO) {
        startActivityForResult(Intent(activity, ImprovementPoaListActivity::class.java)
            .putExtra(IntentConstants.IP_POA, sitesWithPlansMO),
            IntentRequestCodes.REQUEST_OPEN_POA_SCREEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentRequestCodes.REQUEST_OPEN_POA_SCREEN && resultCode == Activity.RESULT_OK)
            updateUarUI()
    }

    fun refreshImprovementPlanUI() {
        viewModel.getImprovementPlans()

        /*viewModel.isLoading.set(View.VISIBLE)
        val inputData = Data.Builder().putInt(SyncPoaWorker::class.java.simpleName, SyncPOA.IMPROVEMENT_PLAN.typeId).build()
        val request = OneTimeWorkRequest.Builder(SyncPoaWorker::class.java).setInputData(inputData).build()
        WorkManager.getInstance(requireContext()).enqueueUniqueWork("SyncPOAWorker", ExistingWorkPolicy.KEEP, request)
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(request.id).observe(this, Observer {
            if (it?.state?.isFinished!!) {
                Timber.e("SYNC POA is finished................")
                it?.state?.name?.let { status ->
                    if (status.equals("SUCCEEDED", ignoreCase = true)) {
                        Timber.e("Data Synced and coming to close the loader")
                        viewModel.isLoading.set(View.GONE)
                    }
                }
            } else {
                Timber.e("SYNC POA is not finished yet")
            }
        })*/
    }


    private var commonLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK)
//                viewModel.initPoaDashboard()
                updateUarUI()
        }
}