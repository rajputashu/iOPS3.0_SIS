package com.sisindia.ai.android.features.kpi;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_DRAWER;

public class MyKpiViewModel extends IopsBaseViewModel {

    @Inject
    public MyKpiViewModel(@NonNull Application application) {
        super(application);
    }

    public void ivRotaDrawerClick(View view) {
        message.what = OPEN_DASH_BOARD_DRAWER;
        liveData.postValue(message);
    }

    public MyKPIAdapter myKpiAdapter = new MyKPIAdapter();

    public void updateKPIAdapter() {
        List<MyKpiMO> list = new ArrayList<>();
        Resources res = getApplication().getResources();
        list.add(new MyKpiMO("1.", "Rota Compliance", "100%", "Average of Monthly Rota Compliance",
                res.getString(R.string.kpi1_range), "5\n4\n3\n2\n1", "15%"));

        list.add(new MyKpiMO("2.", "Actual vs contracted billing (Cumulative score based on category wise performance)",
                "0", "Variance (Shortage) rank wise in INR",
                res.getString(R.string.kpi2_range), "5\n4\n3\n2\n1", "10%"));

        list.add(new MyKpiMO("3.", "Recruitment", "10 per month", "As per Registration from data (Average)",
                res.getString(R.string.kpi3_range), "5\n4\n3\n2\n1", "15%"));

        list.add(new MyKpiMO("4.", "Deduction Control", "0%", "Actual Deduction / billing",
                res.getString(R.string.kpi4_range), "5\n4\n3\n2\n1", "10%"));

        list.add(new MyKpiMO("5.", "Collection vs Target (Current month billing)", "100%",
                "Average Collection % as per ERP",
                res.getString(R.string.kpi5_range), "5\n4\n3\n2\n1", "15%"));

        list.add(new MyKpiMO("6.", "Disbandment Control", "0.25%", "ERP Generated",
                res.getString(R.string.kpi6_range), "5\n4\n3\n2\n1", "25%"));

        list.add(new MyKpiMO("7.", "Over time (OT Control)", "2.5%", "ERP Generated",
                res.getString(R.string.kpi7_range), "5\n4\n3\n2\n1", "5%"));

        list.add(new MyKpiMO("8.", "Annual Kit Replacement", "No of Guards whose kit are due for replacement for more than 30 days. It shall be counted when logo is returned.",
                "ERP Generated", res.getString(R.string.kpi8_range), "5\n4\n3\n2\n1", "5%"));
        myKpiAdapter.clearAndSetItems(list);
    }
}
