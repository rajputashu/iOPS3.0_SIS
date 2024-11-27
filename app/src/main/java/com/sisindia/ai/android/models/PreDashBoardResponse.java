package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class PreDashBoardResponse {

    @SerializedName("efforts")
    private ArrayList<Object> efforts;

    @SerializedName("results")
    private ArrayList<Object> results;

    public PreDashBoardResponse() {
    }

    public ArrayList<Object> getEfforts() {
        return efforts;
    }

    public void setEfforts(ArrayList<Object> efforts) {
        this.efforts = efforts;
    }

    public ArrayList<Object> getResults() {
        return results;
    }

    public void setResults(ArrayList<Object> results) {
        this.results = results;
    }
}
