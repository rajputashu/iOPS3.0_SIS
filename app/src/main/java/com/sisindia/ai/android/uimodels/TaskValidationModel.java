package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class TaskValidationModel {

    public int taskId;

    public int taskType;

    public int posts;

    public int totalSiteCheckList;

    public int totalEdited;

    public int isPendingStrength;

    public int clientHandShakeStatus;

    public int minPostCheckCount = 1;

    public int minGuardCheckCount = 1;

    public int checkedGuardCount = 1;

    public TaskValidationModel() {
    }
}
