package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ClientModel {

    public int id;

    public String title;

    public String role;

    public String contactFullName;

    public String contactEmailId;

    public String contactPhoneNo;

    public int customerId;

    public String customerMobileNo;

    public boolean isActive;

    public int customerContactId;

    @Ignore
    public boolean isChecked;

    public ClientModel() {
    }
}
