package com.sisindia.ai.android.features.dashboard;

import static com.sisindia.ai.android.constants.IntentConstants.DYNAMIC_FORM_ID;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_ADD_TASK;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_DUTY_ON_SELFIE;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_BARRACK_INSPECTION;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_BILL_SUBMISSION;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_DYNAMIC_TASK;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_OTHERS;
import static com.sisindia.ai.android.constants.NavigationConstants.HIDE_PROGRESS_DIALOG;
import static com.sisindia.ai.android.constants.NavigationConstants.HIDE_PROGRESS_DIALOG_SHOW_POPUP;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_ANNUAL_KIT_REPLACEMENT_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_BARRACKS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_CIVIL_DEFENCE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_CONVEYANCE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_DASHBOARD_ROTA_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_DISBANDMENT_MENU_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_FORCE_DUTY_OFF;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_ISSUE_MANAGEMENT_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_LAUNCHING_MY_SIS_APP;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_LOGOUT_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_MANUAL_SYNC;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_MY_KPIS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_NOTIFICATIONS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_PERFORMANCE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_PLAN_OF_ACTIONS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_RECRUIT_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_SALES_REFERENCE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_SELF_SERVICE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_SETTING_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_TIMELINE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_UNITS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_ADD_ROTA;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_BARRACK_INSPECTION;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_BILL_COLLECTION_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_BILL_SUBMISSION_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_CAMERA_FOR_SELFIE;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_CLIENT_COORDINATION;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_DRAWER;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_ROTA;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DYNAMIC_TASKS;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_MYSIS_TASKS;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_NUDGES_DASHBOARD;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_OTHER_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRACTO_APP_LOGIN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRE_DASH_BOARD;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_ROTA_BILL_SUBMISSION_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_ROTA_MONINPUT_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_SIS_EVENTS;
import static com.sisindia.ai.android.constants.NavigationConstants.SHOW_PROGRESS_DIALOG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.droidcommons.camera.BaseCameraFragment;
import com.droidcommons.preference.Prefs;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.commons.YesNoDialogFragment;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.databinding.ActivityDashBoardBinding;
import com.sisindia.ai.android.databinding.LayoutDrawerHeaderBinding;
import com.sisindia.ai.android.features.addtask.AddTaskActivity;
import com.sisindia.ai.android.features.ailocation.AILocationFragment;
import com.sisindia.ai.android.features.akr.AKRFragment;
import com.sisindia.ai.android.features.barracks.inspection.BarrackInspectionActivity;
import com.sisindia.ai.android.features.barracks.listing.BarrackListingFragment;
import com.sisindia.ai.android.features.billcollection.BillCollectionDetailsOnSites;
import com.sisindia.ai.android.features.billsubmit.BillSubmissionActivity;
import com.sisindia.ai.android.features.billsubmit.BillSubmissionCardsActivity;
import com.sisindia.ai.android.features.civil.CivilDefenceFragment;
import com.sisindia.ai.android.features.clientcoordination.ClientCoordinationActivity;
import com.sisindia.ai.android.features.conveyance.ConveyanceActivity;
import com.sisindia.ai.android.features.disband.DisbandmentFragment;
import com.sisindia.ai.android.features.dynamictask.DynamicTaskActivity;
import com.sisindia.ai.android.features.issues.IssueManagementFragment;
import com.sisindia.ai.android.features.kpi.MyKpiFragment;
import com.sisindia.ai.android.features.moninput.MonInputCardsActivity;
import com.sisindia.ai.android.features.nudges.NudgesDashboardFragment;
import com.sisindia.ai.android.features.othertasks.OtherTaskActivity;
import com.sisindia.ai.android.features.performance.PerformanceFragment;
import com.sisindia.ai.android.features.poa.PlansOfActionFragment;
import com.sisindia.ai.android.features.practo.PractoTaskActivity;
import com.sisindia.ai.android.features.predashboard.EffortsFragment;
import com.sisindia.ai.android.features.recruitment.RecruitmentFragment;
import com.sisindia.ai.android.features.rota.RotaFragment;
import com.sisindia.ai.android.features.sales.SalesReferenceFragment;
import com.sisindia.ai.android.features.selfie.DutyOnOffSelfie;
import com.sisindia.ai.android.features.selfservice.SelfServiceFragment;
import com.sisindia.ai.android.features.sync.ManualSyncFragment;
import com.sisindia.ai.android.features.timline.TimeLineFragment;
import com.sisindia.ai.android.features.uar.dialog.DialogListener;
import com.sisindia.ai.android.features.units.DashBoardUnitsFragment;
import com.sisindia.ai.android.features.webviews.EventsFragment;
import com.sisindia.ai.android.receivers.MySISReceiver;
import com.sisindia.ai.android.uimodels.RotaTaskItemModel;
import com.sisindia.ai.android.uimodels.tasks.MySiSTaskDescription;
import com.sisindia.ai.android.utils.CustomProgressDialog;

