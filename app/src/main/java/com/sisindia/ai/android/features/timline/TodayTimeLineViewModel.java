package com.sisindia.ai.android.features.timline;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.room.dao.DutyStatusDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.DutyStatusEntity;
import com.sisindia.ai.android.uimodels.TimeLineModel;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TodayTimeLineViewModel extends IopsBaseViewModel {

    public TimeLineAdapter todayAdapter = new TimeLineAdapter();
    public ObservableInt noDataAvailable = new ObservableInt(GONE);

    @Inject
    public DutyStatusDao dutyStatusDao;

    @Inject
    public TaskDao taskDao;

    @Inject
    public TodayTimeLineViewModel(@NonNull Application application) {
        super(application);
    }


    public void initViewModel() {

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.now().with(LocalTime.of(5, 0, 0));

//        Timber.e("End " + end.toString() + "- Start " + start.toString());

        addDisposable(dutyStatusDao.fetchDutyStatusBetween(start.toString(), end.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    noDataAvailable.set(list.size() == 0 ? VISIBLE : GONE);

                    for (DutyStatusEntity item : list) {
                        TimeLineModel model = new TimeLineModel();
                        model.dutyOffDateTime = item.dutyOffDateTime;
                        model.dutyOnDateTime = item.dutyOnDateTime;

                        String startTime = TextUtils.isEmpty(model.dutyOnDateTime) ? LocalDateTime.now().toString() : model.dutyOnDateTime;
                        String endTime = TextUtils.isEmpty(model.dutyOffDateTime) ? LocalDateTime.now().toString() : model.dutyOffDateTime;

                        Timber.e("START " + startTime + "- END " + endTime);

                        addDisposable(taskDao.fetchAllCompletedTasksBetween(startTime, endTime)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(tasks -> {
                                    model.tasks = tasks;
                                    todayAdapter.add(model);
                                }, Timber::e));
                    }
                }, Timber::e));
    }
}
