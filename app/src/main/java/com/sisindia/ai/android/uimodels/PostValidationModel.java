package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class PostValidationModel {

    public int checkedPostId;

    public int countOfCheckedGuard;

    public int totalRegisters;

    public int countOfCheckedRegister;

    public int totalPostCheckList;

    public int editedPostCheckList;

    public int minGuardCheckCount = 1;

    /*public int countOfCheckedPost;

    public int minPostCheckCount = 1;*/

    public PostValidationModel() {
    }
}
