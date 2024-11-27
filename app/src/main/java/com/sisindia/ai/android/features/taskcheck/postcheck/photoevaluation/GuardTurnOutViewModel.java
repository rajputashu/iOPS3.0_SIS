package com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.LookUpType;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;
import com.sisindia.ai.android.utils.GsonUtils;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GuardTurnOutViewModel extends IopsBaseViewModel {

    public GuardTurnOutEditRecyclerAdapter recyclerAdapter = new GuardTurnOutEditRecyclerAdapter();
    public ObservableField<CheckedGuardEntity> checkedGuard = new ObservableField<>(new CheckedGuardEntity());

    @Inject
    public LookUpDao lookUpDao;

    @Inject
    public DayCheckDao dayCheckDao;

    public List<GuardTurnOutResult.GuardTurnoutModel> receivedTurnOutList = new ArrayList<>();
    public List<GuardTurnOutResult.GuardTurnoutModel> receivedMLTurnOutList = new ArrayList<>();


    @Inject
    public GuardTurnOutViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {

        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);

        addDisposable(dayCheckDao.fetchCheckedGuard(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGuardFetched, Timber::e));
    }

    private void onGuardFetched(CheckedGuardEntity item) {

        if (receivedTurnOutList.isEmpty()) {
            Timber.e("LIstSize coming in if received list is empty");
            if (TextUtils.isEmpty(item.guardEvaluationResult)) {
                addDisposable(lookUpDao.fetchGuardTurnOut(LookUpType.GUARD_TURNOUT.getTypeId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(list -> {
                            /*item.totalTurnOut = list.size();
                            checkedGuard.set(item);
                            recyclerAdapter.clearAndSetItems(list);*/
                            updateGuardTurnOutRV(item, list);
                        }, Timber::e));
            } else {
                GuardTurnOutResult result = new Gson().fromJson(item.guardEvaluationResult, GuardTurnOutResult.class);
                updateGuardTurnOutRV(item, result.turnOutResult);
            }

        } else {
            Timber.e("LIstSize coming in if received list IS NOT EMPTY");
            updateGuardTurnOutRV(item, receivedTurnOutList);
        }
    }

    private void updateGuardTurnOutRV(CheckedGuardEntity item, List<GuardTurnOutResult.GuardTurnoutModel> list) {
        item.totalTurnOut = list.size();
        checkedGuard.set(item);
        recyclerAdapter.clearAndSetItems(list);
    }

    public void onSaveChangesClick(View view) {

        /*GuardTurnOutResult result = new GuardTurnOutResult();
        result.turnOutResult = recyclerAdapter.getItems();
        result.mlTurnOutResult = receivedMLTurnOutList;*/

        //Creating model for manual/editing guardTurnOutResult
        GuardTurnOutResult result = new GuardTurnOutResult();
        result.turnOutResult = recyclerAdapter.getItems();

        //Creating model for ML detection guardTurnOutResult
        GuardTurnOutResult mlResult = new GuardTurnOutResult();
        mlResult.mlTurnOutResult = receivedMLTurnOutList;

        int turnOut = 0;
        for (GuardTurnOutResult.GuardTurnoutModel model : result.turnOutResult) {
            if (model.isSelected) {
                turnOut += 1;
            }
        }

        if (turnOut != 0) {
            CheckedGuardEntity item = checkedGuard.get();
            if (item == null) {
                showToast("Unable to save Guard");
                return;
            }
            item.turnOutScore = turnOut;
            item.guardEvaluationResult = GsonUtils.toJsonWithoutExopse().toJson(result);
            item.mlGuardEvaluationResult = GsonUtils.toJsonWithoutExopse().toJson(mlResult);
            item.currentState = CheckedGuardEntity.CurrentState.EVALUATION.getGuardStatus();
            item.updatedDateTime = LocalDateTime.now().toString();

            addDisposable(dayCheckDao.updateCheckedGuard(item)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> {
                        message.what = NavigationConstants.ON_GUARD_EVALUATION_EDITED;
                        liveData.postValue(message);
                    }, Timber::e));
        } else {
            showToast("Please select at least one TurnOut to continue.");
        }
    }
}
