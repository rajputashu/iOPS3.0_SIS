package com.sisindia.ai.android.features.billcollection

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.droidcommons.base.timer.CountUpTimer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityBillCollectionBinding
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.uimodels.collection.CollectionCardDetailsMO
import com.sisindia.ai.android.utils.TimeUtils
import org.parceler.Parcels
import org.threeten.bp.LocalDate

/**
 * Created by Ashu Rajput on 3/13/2020.
 */
class BillCollectionActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityBillCollectionBinding
    private var viewModel: BillCollectionViewModel? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_bill_collection
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
            timer.start()
            fetchDueCollectionViaSiteId()
        }
    }

    override fun extractBundle() {
        val collectionMO =
            intent.extras!!.getParcelable<CollectionCardDetailsMO>(IntentConstants.DUE_BILL_COLLECTION)
        if (collectionMO != null) {
            viewModel!!.apply {
                unitName.set(collectionMO.siteName)
                dateOfCollection.set(TimeUtils.COLLECTION_DATE_FORMAT(LocalDate.now()))
                obsSiteId.set(collectionMO.siteId.toInt())
//                totalBillAmount.set("₹" + collectionMO.)
            }
        }
    }

    override fun initViewState() {
        liveTimerEvent.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    CountUpTimer.REVIEW_INFORMATION_TIME_SPENT -> bindReviewInformationTime(message.arg1)
                }
            })

        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.ON_COMPLETE_BILL_COLLECTION -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    NavigationConstants.ON_TAKE_PICTURE -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithAttachment(this,
                            viewModel!!.chequeAttachmentEntity.get()),
                            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)
                    }
                }
            })
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK && data != null && data.extras!!.containsKey(
                    AttachmentEntity::class.java.simpleName)) {
                showToast("Photo captured successfully...")
                viewModel!!.apply {
                    chequeAttachmentEntity.set(Parcels.unwrap<AttachmentEntity>(data.extras!!.getParcelable(
                        AttachmentEntity::class.java.simpleName)))
                    chequeImageUri.set(Uri.parse(chequeAttachmentEntity.get()!!.localFilePath))
                }
            } else
                Toast.makeText(this, "Unable to Capture Image", Toast.LENGTH_SHORT).show()
        }
    }
}