public class DashBoardActivity extends IopsBaseActivity {

    DashBoardViewModel viewModel = null;
    ActivityDashBoardBinding binding = null;
    private boolean doubleBackToExitPressedOnce = false;
    private MySISReceiver receiver = null;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            loadFragmentWithNotificationHandling(intent.getExtras());
        }
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {

            switch (message.what) {

                case OPEN_NUDGES_DASHBOARD:
                    loadFragment(R.id.flDashBoard, NudgesDashboardFragment.Companion.newInstance(),
                            FRAGMENT_REPLACE, false);
                    break;

                case ON_DASHBOARD_ROTA_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openRotaScreen();
                    break;

                case ON_UNITS_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openUnitsScreen();
                    break;

                case ON_PERFORMANCE_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openPerformanceScreen();
//                    openPreDashBoardScreen();
                    break;

                case ON_NOTIFICATIONS_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openNotificationsScreen();
                    break;

                case ON_CONVEYANCE_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openConveyanceScreen();
                    break;

                case ON_PLAN_OF_ACTIONS_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openPlanOfActionsScreen();
                    break;

                /*case ON_UNITS_RISK_POA_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openUnitsRiskPoAScreen();
                    break;*/

                /*case ON_IMPROVEMENT_POA_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openImprovementPoAScreen();
                    break;*/

                case ON_ISSUE_MANAGEMENT_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openIssueManagementScreen();
                    break;

                case ON_ANNUAL_KIT_REPLACEMENT_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openAnnualKitReplacementScreen();
                    break;

                case ON_CIVIL_DEFENCE_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openCivilDefenceScreen();
                    break;

                /*case ON_MASK_DISTRIBUTION_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openMaskDistributionScreen();
                    break;*/

                case OPEN_SIS_EVENTS:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openSISEventsScreen();
                    break;

                case ON_RECRUIT_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openRecruitScreen();
                    break;

                case ON_SALES_REFERENCE_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openSalesReferenceScreen();
                    break;

                case ON_DISBANDMENT_MENU_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openDisbandmentScreen();
                    break;

                case ON_LAUNCHING_MY_SIS_APP:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    launchMySiSApp(true, null);
                    break;

                case ON_SELF_SERVICE_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openSelfServiceScreen();
                    break;

                /*case ON_UNIT_RAISING_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openUnitRaisingScreen();
                    break;*/

                case ON_BARRACKS_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openBarracksScreen();
                    break;

                case ON_MY_KPIS_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openMyKpisScreen();
                    break;

                case ON_SETTING_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openSettingScreen();
                    break;

                case OPEN_DASH_BOARD_DRAWER:
                    binding.dlDashBoard.openDrawer(GravityCompat.START);
                    break;

                case OPEN_ADD_ROTA:
                    openAddNewTaskScreen();
                    break;

                case OPEN_DASH_BOARD_ROTA:
                    openRotaScreen();
                    break;

                case OPEN_PRE_DASH_BOARD:
                    openPreDashBoardScreen();
                    break;

                /*case OPEN_ROTA_COMPLIANCE_ACTIVITY:
                    openRotaComplianceScreen();
                    break;*/

                case OPEN_OTHER_TASK:
                    openOtherTaskScreen();
                    break;

                case OPEN_BARRACK_INSPECTION:
                    startActivityForResult(new Intent(this, BarrackInspectionActivity.class),
                            REQUEST_CODE_OPEN_BARRACK_INSPECTION);
                    break;

                case OPEN_BILL_SUBMISSION_TASK:
                    startActivityForResult(new Intent(this, BillSubmissionActivity.class),
                            REQUEST_CODE_OPEN_BILL_SUBMISSION);
                    break;

                case OPEN_BILL_COLLECTION_TASK:
                    startActivity(new Intent(this, BillCollectionDetailsOnSites.class));
                    break;

                case OPEN_CLIENT_COORDINATION:
                    startActivity(new Intent(this, ClientCoordinationActivity.class));
                    break;

                case ON_MANUAL_SYNC:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openManualSyncScreen();
                    break;

                case ON_TIMELINE_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    openTimeLineScreen();
                    break;

                case ON_LOGOUT_CLICK:
                    binding.dlDashBoard.closeDrawer(GravityCompat.START);
                    onLogout();
                    break;

                case OPEN_ROTA_BILL_SUBMISSION_TASK:
                    startActivity(new Intent(this, BillSubmissionCardsActivity.class));
                    break;

                case OPEN_ROTA_MONINPUT_TASK:
                    startActivity(new Intent(this, MonInputCardsActivity.class));
                    break;

                case OPEN_CAMERA_FOR_SELFIE:
                    startActivityForResult(new Intent(this, DutyOnOffSelfie.class), REQUEST_CODE_DUTY_ON_SELFIE);
                    break;

                /*case OPEN_CHAT_BOT_TASK:
                    openChatBotTaskScreen();
                    break;*/

                case OPEN_MYSIS_TASKS:
                    RotaTaskItemModel model = (RotaTaskItemModel) message.obj;
                    launchMySiSApp(false, model);
                    break;

                case OPEN_DYNAMIC_TASKS:
                    startActivityForResult(new Intent(this, DynamicTaskActivity.class)
                                    .putExtra(DYNAMIC_FORM_ID, message.arg1),
                            REQUEST_CODE_OPEN_DYNAMIC_TASK);
                    break;

                case OPEN_PRACTO_APP_LOGIN:
                    Intent intent = new Intent(this, PractoTaskActivity.class);
                    practoLauncher.launch(intent);
                    break;

                case ON_FORCE_DUTY_OFF:
                    openDutyOffDialog();
                    break;

                case SHOW_PROGRESS_DIALOG:
                    showDialogProgress();
                    break;

                case HIDE_PROGRESS_DIALOG:
                    hideDialogProgress(false, "");
                    break;

                case HIDE_PROGRESS_DIALOG_SHOW_POPUP:
                    String errorMsg = (String) message.obj;
                    hideDialogProgress(true, errorMsg);
                    break;
            }
        });
    }

    private void openConveyanceScreen() {
//        loadFragment(R.id.flDashBoard, ConveyanceFragment.newInstance(), FRAGMENT_REPLACE, false);
        startActivity(ConveyanceActivity.newIntent(this));
    }

    private void openTimeLineScreen() {
        loadFragment(R.id.flDashBoard, TimeLineFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openManualSyncScreen() {
        loadFragment(R.id.flDashBoard, ManualSyncFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openIssueManagementScreen() {
        loadFragment(R.id.flDashBoard, IssueManagementFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    /*private void onDutyChanged(Boolean isOnDuty) {
        startActivityForResult(UserLocationActivity.newIntent(this, isOnDuty), IntentRequestCodes.REQUEST_CODE_IS_DUTY_ON);
    }

    private void openRotaComplianceScreen() {
        startActivity(new Intent(this, RotaComplianceGraphActivity.class));
    }*/

    private void openOtherTaskScreen() {
        startActivityForResult(new Intent(this, OtherTaskActivity.class), REQUEST_CODE_OPEN_OTHERS);
    }

    private void openPreDashBoardScreen() {
        binding.dlDashBoard.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        loadFragment(R.id.flDashBoard, EffortsFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openAddNewTaskScreen() {
        startActivityForResult(AddTaskActivity.newIntent(this), IntentRequestCodes.REQUEST_CODE_ADD_TASK);
    }

    private void openMyKpisScreen() {
        binding.dlDashBoard.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        loadFragment(R.id.flDashBoard, MyKpiFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openBarracksScreen() {
        loadFragment(R.id.flDashBoard, BarrackListingFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    /*private void openUnitRaisingScreen() {
        startActivity(new Intent(this, RaisingCardsActivity.class));
    }*/

    private void openRecruitScreen() {
        loadFragment(R.id.flDashBoard, RecruitmentFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openSalesReferenceScreen() {
        loadFragment(R.id.flDashBoard, SalesReferenceFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openDisbandmentScreen() {
        loadFragment(R.id.flDashBoard, DisbandmentFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void launchMySiSApp(boolean isDashboardCall, RotaTaskItemModel model) {

        String packageName = "com.sisindia.mysis.ao";

//        if (isMySISAppInstalled(packageName)) {
        if (isAppInstalled(this, packageName)) {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            if (!isDashboardCall) {
                try {
                    Gson gson = new Gson();
                    MySiSTaskDescription[] description = gson.fromJson(model.description, MySiSTaskDescription[].class);
                    if (description.length > 0) {
                        viewModel.updateMySiSStartStatus();
                        intent.putExtra("IOPSEmpNo", Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE));
//                        intent.putExtra("date", TimeUtils.formatServerDateToYYYYDDMM(model.estimatedTaskExecutionStartDateTime));
                        intent.putExtra("date", description[0].getTaskDate());
                        intent.putExtra("Task_ID", "" + model.taskTypeId);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            } else {
                intent.putExtra("IOPSEmpNo", Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE));
                intent.putExtra("date", "");
                intent.putExtra("Task_ID", "99");
            }
            final ComponentName cn = new ComponentName(packageName, "com.sisindia.mysis.ao.iops.IopsMainActivity");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "MySIS Activity Not Found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "MySIS Application Not Found/Not Installed", Toast.LENGTH_LONG).show();
            //Redirecting to play store if MYSIS is not installed
            /*try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } catch (ActivityNotFoundException exception) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }*/
        }
    }

    /*private boolean isMySISAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    public boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openSelfServiceScreen() {
        loadFragment(R.id.flDashBoard, SelfServiceFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openAnnualKitReplacementScreen() {
        loadFragment(R.id.flDashBoard, AKRFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openCivilDefenceScreen() {
        loadFragment(R.id.flDashBoard, CivilDefenceFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    /*private void openWinterKitScreen() {
        loadFragment(R.id.flDashBoard, WinterKitFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }*/

    /*private void openMaskDistributionScreen() {
        loadFragment(R.id.flDashBoard, MaskDistributionFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }*/

    private void openSISEventsScreen() {
        loadFragment(R.id.flDashBoard, EventsFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openPlanOfActionsScreen() {
        loadFragment(R.id.flDashBoard, PlansOfActionFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    /*private void openUnitsRiskPoAScreen() {
        loadFragment(R.id.flDashBoard, UnitAtRiskFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openImprovementPoAScreen() {
        loadFragment(R.id.flDashBoard, ImprovementPlansFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }*/

    private void openNotificationsScreen() {
        showToast("Notification");
        loadFragment(R.id.flDashBoard, BaseCameraFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openPerformanceScreen() {
        loadFragment(R.id.flDashBoard, PerformanceFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openUnitsScreen() {
//        binding.dlDashBoard.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        loadFragment(R.id.flDashBoard, DashBoardUnitsFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    // this method for opening the the rota dash board
    private void openRotaScreen() {
        binding.dlDashBoard.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        loadFragment(R.id.flDashBoard, RotaFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    // this method for opening the setting screen {AI LOCATION}
    private void openSettingScreen() {
        binding.dlDashBoard.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        loadFragment(R.id.flDashBoard, AILocationFragment.Companion.newInstance(), FRAGMENT_REPLACE, false);
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
        registerMySISReceiver();
        loadFragmentWithNotificationHandling(getIntent().getExtras());
        FirebaseCrashlytics.getInstance().setUserId(Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE, ""));
    }

    private void loadFragmentWithNotificationHandling(Bundle bundle) {
        if (bundle != null && bundle.containsKey("NOTIFICATION_MODULE")) {
            switch (bundle.getString("NOTIFICATION_MODULE")) {
                case "UNITS_STATUS":
                    openUnitsScreen();
                    break;
                case "NEW_SITE_RISK_STATUS":
                    openPlanOfActionsScreen();
                    break;
                case "ROTA_STATUS":
                    openRotaScreen();
                    break;
                case "GRIEVANCE_STATUS":
                    openIssueManagementScreen();
                    break;
                case "AKR_SYNC":
                    openAnnualKitReplacementScreen();
                    break;
                case "DUTY_ON_OFF_STATUS":
                    openRotaScreen();
                    binding.dlDashBoard.openDrawer(GravityCompat.START);
                    break;
            }
        } else {
            openRotaScreen();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.dlDashBoard.isDrawerOpen(GravityCompat.START)) {
            binding.dlDashBoard.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flDashBoard);
            if (currentFragment instanceof RotaFragment) {
                showAppCloseSnackBar();
            } /*else if (currentFragment instanceof ManualSyncFragment) {
                openSettingScreen();
            } */ else {
                openRotaScreen();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_ADD_TASK:
            case REQUEST_CODE_OPEN_BILL_SUBMISSION:
            case REQUEST_CODE_OPEN_BARRACK_INSPECTION:
            case REQUEST_CODE_OPEN_OTHERS:
            case REQUEST_CODE_OPEN_DYNAMIC_TASK:
                openRotaScreen();
                break;
            case REQUEST_CODE_DUTY_ON_SELFIE:
                binding.dlDashBoard.closeDrawer(GravityCompat.START);
                if (resultCode == Activity.RESULT_OK)
                    viewModel.triggerDutyOnOffFunctions(true);
                else
                    drawerHeaderBinding.scDuty.setChecked(false);
                break;
        }
    }

    LayoutDrawerHeaderBinding drawerHeaderBinding;

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.nvDashBoard.setNavigationItemSelectedListener(viewModel);
        drawerHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_drawer_header, binding.nvDashBoard, false);
        binding.nvDashBoard.addHeaderView(drawerHeaderBinding.getRoot());
        drawerHeaderBinding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (DashBoardViewModel) getAndroidViewModel(DashBoardViewModel.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_dash_board;
    }

    private void showAppCloseSnackBar() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar.make(binding.getRoot(), "Press again to exit", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightRed));
        snackbar.show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void onLogout() {
        if (getSupportFragmentManager().findFragmentByTag(YesNoDialogFragment.class.getSimpleName()) == null) {
            YesNoDialogFragment fragment = YesNoDialogFragment.newInstance(getResources().getString(R.string.string_logout_msg));
            fragment.setCancelable(false);
            fragment.initDialogListener(new DialogListener() {
                @Override
                public void onCrossButtonClick() {
                    fragment.dismissAllowingStateLoss();
                }

                @Override
                public void onViewAllPOAClick() {

                }

                @Override
                public void onYesButtonClicked() {
                    viewModel.clearAppData();
                    fragment.dismissAllowingStateLoss();
                }

                @Override
                public void onNoButtonClicked() {
                    fragment.dismissAllowingStateLoss();
                }
            });
            fragment.show(getSupportFragmentManager(), YesNoDialogFragment.class.getSimpleName());
        }
    }

    private void openDutyOffDialog() {
        YesNoDialogFragment fragment = YesNoDialogFragment.newSingleButtonInstance("Warning!\n\n\"You have not turned off your previous day duty. This may impact your conveyance.\" \n\nAlways mark duty off daily");
        fragment.setCancelable(false);
        fragment.initDialogListener(new DialogListener() {
            @Override
            public void onCrossButtonClick() {

            }

            @Override
            public void onViewAllPOAClick() {

            }

            @Override
            public void onYesButtonClicked() {
                fragment.dismissAllowingStateLoss();
                drawerHeaderBinding.scDuty.setChecked(false);
            }

            @Override
            public void onNoButtonClicked() {

            }
        });
        fragment.show(this.getSupportFragmentManager(), YesNoDialogFragment.class.getSimpleName());
    }

    private void openDutyOnOffAttemptsDialog(String errorMsg) {
        YesNoDialogFragment fragment = YesNoDialogFragment.newSingleButtonInstance(errorMsg);
        fragment.setCancelable(false);
        fragment.initDialogListener(new DialogListener() {
            @Override
            public void onCrossButtonClick() {

            }

            @Override
            public void onViewAllPOAClick() {

            }

            @Override
            public void onYesButtonClicked() {
//                viewModel.triggerDutyOnOffToServer();
            }

            @Override
            public void onNoButtonClicked() {

            }
        });
        fragment.show(this.getSupportFragmentManager(), YesNoDialogFragment.class.getSimpleName());
    }

    private Dialog dialog = null;

    private void showDialogProgress() {
        dialog = CustomProgressDialog.Companion.buildDialog(this, "Please wait... updating your duty status");
        dialog.show();
    }

    private void hideDialogProgress(boolean showPopUp, String errorMsg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
            if (showPopUp)
                openDutyOnOffAttemptsDialog(errorMsg);
        }
    }

    ActivityResultLauncher<Intent> practoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK)
                    openRotaScreen();
            });

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void registerMySISReceiver() {
        receiver = new MySISReceiver();
        IntentFilter intentFilter = new IntentFilter("com.sisindia.ai.android.dev.mysisrota");
//        registerReceiver(receiver, intentFilter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
    }
}