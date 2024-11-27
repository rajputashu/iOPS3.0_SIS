package com.sisindia.ai.android.features.moninput

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityMoninputCardsBinding
import com.sisindia.ai.android.features.othertasks.OtherTaskActivity
//import kotlinx.android.synthetic.main.activity_moninput_cards.*
import kotlinx.coroutines.*

/**
 * Created by Ashu Rajput on 6/2/2020.
 */
class MonInputCardsActivity : IopsBaseActivity() {

    private lateinit var viewModel: MonInputViewModel
    private lateinit var binding: ActivityMoninputCardsBinding
    private var isAnythingUpdated: Boolean = false

    override fun getLayoutResource(): Int {
        return R.layout.activity_moninput_cards
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(MonInputViewModel::class.java) as MonInputViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbMonInput)
        viewModel.getMonInputDetails()
        binding.getMonInputTasks.setOnClickListener {
            //            showWeekSelectionPopup(it)
            viewModel.getMonInputTasksFromServer()
        }
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_MONINPUT_TASK -> {
                    startActivityForResult(Intent(this, OtherTaskActivity::class.java),
                        IntentRequestCodes.REQUEST_CODE_OPEN_MON_INPUT)
                }
                NavigationConstants.ON_UPDATING_MON_INPUT_UI -> updateMonInputUI()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentRequestCodes.REQUEST_CODE_OPEN_MON_INPUT && resultCode == Activity.RESULT_OK)
            updateMonInputUI()
    }

    private fun updateMonInputUI() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            withContext(Dispatchers.Main) {
                viewModel.getMonInputDetails()
                isAnythingUpdated = true
            }
        }
    }

    //Don't delete it keep it for reference {for further use}
    /* private fun showWeekSelectionPopup(view: View) {
         val popup = PopupMenu(this, view)
         popup.inflate(R.menu.toolbar_menu_bs_weeks)
         popup.setOnMenuItemClickListener { item: MenuItem? ->
             when (item!!.itemId) {
                 R.id.currentWeek -> viewModel.getMonInputTasksFromServer("CURRENT_WEEK")
                 R.id.lastWeek -> viewModel.getMonInputTasksFromServer("LAST_WEEK")
             }
             true
         }
         popup.show()
     }*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isAnythingUpdated) {
            setResult(RESULT_OK)
            finish()
        } else
            super.onBackPressed()
    }
}