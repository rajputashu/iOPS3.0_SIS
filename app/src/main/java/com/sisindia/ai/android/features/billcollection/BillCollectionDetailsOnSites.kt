package com.sisindia.ai.android.features.billcollection

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityBillCollectionOnsiteBinding
import com.sisindia.ai.android.uimodels.collection.CollectionCardDetailsMO

/**
 * Created by Ashu Rajput on 5/21/2020.
 */
class BillCollectionDetailsOnSites : IopsBaseActivity() {

    private lateinit var binding: ActivityBillCollectionOnsiteBinding
    private var viewModel: BillCollectionViewModel? = null
    private var isAnythingUpdated: Boolean = false

    override fun getLayoutResource(): Int {
        return R.layout.activity_bill_collection_onsite
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BillCollectionViewModel::class.java)
                as BillCollectionViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbBillCollection)
        viewModel!!.apply {
            fetchCollectionAtSiteFromDB()
        }
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_BILL_COLLECTION_TASK_FROM_CARD ->
                        if (message.obj is CollectionCardDetailsMO) // Added this after seeing in crashlytics {can remove}
                            openBillCollectionActivity(message.obj as CollectionCardDetailsMO)
                }
            })
    }

    private fun openBillCollectionActivity(collectionCardDetailsMO: CollectionCardDetailsMO) {
        startActivityForResult(Intent(this, BillCollectionActivity::class.java)
            .putExtra(IntentConstants.DUE_BILL_COLLECTION, collectionCardDetailsMO),
            IntentRequestCodes.REQUEST_CODE_OPEN_BILL_COLLECTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentRequestCodes.REQUEST_CODE_OPEN_BILL_COLLECTION && resultCode == Activity.RESULT_OK) {
            viewModel!!.fetchCollectionAtSiteFromDB()
            isAnythingUpdated = true
        }
    }

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