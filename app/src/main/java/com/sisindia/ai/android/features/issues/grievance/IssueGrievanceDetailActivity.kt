package com.sisindia.ai.android.features.issues.grievance

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.NavigationConstants.ON_GRIEVANCE_ADDED_CONTINUE
import com.sisindia.ai.android.databinding.ActivityIssueGrievanceDetailBinding

class IssueGrievanceDetailActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityIssueGrievanceDetailBinding
    private lateinit var viewModel: IssueGrievanceViewDetail

    companion object {
        fun newIntent(activity: Activity, grievanceId: Int): Intent {
            val intent = Intent(activity, IssueGrievanceDetailActivity::class.java)
            intent.putExtra(IssueGrievanceDetailActivity::class.java.simpleName, grievanceId)
            return intent
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun extractBundle() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(IssueGrievanceDetailActivity::class.java.simpleName)) {
            val grievanceId = bundle.getInt(IssueGrievanceDetailActivity::class.java.simpleName)
            viewModel.fetchGrievanceDetail(grievanceId)
        }
    }

    override fun initViewState() {
        liveData.observe(this, Observer {
            when (it.what) {
                NavigationConstants.ON_GRIEVANCE_CLOSED -> {
                    openGrievanceOnClosed(it.arg1)
                }
                ON_GRIEVANCE_ADDED_CONTINUE -> {
                    onGrievanceContinue()
                }
            }
        })
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }


    private fun onGrievanceContinue() {
        setResult(Activity.RESULT_OK)
        this.finish()
    }

    private fun openGrievanceOnClosed(grievanceId: Int) {
        if (supportFragmentManager.findFragmentByTag(ClosedGrievanceDialogFragment::class.java.simpleName) == null) {
            val fragment =
                ClosedGrievanceDialogFragment.newInstance(grievanceId)
            fragment.isCancelable = false
            fragment.show(supportFragmentManager,
                ClosedGrievanceDialogFragment::class.java.simpleName)
        }
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbGrievanceDetail)
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityIssueGrievanceDetailBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(IssueGrievanceViewDetail::class.java) as IssueGrievanceViewDetail
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_issue_grievance_detail
    }
}