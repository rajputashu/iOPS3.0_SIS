package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.models.geolocation.GPSLocationMO;
import com.sisindia.ai.android.models.geolocation.GeoLocationApiBodyMO;
import com.sisindia.ai.android.room.dao.GeoLocationDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LocationWorker extends BaseWorker {

    @Inject
    GeoLocationDao geoLocationDao;

    @Inject
    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        addDisposable(geoLocationDao.fetchAllGeoPings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadGeoPingsToServer, Timber::e));

        return Result.success();
    }

    private void uploadGeoPingsToServer(List<GPSLocationMO> pingList) {
        if (!pingList.isEmpty()) {
            GeoLocationApiBodyMO geoLocationBody = new GeoLocationApiBodyMO(pingList);
            addDisposable(gpsApi.uploadGeoPingsToServer(geoLocationBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(task -> {
                        if (task.isDone) {
                            ArrayList<Integer> ids = new ArrayList<>(pingList.size());
                            for (GPSLocationMO model : pingList)
                                ids.add(model.pingId);
                            deletingUploadedGeoPingsToServer(ids);
                        }
                    }, this::onApiError));
        }
    }

    private void deletingUploadedGeoPingsToServer(ArrayList<Integer> ids) {
        if (ids.size() > 0) {
            addDisposable(geoLocationDao.deleteUploadedPings(ids)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                    }, Timber::e));
        }
    }
}