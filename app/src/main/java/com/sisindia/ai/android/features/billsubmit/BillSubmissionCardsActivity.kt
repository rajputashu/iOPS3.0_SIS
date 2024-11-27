package com.sisindia.ai.android.features.billsubmit

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityBillSubmissionCardsBinding
import com.sisindia.ai.android.uimodels.billsubmit.BillSubmissionCardDetailsMO
import kotlinx.coroutines.*

/**
 * Created by Ashu Rajput on 6/1/2020.
 */
class BillSubmissionCardsActivity : IopsBaseActivity() {

    private lateinit var viewModel: BillSubmissionViewModel
    private lateinit var binding: ActivityBillSubmissionCardsBinding
    private var isAnythingUpdated: Boolean = false

    override fun getLayoutResource(): Int {
        return R.layout.activity_bill_submission_cards
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(BillSubmissionViewModel::class.java) as BillSubmissionViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbBillSubmission)
        viewModel.getBillCardsDetails()
        binding.getBillSubmissionTask.setOnClickListener {
            //            showWeekSelectionPopup(it)
            viewModel.getBillSubmissionTasksFromServer()
        }
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_BILL_SUBMISSION_TASK_FROM_CARD -> openBillSubmissionTask(
                    message.obj as BillSubmissionCardDetailsMO)
                NavigationConstants.ON_UPDATING_BS_UI -> updateBillSubmissionUI()
            }
        }
    }

    private fun openBillSubmissionTask(model: BillSubmissionCardDetailsMO) {
        startActivityForResult(Intent(this, BillSubmissionActivity::class.java)
            .putExtra("SITE_NAME_KEY", model.siteName),
            IntentRequestCodes.REQUEST_CODE_OPEN_BILL_SUBMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentRequestCodes.REQUEST_CODE_OPEN_BILL_SUBMISSION && resultCode == Activity.RESULT_OK)
            updateBillSubmissionUI()
    }

    private fun updateBillSubmissionUI() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            withContext(Dispatchers.Main) {
                viewModel.getBillCardsDetails()
                isAnythingUpdated = true
            }
        }
    }

    /*private fun showWeekSelectionPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.toolbar_menu_bs_weeks)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.currentWeek -> viewModel.getBillSubmissionTasksFromServer("CURRENT_WEEK")
                R.id.lastWeek -> viewModel.getBillSubmissionTasksFromServer("LAST_WEEK")
            }
            true
        }
        popup.show()
    }*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (isAnythingUpdated) {
            setResult(RESULT_OK)
            finish()
        } else
            super.onBackPressed()
    }
}