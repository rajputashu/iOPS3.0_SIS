package com.sisindia.ai.android.uimodels;

import androidx.annotation.StringRes;

import com.sisindia.ai.android.R;

import org.parceler.Parcel;

@Parcel
public class BreadCumItemModel {

    public @StringRes
    int topTitleResId;

    public String topTitle;

    public String bottomTitle;

    public boolean isPending;

    public boolean isDisable;

    public int id;

    public BreadCumItemModel() {
    }

    public BreadCumItemModel(int id) {
        this.id = id;
    }


    public static BreadCumItemModel postItem(int noOfCheckedPosts, boolean isDisable) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 1;
        model.isDisable = isDisable;
        model.topTitleResId = R.string.post_check_text;
        if (isDisable) {
            model.bottomTitle = "NA";
        } else {
            model.bottomTitle = noOfCheckedPosts == 0 ? "(0)" : "(" + noOfCheckedPosts + ")";
            model.isPending = noOfCheckedPosts == 0;
        }

        return model;
    }

    public static BreadCumItemModel strengthItem(int availableStrength, int noOfCheckedStrength, boolean isDisable) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 2;
        model.isDisable = isDisable;
        model.topTitleResId = R.string.strength_check_text;
        if (isDisable) {
            model.bottomTitle = "NA";
        } else {
            model.bottomTitle = noOfCheckedStrength == availableStrength ? "Done" : "Pending";
            model.isPending = noOfCheckedStrength != availableStrength;
        }

        return model;
    }

    public static BreadCumItemModel clientHandShakeItem(Integer clientHandshakeIsDone, boolean isDisable) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 3;
        model.isDisable = isDisable;
        model.topTitleResId = R.string.client_check_text;
        if (isDisable) {
            model.bottomTitle = "NA";
        } else {
            model.bottomTitle = (clientHandshakeIsDone != null && clientHandshakeIsDone == 2) ? "Done" : "Pending";
            model.isPending = clientHandshakeIsDone == null || clientHandshakeIsDone != 2;
        }
        return model;
    }

    public static BreadCumItemModel siteCheckListItem(int availableSiteCheckList, int noOfSiteCheckListDone, boolean isDisable) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 4;
        model.isDisable = isDisable;
        model.topTitleResId = R.string.site_check_text;
        if (isDisable) {
            model.bottomTitle = "NA";
        } else {
            model.bottomTitle = noOfSiteCheckListDone == availableSiteCheckList ? "Done" : "Pending";
            model.isPending = noOfSiteCheckListDone != availableSiteCheckList;
        }
        return model;
    }

    public static BreadCumItemModel guardItem(int noOfGuards) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 5;
        model.topTitleResId = R.string.guards;
        model.bottomTitle = noOfGuards == 0 ? "(0)" : "(" + noOfGuards + ")";
        model.isPending = noOfGuards == 0;
        return model;
    }

    public static BreadCumItemModel registerItem(int availableRegisters, int noOfCheckedRegisters, boolean isDisable) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 6;
        model.isDisable = isDisable;
        model.topTitleResId = R.string.string_register;
        if (isDisable) {
            model.bottomTitle = "NA";
        } else {
            model.bottomTitle = availableRegisters == noOfCheckedRegisters ? "Done" : "Pending";
            model.isPending = availableRegisters != noOfCheckedRegisters;
        }
        return model;
    }

    public static BreadCumItemModel postCheckListItem(int availablePostCheckList, int postCheckListDone, boolean isDisable) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 7;
        model.isDisable = isDisable;
        model.topTitleResId = R.string.post_check_checklist_text;
        if (isDisable) {
            model.bottomTitle = "NA";
        } else {
            model.bottomTitle = availablePostCheckList == postCheckListDone ? "Done" : "Pending";
            model.isPending = availablePostCheckList != postCheckListDone;
        }
        return model;
    }

    public static BreadCumItemModel guardSleepingItem() {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 8;
        model.topTitleResId = R.string.guard_sleeping_status_text;
        model.bottomTitle = "Done";
        model.isPending = false;
        return model;
    }

    public static BreadCumItemModel guardAvailableItem() {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 9;
        model.topTitleResId = R.string.scan_id_status_text;
        model.bottomTitle = "Done";
        model.isPending = false;
        return model;
    }

    public static BreadCumItemModel guardTurnOutItem(boolean isDone) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 10;
        model.topTitleResId = R.string.turn_out_status_text;
        model.bottomTitle = isDone ? "Done" : "Pending";
        model.isPending = !isDone;
        return model;
    }

    public static BreadCumItemModel guardSignatureItem(boolean isDone) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 11;
        model.topTitleResId = R.string.signature_status_text;
        model.bottomTitle = isDone ? "Done" : "Pending";
        model.isPending = !isDone;
        return model;
    }

    public static BreadCumItemModel guardDetailItem(boolean isDone) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 12;
        model.topTitleResId = R.string.guard_details_status_text;
        model.bottomTitle = isDone ? "Done" : "Pending";
        model.isPending = !isDone;
        return model;
    }

    public static BreadCumItemModel grievanceItem(boolean isDone) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 13;
        model.topTitleResId = R.string.grievances_details_status_text;
        model.bottomTitle = isDone ? "Done" : "Pending";
        model.isPending = !isDone;
        return model;
    }

    public static BreadCumItemModel registerCheckItem(String registerName, boolean isDone) {
        BreadCumItemModel model = new BreadCumItemModel();
        model.id = 13;
        model.topTitleResId = R.string.register_item;
        model.topTitle = registerName;
        model.bottomTitle = isDone ? "Done" : "Pending";
        model.isPending = !isDone;
        return model;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BreadCumItemModel)) return false;
        BreadCumItemModel it = (BreadCumItemModel) obj;
        return id == it.id;
    }
}
