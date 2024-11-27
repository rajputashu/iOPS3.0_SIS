package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class GrievanceCategory {

    @Expose
    public int id;

    @Expose
    public String displayName;

    @Expose
    public int lookupIdentifier;

    @Ignore
    @Expose
    public boolean isSelected = false;

    public GrievanceCategory() {
    }

    @Ignore
    public GrievanceCategory(int lookupIdentifier) {
        this.lookupIdentifier = lookupIdentifier;
    }
}
