package com.sisindia.ai.android.features.issues.complaints

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.NavigationConstants.ON_COMPLAINT_ADDED_CONTINUE
import com.sisindia.ai.android.databinding.ActivityIssueComplaintDetailBinding

class IssueComplaintDetailActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityIssueComplaintDetailBinding
    private lateinit var viewModel: IssueComplaintViewDetail

    companion object {
        fun newIntent(activity: Activity, complaintId: Int): Intent {
            val intent = Intent(activity, IssueComplaintDetailActivity::class.java)
            intent.putExtra(IssueComplaintDetailActivity::class.java.simpleName, complaintId)
            return intent
        }

    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

    override fun extractBundle() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(IssueComplaintDetailActivity::class.java.simpleName)) {
            val complaintId = bundle.getInt(IssueComplaintDetailActivity::class.java.simpleName)
            viewModel.fetchComplaintDetail(complaintId)
        }
    }

    override fun initViewState() {
        liveData.observe(this, Observer {
            when (it.what) {
                NavigationConstants.ON_COMPLAINT_CLOSED -> {
                    openComplaintOnClosed(it.arg1)
                }
                ON_COMPLAINT_ADDED_CONTINUE -> {
                    onComplaintContinue()
                }
            }
        })
    }

    private fun onComplaintContinue() {
        setResult(Activity.RESULT_OK)
        this.finish()
    }

    private fun openComplaintOnClosed(complaintId: Int) {
        if (supportFragmentManager.findFragmentByTag(ClosedComplaintDialogFragment::class.java.simpleName) == null) {
            val fragment =
                ClosedComplaintDialogFragment.newInstance(complaintId)
            fragment.isCancelable = false
            fragment.show(supportFragmentManager,
                ClosedComplaintDialogFragment::class.java.simpleName)
        }
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbComplaintDetail)
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityIssueComplaintDetailBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(IssueComplaintViewDetail::class.java) as IssueComplaintViewDetail
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_issue_complaint_detail
    }
}