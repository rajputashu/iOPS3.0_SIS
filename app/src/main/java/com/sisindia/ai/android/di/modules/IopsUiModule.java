package com.sisindia.ai.android.di.modules;

import android.content.Context;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.uimodels.NavigationUIModel;
import com.sisindia.ai.android.uimodels.StatusUIModel;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.sisindia.ai.android.uimodels.StatusUIModel.StatusUIViewType.VIEW_TYPE_DIVIDER;
import static com.sisindia.ai.android.uimodels.StatusUIModel.StatusUIViewType.VIEW_TYPE_ITEM;

@Module
public class IopsUiModule {

    @Provides
    @Named("PostRegisters")
    ArrayList<StatusUIModel> providesPostRegisters(Context ctx) {
        ArrayList<StatusUIModel> list = new ArrayList<>();
        /*
          @params String titleTxt, String valueTxt, boolean isCompleted,
         * boolean isPending, boolean isDisabled, StatusUIViewType viewType,
         *int completedResId, int pendingResId, int disabledResId
         * */


        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new StatusUIModel
                ("SIS Register", "Pending",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));

        /* DayCheck Fragment status Ui Model STRENGTH CHECK*/
        list.add(new StatusUIModel
                ("Client Material", "Pending",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));

        /* DayCheck Fragment status Ui Model CLIENT HAND SHAKE*/
        list.add(new StatusUIModel
                ("Visitors", "Pending",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        return list;
    }


    @Provides
    @Named("DayCheckStatus")
    ArrayList<StatusUIModel> providesDayCheckStatusCheckUIModels(Context ctx) {
        ArrayList<StatusUIModel> list = new ArrayList<>();
        /*
          @params String titleTxt, String valueTxt, boolean isCompleted,
         * boolean isPending, boolean isDisabled, StatusUIViewType viewType,
         *int completedResId, int pendingResId, int disabledResId
         * */


        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new StatusUIModel
                (ctx.getString(R.string.post_check_text), ctx.getString(R.string.string_zero_enclosed_bracket),
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));

        /* DayCheck Fragment status Ui Model STRENGTH CHECK*/
        list.add(new StatusUIModel
                (ctx.getString(R.string.strength_check_text), ctx.getString(R.string.pending_text),
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));

        /* DayCheck Fragment status Ui Model CLIENT HAND SHAKE*/
        list.add(new StatusUIModel
                (ctx.getString(R.string.client_check_text), ctx.getString(R.string.pending_text),
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        return list;
    }

    @Provides
    @Named("DayCheckNavigation")
    ArrayList<NavigationUIModel> providesDayCheckNavigation(Context ctx) {
        ArrayList<NavigationUIModel> list = new ArrayList<>();

        /* String bottomTxt, int iconResId, boolean isCompleted, boolean isClickable, NavigationUiViewType viewType */
        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.post_check_text),
                R.drawable.ic_post_check, false, true, NavigationUIModel.NavigationUiViewType.DAY_CHECK_POST));

        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.strength_check_text),
                R.drawable.ic_strength_check, false, true, NavigationUIModel.NavigationUiViewType.DAY_CHECK_STRENGTH));

        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.client_check_text),
                R.drawable.ic_client_handshake, false, true, NavigationUIModel.NavigationUiViewType.DAY_CHECK_CLIENT_HANDSHAKE));


        return list;
    }

    @Provides
    @Named("NightCheckNavigation")
    ArrayList<NavigationUIModel> providesNightCheckNavigation(Context ctx) {
        ArrayList<NavigationUIModel> list = new ArrayList<>();

        /* String bottomTxt, int iconResId, boolean isCompleted, boolean isClickable, NavigationUiViewType viewType */
        /* NightCheck Navigation Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.post_check_text),
                R.drawable.ic_post_check, false, true, NavigationUIModel.NavigationUiViewType.DAY_CHECK_POST));

        /* NightCheck Navigation Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.strength_check_text),
                R.drawable.ic_strength_check, false, true, NavigationUIModel.NavigationUiViewType.DAY_CHECK_STRENGTH));

        return list;
    }


    @Provides
    @Named("PostCheckStatus")
    ArrayList<StatusUIModel> providesPostCheckStatusCheckUIModels(Context ctx) {
        ArrayList<StatusUIModel> list = new ArrayList<>();
        /*
          @params String titleTxt, String valueTxt, boolean isCompleted,
         * boolean isPending, boolean isDisabled, StatusUIViewType viewType,
         *int completedResId, int pendingResId, int disabledResId
         * */


        /* PostCheck Fragment status Ui Model POST CHECK*/
        list.add(new StatusUIModel
                (ctx.getString(R.string.post_check_guards_text), ctx.getString(R.string.string_zero_enclosed_bracket),
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));

        /* PostCheck Fragment status Ui Model STRENGTH CHECK*/
        list.add(new StatusUIModel
                (ctx.getString(R.string.post_check_registers_text), ctx.getString(R.string.pending_text),
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));

        /* PostCheck Fragment status Ui Model CLIENT HAND SHAKE*/
        list.add(new StatusUIModel
                (ctx.getString(R.string.post_check_checklist_text), ctx.getString(R.string.pending_text),
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        return list;
    }

    @Provides
    @Named("PostCheckNavigation")
    ArrayList<NavigationUIModel> providesPostCheckNavigation(Context ctx) {
        ArrayList<NavigationUIModel> list = new ArrayList<>();

        /* String bottomTxt, int iconResId, boolean isCompleted, boolean isClickable, NavigationUiViewType viewType */
        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.post_check_guard_check_text),
                R.drawable.ic_check_guard, false, true, NavigationUIModel.NavigationUiViewType.POST_CHECK_GUARD));

        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.post_check_register_check_text),
                R.drawable.ic_register_check, false, true, NavigationUIModel.NavigationUiViewType.POST_CHECK_REGISTER));

        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.post_check_add_security_risk_text),
                R.drawable.ic_add_security, false, true, NavigationUIModel.NavigationUiViewType.POST_CHECK_SECURITY_RISK));

        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.post_check_add_grievance_text),
                R.drawable.ic_add_grievance, false, true, NavigationUIModel.NavigationUiViewType.POST_CHECK_GRIEVANCE));


        /* DayCheck Fragment status Ui Model POST CHECK*/
        list.add(new NavigationUIModel(ctx.getString(R.string.post_check_add_kit_request_text),
                R.drawable.ic_add_kit_request, false, true, NavigationUIModel.NavigationUiViewType.POST_CHECK_KIT_REQUEST));


        return list;
    }


    @Provides
    @Named("GuardSleepingStatus")
    ArrayList<StatusUIModel> providesGuardSleepingStatusCheckUIModels(Context ctx) {
        ArrayList<StatusUIModel> list = new ArrayList<>();
        /*
          @params String titleTxt, String valueTxt, boolean isCompleted,
         * boolean isPending, boolean isDisabled, StatusUIViewType viewType,
         *int completedResId, int pendingResId, int disabledResId
         * */


        list.add(StatusUIModel.GuardSleepingStatusIndex.sleepingItemIndex, new StatusUIModel
                (ctx.getString(R.string.guard_sleeping_status_text), "",
                        true, false, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(StatusUIModel.GuardSleepingStatusIndex.sleepingDeviderIndex, new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));


        list.add(StatusUIModel.GuardSleepingStatusIndex.scantIdItemIndex, new StatusUIModel
                (ctx.getString(R.string.scan_id_status_text), "",
                        true, false, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(StatusUIModel.GuardSleepingStatusIndex.scantIdDeviderIndex, new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));


        list.add(StatusUIModel.GuardSleepingStatusIndex.photoEvaluationItemIndex, new StatusUIModel
                (ctx.getString(R.string.turn_out_status_text), "",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(StatusUIModel.GuardSleepingStatusIndex.photoEvaluationDeviderIndex, new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));


        list.add(StatusUIModel.GuardSleepingStatusIndex.addSignatureItemIndex, new StatusUIModel
                (ctx.getString(R.string.signature_status_text), "",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        return list;
    }


    @Provides
    @Named("GuardAvailableStatus")
    ArrayList<StatusUIModel> providesGuardAvailableStatusCheckUIModels(Context ctx) {
        ArrayList<StatusUIModel> list = new ArrayList<>();
        /*
          @params String titleTxt, String valueTxt, boolean isCompleted,
         * boolean isPending, boolean isDisabled, StatusUIViewType viewType,
         *int completedResId, int pendingResId, int disabledResId
         * */

        list.add(StatusUIModel.GuardAvailableStatusIndex.scantIdItemIndex, new StatusUIModel
                (ctx.getString(R.string.scan_id_status_text), "",
                        true, false, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(StatusUIModel.GuardAvailableStatusIndex.scantIdDeviderIndex, new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));


        list.add(StatusUIModel.GuardAvailableStatusIndex.photoEvaluationItemIndex, new StatusUIModel
                (ctx.getString(R.string.turn_out_status_text), "",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(StatusUIModel.GuardAvailableStatusIndex.photoEvaluationDeviderIndex, new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));


        list.add(StatusUIModel.GuardAvailableStatusIndex.addSignatureItemIndex, new StatusUIModel
                (ctx.getString(R.string.signature_status_text), "",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        return list;
    }


    @Provides
    @Named("AddGrienvancesStatus")
    ArrayList<StatusUIModel> providesAddGrienvancesStatus(Context ctx) {
        ArrayList<StatusUIModel> list = new ArrayList<>();
        /*
          @params String titleTxt, String valueTxt, boolean isCompleted,
         * boolean isPending, boolean isDisabled, StatusUIViewType viewType,
         *int completedResId, int pendingResId, int disabledResId
         * */

        list.add(new StatusUIModel
                (ctx.getString(R.string.guard_details_status_text), "",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        list.add(new StatusUIModel
                ("", "",
                        false, false, false, VIEW_TYPE_DIVIDER,
                        0, 0, 0));


        list.add(new StatusUIModel
                (ctx.getString(R.string.grievances_details_status_text), "",
                        false, true, false, VIEW_TYPE_ITEM,
                        R.drawable.ic_status_completed, R.drawable.ic_status_pending, R.drawable.is_status_disable));

        return list;
    }

    @Provides
    @Named("BarrackInspectionNavigation")
    ArrayList<NavigationUIModel> providesBarrackInspectionNavigation(Context ctx) {
        ArrayList<NavigationUIModel> list = new ArrayList<>();

        /* String bottomTxt, int iconResId, boolean isCompleted, boolean isClickable, NavigationUiViewType viewType */
        /* BarrackInspection Fragment status Ui Model {STRENGTH}*/
        list.add(new NavigationUIModel(ctx.getString(R.string.barrack_inspection_strength),
                R.drawable.ic_check_guard, false, true, NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_STRENGTH));

        /* BarrackInspection Fragment status Ui Model {SPACE}*/
        list.add(new NavigationUIModel(ctx.getString(R.string.barrack_inspection_space),
                R.drawable.ic_register_check, false, true, NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_SPACE));

        /* BarrackInspection Fragment status Ui Model {PICTURES}*/
        list.add(new NavigationUIModel(ctx.getString(R.string.barrack_inspection_pictures),
                R.drawable.ic_add_security, false, true, NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_OTHERS));

        /* BarrackInspection Fragment status Ui Model {MET LANDLORD}*/
        list.add(new NavigationUIModel(ctx.getString(R.string.barrack_inspection_metlandlord),
                R.drawable.ic_add_grievance, false, true, NavigationUIModel.NavigationUiViewType.BARRACK_INSPECTION_LANDLORD));
        return list;
    }
}
