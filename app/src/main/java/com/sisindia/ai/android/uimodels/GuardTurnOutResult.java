package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

@Parcel
public class GuardTurnOutResult implements Serializable {

    @Expose
    public List<GuardTurnoutModel> turnOutResult;
    @Expose
    public List<GuardTurnoutModel> mlTurnOutResult;

    public GuardTurnOutResult() {

    }

    @Parcel
    public static class GuardTurnoutModel {
//    public static class GuardTurnoutModel implements Serializable {

        @Expose
        public int id;

        @Expose
        public String displayName;

        @Expose
        public int lookupIdentifier;

        @Ignore
        @Expose
        public boolean isSelected = false;

        @Ignore
        @Expose
        public boolean isCBSelected = false;


        public GuardTurnoutModel() {
        }

        public GuardTurnoutModel(String displayName,boolean isSelected) {
            this.id = -1;
            this.displayName = displayName;
            this.isSelected = isSelected;
        }
    }
}
