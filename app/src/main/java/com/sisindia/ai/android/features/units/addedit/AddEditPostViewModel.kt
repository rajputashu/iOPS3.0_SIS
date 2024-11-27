package com.sisindia.ai.android.features.units.addedit

import android.app.Application
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.room.EmptyResultSetException
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.droidcommons.preference.Prefs
import com.google.android.gms.maps.model.LatLng
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.uar.dialog.DialogListener
import com.sisindia.ai.android.models.site.AddEditPostsMO
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.SitePostDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.SitePostEntity
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.SitePostsWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class AddEditPostViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var sitePostDao: SitePostDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    private var geoLatitude = ObservableField(0.0)
    private var geoLongitude = ObservableField(0.0)
    var geoLocation = ObservableField("")
    var mMapLatLng = ObservableField<LatLng>()

    var toolBarTitle = ObservableField(app.resources.getString(R.string.string_add_post))
    var postName = ObservableField("")
    var isMainGate = ObservableField(false)
    var isEditModeOn = ObservableField(false)

    var isEquipmentListVisible = ObservableInt(View.VISIBLE)
    private var equipmentList: ArrayList<EquipmentsMO>? = ArrayList()
    var postQRCode = ObservableField("")
    var postImageUri = ObservableField<Uri>()
    var spiImageUri = ObservableField<Uri>()
    var postImageMetaData = ObservableField("")
    var spiImageMetaData = ObservableField("")
    var isEditingPostImage = ObservableField(false)
    var isEditingSpiImage = ObservableField(false)
    var isQRDialogFlag = ObservableField(true)

    private var replaceablePostId = 0

    val equipAdapter = EquipmentAdapter()

    var sitePost = ObservableField(SitePostEntity())

    val dialogListener = object : DialogListener {
        override fun onCrossButtonClick() {
        }

        override fun onViewAllPOAClick() {
        }

        override fun onYesButtonClicked() {
            if (!isQRDialogFlag.get()!!) {
                //                replaceAndAddPost()
                if (isEditModeOn.get()!!)
                    updateExistingPostToDB(true)
                else
                    addNewPostToDB(true)
            }
        }

        override fun onNoButtonClicked() {
        }
    }

    fun getLatLongFromService() {
        geoLatitude.set(Prefs.getDouble(PrefConstants.LATITUDE))
        geoLongitude.set(Prefs.getDouble(PrefConstants.LONGITUDE))
        geoLocation.set(geoLatitude.get().toString() + "," + geoLongitude.get()!!)
        mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
    }

    fun fetchPostDetailsForEdit(postId: Int) {
        Prefs.putInt(PrefConstants.CURRENT_POST_ID_FOR_EDIT, postId)
        setIsLoading(true)
        addDisposable(sitePostDao.getItemByPrimaryKeyId(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setIsLoading(false)
                sitePost.set(it)
                postName.set(it.postName)
                isMainGate.set(it.isMainPost)
                postQRCode.set(it.dutyPostCode)
                isEditingPostImage.set(it.qRenabled)
                isEditingSpiImage.set(it.spiEnabled)
                if (!it.postGeoPointString.isNullOrEmpty() && it.postGeoPointString.contains(",")) {
                    try {
                        val location = it.postGeoPointString.replace(" ", "").split(",")
                        geoLocation.set(it.postGeoPointString)
                        geoLatitude.set(location[0].toDouble())
                        geoLongitude.set(location[1].toDouble())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
                setIsLoading(true)
            }))
    }

    fun updateAddedEquipment(equipmentsMO: EquipmentsMO) {
        equipmentList!!.apply {
            add(equipmentsMO)
        }
        if (equipmentList!!.size > 0)
            isEquipmentListVisible.set(View.GONE)

//        Log.e("AddEditPost","EquipmentList : $equipmentList")

        equipAdapter.updateEquipmentsList(equipmentList!!)
    }

    private fun addNewPostToDB(isNeedToReplacePost: Boolean = false) {
        setIsLoading(true)
        val post = sitePost.get()
        //        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)

        if (post == null) {
            showToast("Unable to save post on Null")
            return
        }

        post.postName = postName.get()
        post.isMainPost = isMainGate.get()!!
        post.postGeoPointString = geoLocation.get().toString()
        if (postQRCode.get().toString().isEmpty()) {
            post.qRenabled = false
            post.dutyPostCode = ""
        } else {
            post.qRenabled = true
            post.dutyPostCode = postQRCode.get().toString()
        }

        post.spiEnabled = spiImageUri.get() != null
        post.isCreated = 0
        post.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        post.description = ""
        post.isActive = true
        post.createdBy = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID)
        post.updatedBy = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID)
        //        post.isSynced = 0
        post.isSynced = 1 //{changing to 1 as checking from API first}
        post.createdDateTime = LocalDateTime.now().toString()
        post.updatedDateTime = LocalDateTime.now().toString()

        //USING BELOW MODEL TO POST TO SERVER
        val apiPostDetailsMO =
            AddEditPostsMO(id = post.id,
                postName = post.postName,
                dutyPostCode = post.dutyPostCode,
                description = post.description,
                qrEnabled = post.qRenabled,
                isActive = post.isActive,
                isMainPost = post.isMainPost,
                siteId = post.siteId,
                isCreated = post.isCreated,
                postGeoPointString = post.postGeoPointString,
                spiEnabled = post.spiEnabled)

        addDisposable(coreApi.addEditPosts(apiPostDetailsMO)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                if (data.statusCode == 200) {
                    if (data?.data != null && data.data.serverId != 0)
                        if (isNeedToReplacePost) {
                            //                            Timber.e("Post : Coming to ADDDDD the post and replace Main Gate")
                            replaceAndAddPost()
                            saveAddedPostToLocalDB(post)
                        } else
                            saveAddedPostToLocalDB(post)
                } else {
                    setIsLoading(false)
                    // showToast(data.statusMessage)
                    onShowDialogWithServerMsg(data.statusMessage)
                }
            }, { onShowDialogWithServerMsg("Unable to add post, please try again!") }))

        //DON'T DELETE {MAY REQUIRED IN FUTURE}
        /*addDisposable(sitePostDao.isDuplicatePost(siteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setIsLoading(false)
                if (!it.contains(post.postName))
                    saveAddedPostToLocalDB(post)
                else
                    showToast("Post with same name... already exists")
            }, { throwable: Throwable? ->
                showToast("Unable to save Post on Error")
                this.onError(throwable!!)
            }))*/
    }

    /*
    * Below method is used to save newly added post to local DB after validating from API
    * */
    private fun saveAddedPostToLocalDB(addedPostEntity: SitePostEntity) {
        addDisposable(sitePostDao.insert(addedPostEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showToast("Post added Successfully.")
                //                Timber.e("Post : Coming to ADDD ")
                onSaveSuccessfully()
                updatePostAttachment()
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun updateExistingPostToDB(isNeedToReplacePost: Boolean = false) {
        setIsLoading(true)
        val post = sitePost.get()
        if (post == null) {
            showToast("Unable to update the post")
            return
        }
        post.postName = postName.get().toString()
        post.isMainPost = isMainGate.get()!!
        if (postQRCode.get().toString().isNotEmpty()) {
            post.qRenabled = true
            post.dutyPostCode = postQRCode.get().toString()
        }
        post.isCreated = 1
        post.postGeoPointString = geoLocation.get().toString()
        post.spiEnabled = isEditingSpiImage.get()!!
        post.isSynced = 1 //{Changed to 1 : As syncing data on front}
        post.updatedDateTime = LocalDateTime.now().toString()

        //USING BELOW MODEL TO POST TO SERVER
        val apiPostDetailsMO =
            AddEditPostsMO(id = post.id,
                postName = post.postName,
                dutyPostCode = post.dutyPostCode,
                description = post.description,
                qrEnabled = post.qRenabled,
                isActive = post.isActive,
                isMainPost = post.isMainPost,
                siteId = post.siteId,
                isCreated = post.isCreated,
                postGeoPointString = post.postGeoPointString,
                spiEnabled = post.spiEnabled)

        addDisposable(coreApi.addEditPosts(apiPostDetailsMO)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                if (data.statusCode == 200) {
                    if (data?.data != null && data.data.serverId != 0)
                        if (isNeedToReplacePost) {
                            //                            Timber.e("Post : Coming to update the post and replace Main Gate")
                            replaceAndAddPost()
                            updateEditedPostToLocalDB(post)
                        } else
                            updateEditedPostToLocalDB(post)
                } else {
                    setIsLoading(false)
                    //                    showToast(data.statusMessage)
                    onShowDialogWithServerMsg(data.statusMessage)
                }
            }, { onShowDialogWithServerMsg("Unable to edit post, please try again!") }))
    }

    private fun updateEditedPostToLocalDB(post: SitePostEntity) {
        addDisposable(sitePostDao.update(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("Post : Coming to update the post")
                onSaveSuccessfully()
                updatePostAttachment()
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun onSaveSuccessfully() {
        setIsLoading(false)
        //        syncAddEditedPostToServer()
        Prefs.remove(PrefConstants.CURRENT_POST_ID_FOR_EDIT)
        message.what = NavigationConstants.ON_SUCCESS_ADD_EDIT_POST
        liveData.postValue(message)
    }

    private fun onError(throwable: Throwable) {
        setIsLoading(false)
        Timber.e(throwable)
        Toast.makeText(getApplication(), "Error while saving/edit post", Toast.LENGTH_SHORT).show()
    }

    private fun onShowDialogWithServerMsg(errorMsg: String) {
        setIsLoading(false)
        message.what = NavigationConstants.ON_SHOWING_ERROR_MESSAGE
        message.obj = errorMsg
        liveData.postValue(message)
    }

    private fun updatePostAttachment() {
        try {
            val attachmentList = ArrayList<AttachmentEntity>()
            if (postImageUri.get() != null && Uri.EMPTY != postImageUri.get()) {
                val postImageMetaDataJson = JSONObject(postImageMetaData.get().toString())
                if (!isEditingPostImage.get()!!) {
                    // If NEW POST IMAGE -> THEN REPLACE POST NAME
                    var newStoragePath = postImageMetaDataJson.getString("storagePath")
                    newStoragePath = newStoragePath.replace("@ReplacePostName",
                        postName.get().toString())
                    postImageMetaDataJson.put("storagePath", newStoragePath)
                }

                attachmentList.add(AttachmentEntity(postImageUri.get().toString(),
                    postImageMetaDataJson.toString()))
            }

            if (spiImageUri.get() != null && Uri.EMPTY != spiImageUri.get()) {
                val spiImageMetaDataJson = JSONObject(spiImageMetaData.get().toString())
                if (!isEditingSpiImage.get()!!) {
                    var newStoragePath = spiImageMetaDataJson.getString("storagePath")
                    newStoragePath = newStoragePath.replace("@ReplacePostName",
                        postName.get().toString())
                    spiImageMetaDataJson.put("storagePath", newStoragePath)
                }
                attachmentList.add(AttachmentEntity(spiImageUri.get().toString(),
                    spiImageMetaDataJson.toString()))
            }

            if (attachmentList.isNotEmpty()) {
                addDisposable(attachmentDao.insertAll(attachmentList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                    }, { throwable: Throwable? ->
                        throwable!!.printStackTrace()
                    }))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.scanPostQRView -> message.what = NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN
            R.id.takePostPhotoView -> message.what = NavigationConstants.ON_TAKE_PICTURE
            R.id.takeSPIPhotoView -> message.what = NavigationConstants.ON_POST_SPI_TAKE_PICTURE
            R.id.addEquipmentButtonView -> message.what = NavigationConstants.OPEN_ADD_EQUIPMENT
            R.id.locationRefreshButton -> refreshGeoLocation()
            R.id.mapRegisterButtonView -> message.what =
                NavigationConstants.OPEN_MAP_REGISTERS_SCREEN
        }
        liveData.postValue(message)
    }

    fun onSaveBtnClick(view: View) {
        val isEdit = isEditModeOn.get()!!
        val postName = postName.get()
        val isMain = isMainGate.get()!!
        val postEntity = sitePost.get()

        if (TextUtils.isEmpty(postName)) {
            showToast("Please enter valid post name")
            setIsLoading(false)
            return
        } /*else if (!isEdit) {
            addDisposable(sitePostDao.isPostNameExist(Prefs.getInt(PrefConstants.CURRENT_SITE),
                postName!!.toUpperCase(Locale.getDefault()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it > 0) {
                        showToast("Post name already exists on this site")
                        setIsLoading(false)
                    } else
                        checkingMainPostLogic(isMain, isEdit, postEntity!!.isMainPost)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))
        } else if (postEntity != null && !TextUtils.isEmpty(postEntity.postName) && (!postEntity.postName.equals(postName, ignoreCase = true))) {
            addDisposable(sitePostDao.isPostNameExist(Prefs.getInt(PrefConstants.CURRENT_SITE),
                postName!!.toUpperCase(Locale.getDefault()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it > 0) {
                        showToast("Post name already exists on this site")
                        setIsLoading(false)
                    } else
                        checkingMainPostLogic(isMain, isEdit, postEntity.isMainPost)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))
        } else
            checkingMainPostLogic(isMain, isEdit, postEntity!!.isMainPost)*/

        checkingMainPostLogic(isMain, isEdit, postEntity!!.isMainPost)
    }

    //If User checked Post as MainGate than checking is MainGate already exist or not
    private fun checkingMainPostLogic(isMain: Boolean, isEdit: Boolean, dbIsMainValue: Boolean) {
        when {
            !dbIsMainValue && isMain -> {
                addDisposable(sitePostDao.isMainPostExist(Prefs.getInt(PrefConstants.CURRENT_SITE))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { rowId, throwable ->
                        if (isEdit) {
                            //update
                            if (throwable != null && throwable is EmptyResultSetException) {
                                showToast("Updating Post to the Site as Main Gate")
                                updateExistingPostToDB()
                            } else if (isMain && rowId != null && rowId != 0) {
                                replaceablePostId = rowId
                                message.what = NavigationConstants.ON_REPLACING_MAIN_GATE
                                liveData.postValue(message)
                            }
                        } else {
                            if (isMain && rowId != null && rowId != 0) {
                                replaceablePostId = rowId
                                message.what = NavigationConstants.ON_REPLACING_MAIN_GATE
                                liveData.postValue(message)
                            } else
                                addNewPostToDB()
                        }
                    })
            }
            isEdit -> updateExistingPostToDB()
            else -> addNewPostToDB()
        }
    }

    fun checkDuplicateQRCode(scannedQrCode: String) {
        addDisposable(sitePostDao.isDuplicateQRExist(scannedQrCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ count ->
                if (count > 0) {
                    message.what = NavigationConstants.ON_SHOWING_ERROR_MESSAGE
                    liveData.postValue(message)
                } else {
                    postQRCode.set(scannedQrCode)
                }
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun syncAddEditedPostToServer() {
        val request = OneTimeWorkRequest.Builder(SitePostsWorker::class.java).build()
        WorkManager.getInstance(getApplication()).enqueue(request)
    }

    private fun refreshGeoLocation() {
        if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
            geoLatitude.set(Prefs.getDouble(PrefConstants.LATITUDE))
            geoLongitude.set(Prefs.getDouble(PrefConstants.LONGITUDE))
            geoLocation.set(Prefs.getDouble(PrefConstants.LATITUDE)
                .toString() + " , " + Prefs.getDouble(PrefConstants.LONGITUDE).toString())
            mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
            message.what = NavigationConstants.ON_REFRESHING_LOCATION
            liveData.postValue(message)
        } else
            showToast(R.string.please_turn_on_duty)
    }

    private fun replaceAndAddPost() {
        //        val isEdit = isEditModeOn.get()!!
        addDisposable(sitePostDao.replaceAndUpdateMainPost(replaceablePostId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                /*if (isEdit)
                    updateExistingPostToDB()
                else
                    addNewPostToDB()*/
                Timber.e("Main gate of post replaced...")
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
                showToast("Error replacing main post...")
            }))
    }
}