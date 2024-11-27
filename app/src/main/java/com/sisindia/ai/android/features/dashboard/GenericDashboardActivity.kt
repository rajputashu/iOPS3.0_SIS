package com.sisindia.ai.android.features.dashboard

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.YesNoDialogFragment
import com.sisindia.ai.android.commons.YesNoDialogFragment.Companion.newInstance
import com.sisindia.ai.android.commons.YesNoDialogFragment.Companion.newSingleButtonInstance
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_ADD_TASK
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_DUTY_ON_SELFIE
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_BILL_SUBMISSION
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_OTHERS
import com.sisindia.ai.android.constants.NavigationConstants.HIDE_PROGRESS_DIALOG
import com.sisindia.ai.android.constants.NavigationConstants.HIDE_PROGRESS_DIALOG_SHOW_POPUP
import com.sisindia.ai.android.constants.NavigationConstants.ON_CONVEYANCE_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_DISBANDMENT_MENU_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_ISSUE_MANAGEMENT_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_LOGOUT_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_MANUAL_SYNC
import com.sisindia.ai.android.constants.NavigationConstants.ON_MY_KPIS_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_PERFORMANCE_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_PLAN_OF_ACTIONS_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_RECRUIT_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_SALES_REFERENCE_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_SELF_SERVICE_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_SETTING_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_TIMELINE_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.ON_UNITS_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_ADD_ROTA
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_BILL_COLLECTION_TASK
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_BILL_SUBMISSION_TASK
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_CAMERA_FOR_SELFIE
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_DRAWER
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_ROTA
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_NUDGES_DASHBOARD
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_OTHER_TASK
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRE_DASH_BOARD
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_ROTA_BILL_SUBMISSION_TASK
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_ROTA_MONINPUT_TASK
import com.sisindia.ai.android.constants.NavigationConstants.SHOW_PROGRESS_DIALOG
import com.sisindia.ai.android.databinding.ActivityGenericDashboardBinding
import com.sisindia.ai.android.databinding.GenericDrawerHeaderBinding
import com.sisindia.ai.android.features.addtask.AddTaskActivity
import com.sisindia.ai.android.features.ailocation.AILocationFragment
import com.sisindia.ai.android.features.billcollection.BillCollectionDetailsOnSites
import com.sisindia.ai.android.features.billsubmit.BillSubmissionActivity
import com.sisindia.ai.android.features.billsubmit.BillSubmissionCardsActivity
import com.sisindia.ai.android.features.conveyance.ConveyanceActivity
import com.sisindia.ai.android.features.disband.DisbandmentFragment
import com.sisindia.ai.android.features.issues.IssueManagementFragment
import com.sisindia.ai.android.features.kpi.MyKpiFragment
import com.sisindia.ai.android.features.moninput.MonInputCardsActivity
import com.sisindia.ai.android.features.nudges.NudgesDashboardFragment
import com.sisindia.ai.android.features.othertasks.OtherTaskActivity
import com.sisindia.ai.android.features.performance.PerformanceFragment
import com.sisindia.ai.android.features.poa.PlansOfActionFragment
import com.sisindia.ai.android.features.predashboard.EffortsFragment
import com.sisindia.ai.android.features.recruitment.RecruitmentFragment
import com.sisindia.ai.android.features.rota.RotaFragment
import com.sisindia.ai.android.features.sales.SalesReferenceFragment
import com.sisindia.ai.android.features.selfie.DutyOnOffSelfie
import com.sisindia.ai.android.features.selfservice.SelfServiceFragment
import com.sisindia.ai.android.features.sync.ManualSyncFragment
import com.sisindia.ai.android.features.timline.TimeLineFragment
import com.sisindia.ai.android.features.uar.dialog.DialogListener
import com.sisindia.ai.android.features.units.DashBoardUnitsFragment
import com.sisindia.ai.android.utils.CustomProgressDialog.Companion.buildDialog

/**
 * Created by Ashu_Rajput on 6/10/2021.
 */
class GenericDashboardActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityGenericDashboardBinding
    lateinit var viewModel: GenericDashboardViewModel
    private var doubleBackToExitPressedOnce = false
    private lateinit var drawerHeaderBinding: GenericDrawerHeaderBinding
    private lateinit var dialog: Dialog

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, GenericDashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_generic_dashboard
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(GenericDashboardViewModel::class.java) as GenericDashboardViewModel
    }

    override fun onCreated() {
        viewModel.initGenericDashboard()
        openRotaScreen()
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.nvDashBoard.setNavigationItemSelectedListener(viewModel)
        drawerHeaderBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.generic_drawer_header,
            binding.nvDashBoard,
            false)
        binding.nvDashBoard.addHeaderView(drawerHeaderBinding.root)
        drawerHeaderBinding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun extractBundle() {

    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            var needToCloseDrawer = true

            when (message.what) {
                OPEN_DASH_BOARD_DRAWER -> {
                    needToCloseDrawer = false
                    binding.dlDashBoard.openDrawer(GravityCompat.START)
                }
                OPEN_NUDGES_DASHBOARD -> replaceFragment(NudgesDashboardFragment.newInstance())
                OPEN_ADD_ROTA -> {
                    startActivityForResult(AddTaskActivity.newIntent(this), REQUEST_CODE_ADD_TASK)
                }
                OPEN_DASH_BOARD_ROTA -> openRotaScreen()
                OPEN_PRE_DASH_BOARD -> replaceFragment(EffortsFragment.newInstance())
                ON_UNITS_CLICK -> replaceFragment(DashBoardUnitsFragment.newInstance())
                ON_PERFORMANCE_CLICK -> replaceFragment(PerformanceFragment.newInstance())
                ON_TIMELINE_CLICK -> replaceFragment(TimeLineFragment.newInstance())
                ON_CONVEYANCE_CLICK -> startActivity(ConveyanceActivity.newIntent(this))
                ON_PLAN_OF_ACTIONS_CLICK -> replaceFragment(PlansOfActionFragment.newInstance())
                ON_ISSUE_MANAGEMENT_CLICK -> replaceFragment(IssueManagementFragment.newInstance())

                ON_RECRUIT_CLICK -> replaceFragment(RecruitmentFragment.newInstance())
                ON_SALES_REFERENCE_CLICK -> replaceFragment(SalesReferenceFragment.newInstance())
                ON_DISBANDMENT_MENU_CLICK -> replaceFragment(DisbandmentFragment.newInstance())

                ON_SELF_SERVICE_CLICK -> replaceFragment(SelfServiceFragment.newInstance())
                ON_MY_KPIS_CLICK -> replaceFragment(MyKpiFragment.newInstance())
                ON_SETTING_CLICK -> replaceFragment(AILocationFragment.newInstance())
                ON_MANUAL_SYNC -> replaceFragment(ManualSyncFragment.newInstance())/*OPEN_CLIENT_COORDINATION -> startActivity(Intent(this,
                    ClientCoordinationActivity::class.java))*/

                OPEN_BILL_SUBMISSION_TASK -> startActivityForResult(Intent(this,
                    BillSubmissionActivity::class.java), REQUEST_CODE_OPEN_BILL_SUBMISSION)

                OPEN_BILL_COLLECTION_TASK -> startActivity(Intent(this,
                    BillCollectionDetailsOnSites::class.java))

                OPEN_OTHER_TASK -> startActivityForResult(Intent(this, OtherTaskActivity::class.java),
                    REQUEST_CODE_OPEN_OTHERS)

                OPEN_ROTA_BILL_SUBMISSION_TASK -> startActivity(Intent(this,
                    BillSubmissionCardsActivity::class.java))

                OPEN_ROTA_MONINPUT_TASK -> startActivity(Intent(this, MonInputCardsActivity::class.java))

                OPEN_CAMERA_FOR_SELFIE -> {
                    startActivityForResult(Intent(this, DutyOnOffSelfie::class.java),
                        REQUEST_CODE_DUTY_ON_SELFIE)
                }
                ON_LOGOUT_CLICK -> onLogout()
                SHOW_PROGRESS_DIALOG -> showDialogProgress()
                HIDE_PROGRESS_DIALOG -> hideDialogProgress()
                HIDE_PROGRESS_DIALOG_SHOW_POPUP -> {
                    hideDialogProgress(true, message.obj as String)
                }
            }
            if (needToCloseDrawer) binding.dlDashBoard.closeDrawer(GravityCompat.START)
        }
    }

    private fun openRotaScreen() {
        binding.dlDashBoard.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        replaceFragment(RotaFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        loadFragment(R.id.flDashBoard, fragment, FRAGMENT_REPLACE, false)
    }

    override fun onBackPressed() {
        if (binding.dlDashBoard.isDrawerOpen(GravityCompat.START)) binding.dlDashBoard.closeDrawer(
            GravityCompat.START)
        else {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.flDashBoard)
            if (currentFragment is RotaFragment) showAppCloseSnackBar()
            else openRotaScreen()
        }
    }

    private fun onLogout() {
        if (supportFragmentManager.findFragmentByTag(YesNoDialogFragment::class.java.simpleName) == null) {
            val fragment = newInstance(resources.getString(R.string.string_logout_msg))
            fragment.isCancelable = false
            fragment.initDialogListener(object : DialogListener {
                override fun onCrossButtonClick() {
                    fragment.dismissAllowingStateLoss()
                }

                override fun onViewAllPOAClick() {}

                override fun onYesButtonClicked() {
                    viewModel.clearAppData()
                    fragment.dismissAllowingStateLoss()
                }

                override fun onNoButtonClicked() {
                    fragment.dismissAllowingStateLoss()
                }
            })
            fragment.show(supportFragmentManager, YesNoDialogFragment::class.java.simpleName)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_OPEN_BILL_SUBMISSION -> openRotaScreen()
            REQUEST_CODE_ADD_TASK -> openRotaScreen()
            REQUEST_CODE_OPEN_OTHERS -> openRotaScreen()
            REQUEST_CODE_DUTY_ON_SELFIE -> {
                binding.dlDashBoard.closeDrawer(GravityCompat.START)
                if (resultCode == Activity.RESULT_OK) viewModel.triggerDutyOnOffFunctions(true)
                else drawerHeaderBinding.scDuty.isChecked = false
            }
        }
    }

    private fun showDialogProgress() {
        dialog = buildDialog(this, "Please wait... updating your duty status")
        dialog.show()
    }

    private fun hideDialogProgress(showPopUp: Boolean = false, errorMsg: String = "") {
        if (dialog.isShowing) {
            dialog.dismiss()
            if (showPopUp) dutyOnOffDialogError(errorMsg)
        }
    }

    private fun dutyOnOffDialogError(errorMsg: String) {
        val fragment = newSingleButtonInstance(errorMsg)
        fragment.isCancelable = false
//        fragment.initDialogListener()
        fragment.show(this.supportFragmentManager, YesNoDialogFragment::class.java.simpleName)
    }

    private fun showAppCloseSnackBar() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        val snackBar = Snackbar.make(binding.root, "Press again to exit", Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightRed))
        snackBar.show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}