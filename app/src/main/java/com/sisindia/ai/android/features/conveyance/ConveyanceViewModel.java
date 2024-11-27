package com.sisindia.ai.android.features.conveyance;

import android.app.Application;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.billsubmit.RadioCheckedListener;
import com.sisindia.ai.android.models.performance.ConveyanceReportDataMO;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_CONVEYANCE_DATE_SELECTED;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_DRAWER;

public class ConveyanceViewModel extends IopsBaseViewModel {

    public ObservableField<LocalDate> conveyanceDate = new ObservableField<>(LocalDate.now());
    public ObservableField<String> receivedDate = new ObservableField<>("");
    public ObservableField<String> toolBarTitleWithRegNo = new ObservableField<>("Conveyance(" + Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE) + ")");
    public ObservableField<String> totalTasks = new ObservableField<>();
    public ObservableField<String> distanceTravelled = new ObservableField<>();
    public ObservableField<String> kmApproved = new ObservableField<>();
    public ObservableField<String> deductions = new ObservableField<>();
    public ObservableField<String> loadingMessage = new ObservableField<>("Loading please wait...");
    public ObservableBoolean noData = new ObservableBoolean(true);
    public ObservableBoolean isConveyanceDataAvailable = new ObservableBoolean(false);
    public ConveyanceAdapter adapter = new ConveyanceAdapter();
    public ObservableBoolean isSingleDateConveyanceRequest = new ObservableBoolean(false);

    public ObservableInt obsTotalTasks = new ObservableInt(0);
    public ObservableDouble obsTotalKmTravelled = new ObservableDouble(0.0);
    public ObservableDouble obsTotalDeduction = new ObservableDouble(0.0);
    public ObservableDouble obsTotalKmApproved = new ObservableDouble(0.0);

    public MonthlyConveyanceAdapter conveyanceAdapter = new MonthlyConveyanceAdapter();
    public ConveyanceListener conveyanceListener = item -> {
        message.what = ON_CONVEYANCE_DATE_SELECTED;
        message.obj = item;
        liveData.postValue(message);
    };

    public RadioCheckedListener onRadioCheckedListener = radioButtonText -> {
        if (radioButtonText.contains("Current"))
            fetchMonthlyConveyanceDetails(false);
        else if (radioButtonText.contains("Last"))
            fetchMonthlyConveyanceDetails(true);
    };

    @Inject
    public ConveyanceViewModel(@NonNull Application application) {
        super(application);
    }

    public void ivRotaDrawerClick(View view) {
        message.what = OPEN_DASH_BOARD_DRAWER;
        liveData.postValue(message);
    }

    public void initViewModel() {
        setIsLoading(true);
        String selectedDate = LocalDate.now().toString();
//        LocalDate selectedDate = conveyanceDate.get() == null ? LocalDate.now() : conveyanceDate.get();
        if (isSingleDateConveyanceRequest.get())
            selectedDate = receivedDate.get();
        else
            selectedDate = conveyanceDate.get() == null ? selectedDate : Objects.requireNonNull(conveyanceDate.get()).toString();

        addDisposable(coreApi.getAOConveyance(Objects.requireNonNull(selectedDate))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setIsLoading(false);
                    adapter.clearAndSetItems(response.conveyance.aoTimeLine);
                    noData.set(response.conveyance == null || response.conveyance.aoTimeLine == null || response.conveyance.aoTimeLine.size() == 0);

                    if (response.conveyance != null && response.conveyance.conveyanceSummary != null) {
                        totalTasks.set("Total Tasks : " + response.conveyance.conveyanceSummary.totalTask);
                        distanceTravelled.set("Distance Travelled : " + response.conveyance.conveyanceSummary.distanceTravelled);
                        kmApproved.set("KM Approved : " + response.conveyance.conveyanceSummary.kmApproved);
                        deductions.set("Deduction : " + response.conveyance.conveyanceSummary.deduction);
                    }

                }, this::onApiError));
    }

    public void onTaskStartDateClick(View v) {
        LocalDate startDate = conveyanceDate.get();
        if (startDate != null) {
            int year = startDate.getYear();
            int month = startDate.getMonthValue() - 1;
            int day = startDate.getDayOfMonth();
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), (view, pYear, pMonth, pDayOfMonth) -> {
                conveyanceDate.set(LocalDate.of(pYear, pMonth + 1, pDayOfMonth));
                initViewModel();
            }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(Instant.now().toEpochMilli());
            datePickerDialog.show();

            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        }
    }

    public void fetchMonthlyConveyanceDetails(boolean isLastMonthConveyanceRequest) {
        isLoading.set(VISIBLE);
        LocalDate localDate = LocalDate.now();
        if (isLastMonthConveyanceRequest)
            localDate = LocalDate.now().minusMonths(1);

        addDisposable(coreApi.getAoMonthlyConveyanceReport(
                Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID), localDate.getMonthValue(), localDate.getYear())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(conveyanceList -> {
                    isLoading.set(GONE);
                    if (conveyanceList.getData().isEmpty()) {
                        loadingMessage.set("No Data Found!!");
                        isConveyanceDataAvailable.set(false);
                    } else {
                        isConveyanceDataAvailable.set(true);
//                        Timber.e("ResultSIZE %s", conveyanceList.getData().size());
                        conveyanceAdapter.clearAndSetItems(conveyanceList.getData());
                        calculateTotals(conveyanceList.getData());
                    }
                }, e -> {
                    isLoading.set(GONE);
                    loadingMessage.set("No Data Found!!");
                }));
    }

    private void calculateTotals(List<ConveyanceReportDataMO> list) {
        int totalTasks = 0;
        double totalKMTravelled = 0.0;
        double totalDeduction = 0.0;
        double totalKMApproved = 0.0;

        for (ConveyanceReportDataMO model : list) {
            totalTasks += model.getTotalTasks() == null ? 0 : model.getTotalTasks();
            totalKMTravelled += model.getTotalArialDistance() == null ? 0.0 : model.getTotalArialDistance();
            totalDeduction += model.getDeductionDistance() == null ? 0.0 : model.getDeductionDistance();
            totalKMApproved += model.getApprovalDistance() == null ? 0.0 : model.getApprovalDistance();
        }
        obsTotalTasks.set(totalTasks);
        obsTotalKmTravelled.set(totalKMTravelled);
        obsTotalDeduction.set(totalDeduction);
        obsTotalKmApproved.set(totalKMApproved);
    }
}