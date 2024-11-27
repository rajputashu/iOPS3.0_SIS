package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class ComplaintSection {

    @Expose
    public int id;

    @Expose
    public String displayName;

    @Expose
    public int lookupIdentifier;

    public int lookupDependentValue;

    @Ignore
    @Expose
    public boolean isSelected = false;

    public ComplaintSection() {
    }
}
