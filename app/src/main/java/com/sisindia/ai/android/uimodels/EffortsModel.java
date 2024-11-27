package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class EffortsModel {

    public EffortBillSubmission effortBillSubmission;
    public EffortMonInput effortMonInput;
    public EffortBillCollection effortBillCollection;
    public EffortsSiteAtRisk effortUnitAtRisk;
    public EffortUnits effortUnits;
    public EffortsDayCheck effortsDayCheck;
    public EffortsNightCheck effortsNightCheck;

    public EffortsModel() {
    }

    @Parcel
    public static class EffortBillSubmission {

        public int billSubmissionPending;

        public int billSubmissionCompleted;

        public int billSubmissionOverDue;

        public EffortBillSubmission() {
        }
    }

    @Parcel
    public static class EffortMonInput {
        public int monInputPending;
        public int monInputCompleted;

        public EffortMonInput() {
        }
    }

    @Parcel
    public static class EffortBillCollection {
        public int bills;
        public int outstanding;
        public int overdue;

        public EffortBillCollection() {
        }
    }

    @Parcel
    public static class EffortUnits {
        public int pendingGeoTagging;
        public int pendingSPI;

        public EffortUnits() {
        }
    }

    @Parcel
    public static class EffortsDayCheck {
        public int target;
        public int rota;
        public int completed;

        public EffortsDayCheck() {
        }
    }

    @Parcel
    public static class EffortsNightCheck {
        public int target;
        public int rota;
        public int completed;

        public EffortsNightCheck() {
        }
    }

    @Parcel
    public static class EffortsSiteAtRisk {
        public int siteAtRisk;
        public int poaCreated;
        public int poaPending;

        public EffortsSiteAtRisk() {
        }
    }

    @Parcel
    public static class EffortsOthers {
        public int created;
        public int completed;

        public EffortsOthers() {
        }
    }

   /* @Parcel
    public static class EffortUnits {
        public int customerComplaints;
        public int customerCSATScore;
        public EffortCustomer() {
        }
    }*/

}
