package com.sisindia.ai.android.uimodels;

import androidx.annotation.DrawableRes;

import org.parceler.Parcel;

@Parcel
public class StatusUIModel {


    private String titleTxt;
    private String valueTxt;
    private boolean isCompleted;
    private boolean isPending;
    private boolean isDisabled;
    private StatusUIViewType viewType;
    @DrawableRes
    private int completedResId;
    @DrawableRes
    private int pendingResId;
    @DrawableRes
    private int disabledResId;

    public StatusUIModel(String titleTxt, String valueTxt, boolean isCompleted,
                         boolean isPending, boolean isDisabled, StatusUIViewType viewType,
                         int completedResId, int pendingResId, int disabledResId) {
        this.titleTxt = titleTxt;
        this.valueTxt = valueTxt;
        this.isCompleted = isCompleted;
        this.isPending = isPending;
        this.isDisabled = isDisabled;
        this.viewType = viewType;
        this.completedResId = completedResId;
        this.pendingResId = pendingResId;
        this.disabledResId = disabledResId;
    }

    public StatusUIModel() {
    }

    public String getTitleTxt() {
        return titleTxt;
    }

    public void setTitleTxt(String titleTxt) {
        this.titleTxt = titleTxt;
    }

    public String getValueTxt() {
        return valueTxt;
    }

    public void setValueTxt(String valueTxt) {
        this.valueTxt = valueTxt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public StatusUIViewType getViewType() {
        return viewType;
    }

    public void setViewType(StatusUIViewType viewType) {
        this.viewType = viewType;
    }

    public int getCompletedResId() {
        return completedResId;
    }

    public void setCompletedResId(int completedResId) {
        this.completedResId = completedResId;
    }

    public int getPendingResId() {
        return pendingResId;
    }

    public void setPendingResId(int pendingResId) {
        this.pendingResId = pendingResId;
    }

    public int getDisabledResId() {
        return disabledResId;
    }

    public void setDisabledResId(int disabledResId) {
        this.disabledResId = disabledResId;
    }

    public enum StatusUIViewType {

        UNKNOWN(0),
        VIEW_TYPE_ITEM(1),
        VIEW_TYPE_DIVIDER(2);

        private final int statusViewType;

        StatusUIViewType(int statusViewType) {
            this.statusViewType = statusViewType;
        }

        public int getStatusViewType() {
            return statusViewType;
        }
    }

    public interface GuardAvailableStatusIndex {

        int scantIdItemIndex = 0;
        int scantIdDeviderIndex = 1;

        int photoEvaluationItemIndex = 2;
        int photoEvaluationDeviderIndex = 3;

        int addSignatureItemIndex = 4;
        int addSignatureDeviderIndex = 5;
    }

    public interface GuardSleepingStatusIndex {

        int sleepingItemIndex = 0;
        int sleepingDeviderIndex = 1;

        int scantIdItemIndex = 2;
        int scantIdDeviderIndex = 3;

        int photoEvaluationItemIndex = 4;
        int photoEvaluationDeviderIndex = 5;

        int addSignatureItemIndex = 6;
        int addSignatureDeviderIndex = 7;
    }


}


