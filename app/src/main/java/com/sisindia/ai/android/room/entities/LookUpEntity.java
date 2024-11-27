package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class LookUpEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("lookupName")
    public String lookupName;

    @SerializedName("displayName")
    public String displayName;

    @SerializedName("lookupIdentifier")
    public int lookupIdentifier;

    @SerializedName("description")
    public String description;

    @SerializedName("lookupTypeId")
    public int lookupTypeId;

    @SerializedName("lookupTypeName")
    public String lookupTypeName;

    @SerializedName("lookupDependentTypeId")
    public int lookupDependentTypeId;

    @SerializedName("lookupDependentValue")
    public int lookupDependentValue;

    public LookUpEntity() {
    }
}
