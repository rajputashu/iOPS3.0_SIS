package com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.commons.SpinnersListener;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.LookUpType;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.EmployeeFineRewardDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity;
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity.RewardType;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;
import com.sisindia.ai.android.uimodels.RewardFineCategory;
import com.sisindia.ai.android.uimodels.RewardFineReasonMO;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GuardAddRewardFineViewModel extends IopsBaseViewModel {

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public EmployeeFineRewardDao fineRewardDao;

    @Inject
    public LookUpDao lookUpDao;

    public ObservableField<CheckedGuardItemModel> selectedGuardObs = new ObservableField<>(new CheckedGuardItemModel());
    public ObservableField<String> deservesRewardOrFine = new ObservableField<>("");
    public ObservableField<RewardType> rewardTypeObs = new ObservableField<>(RewardType.UNKNOWN);
    public ObservableField<List<RewardFineCategory>> categoryObs = new ObservableField<>(new ArrayList<>());
    public ObservableField<RewardFineCategory> selectedCategoryObs = new ObservableField<>(new RewardFineCategory());
    public ObservableBoolean isBtnEnable = new ObservableBoolean();
    public ObservableBoolean isRewardAdded = new ObservableBoolean(false);
    private CheckedGuardEntity checkedGuard;
    public ObservableField<ArrayList<String>> obsRewardOrPunishmentReasons = new ObservableField<>(new ArrayList<>());
    public int selectedPos = 0;
    private List<RewardFineReasonMO> rewardOrFineReasonsList;
    private int lastRewardOrFineAmount = 0;

    public SpinnersListener listener = new SpinnersListener() {
        @Override
        public void onSpinnerItemAndTypeSelected(int pos, int typeId) {
        }

        @Override
        public void onSpinnerOptionSelected(int pos) {
            selectedPos = pos;
        }

        @Override
        public void onSpinnerOptionSelected(@NotNull Object item) {

        }
    };

    @Inject
    public GuardAddRewardFineViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCheckedChange(ChipGroup chipGroup, int check) {
        isBtnEnable.set(check != -1);
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                RewardFineCategory category = (RewardFineCategory) chip.getTag();
                if (category != null)
                    selectedCategoryObs.set(category);
                return;
            } else {
                selectedCategoryObs.set(new RewardFineCategory());
            }
        }
    }

    public void onAddRewardFineDone(View view) {
        if (lastRewardOrFineAmount > 200) {
            Toast.makeText(view.getContext(), "Reward or Fine cannot be more than Rs 200 in a month", Toast.LENGTH_LONG).show();
        } else if (selectedPos == 0) {
            Toast.makeText(view.getContext(), "Please select reason", Toast.LENGTH_LONG).show();
        } else {
            isRewardAdded.set(true);
            message.what = NavigationConstants.ON_REWARD_FINE_SELECTED;
            liveData.postValue(message);
        }
    }

    public void onRewardUndoClick(View view) {
        isRewardAdded.set(false);
        isBtnEnable.set(false);
        rewardTypeObs.set(EmployeeFineRewardEntity.RewardType.UNKNOWN);
    }

    public void onContinueToAddSignature(View view) {
//        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int taskId = Prefs.getInt(PrefConstants.TASK_SERVER_ID); // Earlier we are using CURRENT_TASK
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);
        String employeeNo = Prefs.getString(PrefConstants.SELECTED_EMPLOYEE_NO, "NA");

        EmployeeFineRewardEntity.RewardType rewardType = Objects.requireNonNull(rewardTypeObs.get());
        int rewardTypeId = (rewardType == EmployeeFineRewardEntity.RewardType.REWARD) ? LookUpType.REWARD_VALUES.getTypeId() : LookUpType.FINE_VALUES.getTypeId();
        RewardFineCategory category = selectedCategoryObs.get();
        if (rewardType != EmployeeFineRewardEntity.RewardType.UNKNOWN && category != null && category.id != 0) {
            if (isRewardAdded.get()) {
                int reasonId = 0;
                String reasonName = "";
                if (rewardOrFineReasonsList != null && rewardOrFineReasonsList.size() > 0 && isRewardAdded.get()) {
                    reasonId = rewardOrFineReasonsList.get(selectedPos - 1).getLookupIdentifier();
                    reasonName = rewardOrFineReasonsList.get(selectedPos - 1).getDisplayName();
                }
                EmployeeFineRewardEntity fineRewardEntity = new EmployeeFineRewardEntity(taskId, postId, siteId, employeeId, rewardType.getRewardType(),
                        category.lookupIdentifier, category.displayName, rewardTypeId, reasonId, reasonName, employeeNo);

                addDisposable(fineRewardDao.insert(fineRewardEntity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onRewardSuccess, Timber::e));
            } else {
                onRewardSuccess(0L);
            }
        } else {
            onRewardSuccess(0L);
        }
    }

    private void onRewardSuccess(Long fineRewardId) {

        if (checkedGuard == null) {
            showToast("unable to update the record..");
            return;
        }
        checkedGuard.updatedDateTime = LocalDateTime.now().toString();
        checkedGuard.fineRewardId = fineRewardId.intValue();
        checkedGuard.currentState = CheckedGuardEntity.CurrentState.REWARD_FINE.getGuardStatus();

        addDisposable(dayCheckDao.updateCheckedGuard(checkedGuard)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGuardUpdated, Timber::e));
    }

    private void onGuardUpdated(Integer integer) {
        message.what = NavigationConstants.ON_ADD_SIGNATURE_CLICK;
        liveData.postValue(message);
//        oneTimeWorkerWithNetwork(AddRewardFineWorker.class);
    }

    public void onAddRewardClick(View view) {
        rewardTypeObs.set(EmployeeFineRewardEntity.RewardType.REWARD);
        fetchLastRewardOrFineAmount();
        message.what = NavigationConstants.ON_ADD_REWARD_CLICK;
        liveData.postValue(message);
    }

    public void onAddFineClick(View view) {
        rewardTypeObs.set(EmployeeFineRewardEntity.RewardType.FINE);
        fetchLastRewardOrFineAmount();
        message.what = NavigationConstants.ON_ADD_FINE_CLICK;
        liveData.postValue(message);
    }

    private void fetchLastRewardOrFineAmount() {
        addDisposable(fineRewardDao
                .getLastRewardOrFineAmount(Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID),
                        Objects.requireNonNull(rewardTypeObs.get()).getRewardType())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLastAmountFetched, Timber::e));
    }

    private void onLastAmountFetched(Integer amount) {
        Timber.e("Last Amount Reward Fine : %s", amount);
        if (amount != null && amount > 0)
            lastRewardOrFineAmount = amount;
    }

    void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);

        addDisposable(dayCheckDao.fetchCheckedGuard(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> checkedGuard = item, Timber::e));

        addDisposable(dayCheckDao.fetchGuardDetailAfterScan(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (item != null) {
                        selectedGuardObs.set(item);
                        if (item.totalTurnOut != 0)
                            deservesRewardOrFine.set(((item.turnOutScore * 100 / item.totalTurnOut)) <= 90 ? "Do you want to impose fine for " + item.employeeName : item.employeeName + " deserves a reward!");
                    }
                }, Timber::e));
    }

    public void fetchOptionValues() {
        EmployeeFineRewardEntity.RewardType rewardType = Objects.requireNonNull(rewardTypeObs.get());
        LookUpType type = (rewardType == EmployeeFineRewardEntity.RewardType.REWARD) ? LookUpType.REWARD_VALUES : LookUpType.FINE_VALUES;
        addDisposable(lookUpDao.fetchAllCategoryForFineReward(type.getTypeId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> categoryObs.set(items), Timber::e));
    }

    public void fetchReasons(boolean isReward) {
        ArrayList<String> reasons = new ArrayList<>();
        reasons.add("Select Reason");
        int lookUpTypeId = isReward ? 65 : 66;
        addDisposable(lookUpDao.fetchRewardOrFineReasons(lookUpTypeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reasonList -> {
                    if (reasonList != null && reasonList.size() > 0) {
                        rewardOrFineReasonsList = reasonList;
                        for (RewardFineReasonMO model : reasonList) {
                            reasons.add(model.getDisplayName());
                        }
                        obsRewardOrPunishmentReasons.set(reasons);
                    }
                }, Timber::e));
    }
}

