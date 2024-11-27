package com.sisindia.ai.android.uimodels;

import androidx.annotation.DrawableRes;

import org.parceler.Parcel;

@Parcel
public class NavigationUIModel {

    private String bottomTxt;

    @DrawableRes
    private int iconResId;

    private boolean isCompleted;

    private boolean isClickable;

    private NavigationUiViewType viewType;

    public NavigationUIModel() {
    }

    public NavigationUIModel(String bottomTxt, int iconResId, boolean isCompleted, boolean isClickable, NavigationUiViewType viewType) {
        this.bottomTxt = bottomTxt;
        this.iconResId = iconResId;
        this.isCompleted = isCompleted;
        this.isClickable = isClickable;
        this.viewType = viewType;
    }

    public String getBottomTxt() {
        return bottomTxt;
    }

    public void setBottomTxt(String bottomTxt) {
        this.bottomTxt = bottomTxt;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public NavigationUiViewType getViewType() {
        return viewType;
    }

    public void setViewType(NavigationUiViewType viewType) {
        this.viewType = viewType;
    }


    public enum NavigationUiViewType {
        UNKNOWN(0),
        DAY_CHECK_POST(1),
        DAY_CHECK_STRENGTH(2),
        DAY_CHECK_CLIENT_HANDSHAKE(3),
        POST_CHECK_GUARD(4),
        POST_CHECK_REGISTER(5),
        POST_CHECK_SECURITY_RISK(6),
        POST_CHECK_GRIEVANCE(7),
        POST_CHECK_KIT_REQUEST(8),
        BARRACK_INSPECTION_STRENGTH(9),
        BARRACK_INSPECTION_SPACE(10),
        BARRACK_INSPECTION_OTHERS(11),
        BARRACK_INSPECTION_LANDLORD(12);

        private final int navigationViewType;

        NavigationUiViewType(int navigationViewType) {
            this.navigationViewType = navigationViewType;
        }

        public int getNavigationViewType() {
            return navigationViewType;
        }
    }

}


