package com.sisindia.ai.android.features.uar.add

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Message
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import com.droidcommons.base.BaseActivity
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityAddPoaImproveplanBinding
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate

/**
 * Created by Ashu Rajput on 4/4/2020.
 */
class AddRiskPoaAndIpActivity : IopsBaseActivity() {

    private var viewModel: AddPoaAndIpViewModel? = null
    private lateinit var binding: ActivityAddPoaImproveplanBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_add_poa_improveplan
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AddPoaAndIpViewModel::class.java) as AddPoaAndIpViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbPOAUnitsName)
        viewModel?.let {
            if (it.isComingToAddPoa.get())
                openMarkSiteAtRisk()
            else {
                openAddImprovementPlan()
                it.toolBarLabel.set("Create Improvement Plan")
            }
        }
    }

    override fun extractBundle() {
        viewModel?.isComingToAddPoa?.set(intent
            .extras?.getBoolean(IntentConstants.IS_ADDING_POA, true)!!)

    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_MARK_SITE_AT_RISK -> openMarkSiteAtRisk()
                NavigationConstants.OPEN_TO_ADD_POA -> openAddPOAFragment()
                NavigationConstants.OPEN_TO_ADD_IMPROVEMENT_PLAN -> openAddImprovementPlan()
                NavigationConstants.OPEN_DATE_PICKER -> openDatePicker()
                NavigationConstants.ON_ADDING_POA_OR_IP_SUCCESSFULLY -> {
                    showToast("Plan added successfully")
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun openMarkSiteAtRisk() {
        loadFragment(R.id.addPoaIpContainer, AddSiteAtRiskFragment.newInstance(),
            BaseActivity.FRAGMENT_REPLACE, true)
    }

    private fun openAddPOAFragment() {
        viewModel?.toolBarLabel?.set("Create POA")
        loadFragment(R.id.addPoaIpContainer, AddPoaFragment.newInstance(),
            BaseActivity.FRAGMENT_REPLACE, false)
    }

    private fun openAddImprovementPlan() {
        loadFragment(R.id.addPoaIpContainer, AddImprovementPlanFragment.newInstance(),
            BaseActivity.FRAGMENT_REPLACE, false)
    }

    private fun openDatePicker() {
        val date = LocalDate.now()
        val datePickerDialog = DatePickerDialog(this,
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

                if (viewModel?.isComingToAddPoa!!.get())
                    viewModel!!.poaTargetDate.set(selectedDate)
                else
                    viewModel!!.ipTargetDate.set(selectedDate)

            }, date.year, date.monthValue - 1, date.dayOfMonth)
        datePickerDialog.datePicker.minDate = Instant.now().toEpochMilli()
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}