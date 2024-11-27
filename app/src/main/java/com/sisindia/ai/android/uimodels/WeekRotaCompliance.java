package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@Parcel
public class WeekRotaCompliance {

    public int addRota;

    public int adhoc;

    public int completed;

    public String weekName;


    public WeekRotaCompliance() {
    }


    public String getRotaCompliance() {

        if (addRota != 0 || adhoc != 0) {
            DecimalFormat df = new DecimalFormat("00.00");
            double score = (double) completed * 100 / (addRota + adhoc);
            df.setRoundingMode(RoundingMode.UP);
            df.format(score);
            return df.format(score) + " %";
        } else {
            return "0.0%";
        }
    }
}
