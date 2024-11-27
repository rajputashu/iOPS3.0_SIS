package com.sisindia.ai.android.features.recruitment.sheet

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.room.dao.RecruitmentDao
import com.sisindia.ai.android.room.entities.AddRecruitmentEntity
import com.sisindia.ai.android.workers.AddRecruitmentWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/7/2020.
 */
class AddRecruitmentViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    var obsRecruitName = ObservableField<String>("")
    var obsRecruitPhoneNo = ObservableField<String>("")
    var obsRecruitPanCard = ObservableField<String>("")
    var obsRecruitDOB = ObservableField<String>("MM-DD-YY")
    var obsDateOfBirthForAPI = ObservableField<String>("")

    @Inject
    lateinit var recruitmentDao: RecruitmentDao

    fun onDatePickerClicked(view: View) {
        message.what = NavigationConstants.OPEN_DATE_PICKER
        liveData.postValue(message)
    }

    fun onAddBtnClick(view: View) {
        if (obsRecruitName.get().toString().isEmpty() || obsRecruitName.get().toString().trim().isEmpty())
            showToast("Please enter valid name")
        else if (obsRecruitPhoneNo.get().toString().isEmpty() || obsRecruitPhoneNo.get().toString().length < 10)
            showToast("Please enter valid mobile number")
        else if (obsRecruitPanCard.get().toString().isEmpty() || obsRecruitPanCard.get().toString().trim().isEmpty() ||
            obsRecruitPanCard.get().toString().length < 12)
            showToast("Please enter valid aadhar number")
        else if (obsRecruitDOB.get().toString().equals("MM-DD-YY", ignoreCase = true))
            showToast("Please select date of birth from calendar")
        else
            validateNumberAlreadyExists()
    }

    private fun validateNumberAlreadyExists() {
        addDisposable(recruitmentDao.isMobileNumberExists(obsRecruitPhoneNo.get().toString())!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ count ->
                if (count == 0)
                    insertRecruitmentDetailsToDB()
                else
                    showToast(app.resources.getString(R.string.valid_msg_client_number_exist))
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun insertRecruitmentDetailsToDB() {
        val addRecruitmentMO = AddRecruitmentEntity(obsRecruitName.get().toString(),
            obsRecruitPhoneNo.get().toString(),
            obsRecruitPanCard.get().toString(),
            obsDateOfBirthForAPI.get().toString())
        addDisposable(recruitmentDao.insert(addRecruitmentMO)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showToast("Recruitment details added successfully")
                oneTimeWorkerWithNetwork(AddRecruitmentWorker::class.java)
                message.what = NavigationConstants.ON_RECRUITMENT_ADDED_SUCCESSFULLY
                liveData.postValue(message)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }
}