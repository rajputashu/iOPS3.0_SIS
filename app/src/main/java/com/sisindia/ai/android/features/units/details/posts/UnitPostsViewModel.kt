package com.sisindia.ai.android.features.units.details.posts

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableInt
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.uar.dialog.DialogListener
import com.sisindia.ai.android.room.dao.SitePostDao
import com.sisindia.ai.android.uimodels.SitePostModel
import com.sisindia.ai.android.workers.SitePostsWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class UnitPostsViewModel @Inject constructor(app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var sitePostDao: SitePostDao
    val isPostAvailable: ObservableInt = ObservableInt(View.GONE)
    val unitPostAdapter = UnitPostsAdapter()
    var postIdToDelete: Int = 0

    val postListener = object : PostsListener {
        override fun onPostSelected(postId: Int) {
            message.what = NavigationConstants.OPEN_ADD_EDIT_POSTS
            message.arg1 = postId
            liveData.postValue(message)
        }

        override fun onDeletingPost(postId: Int) {
            postIdToDelete = postId
            message.what = NavigationConstants.ON_DELETING_POST
            liveData.postValue(message)
        }
    }

    val dialogListener = object : DialogListener {
        override fun onCrossButtonClick() {
        }

        override fun onViewAllPOAClick() {
        }

        override fun onYesButtonClicked() {
            Timber.e("Yes Button is clicked go for delete....")
            deleteSelectedPostFromDB()
        }

        override fun onNoButtonClicked() {
        }
    }

    fun onAddPost(view: View) {
        message.what = NavigationConstants.OPEN_ADD_EDIT_POSTS
        message.arg1 = 0
        liveData.postValue(message)
    }

    fun getUnitPostsDetails() {
        setIsLoading(true)
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(sitePostDao.fetchAllPostsBySiteId(siteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ entity: List<SitePostModel>? ->
                if (entity!!.isNotEmpty()) {
                    this.onUnitPostFetch(entity)
                    isPostAvailable.set(View.GONE)
                } else
                    isPostAvailable.set(View.VISIBLE)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun onUnitPostFetch(list: List<SitePostModel>) {
        setIsLoading(false)
        unitPostAdapter.clearAndSetItems(list)
    }

    private fun deleteSelectedPostFromDB() {
        if (postIdToDelete != 0) {
            addDisposable(sitePostDao.updatePostToDelete(postIdToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rowId: Int? ->
                    if (rowId!! > 0) {
                        syncDeletedPostToServer()
                        getUnitPostsDetails()
                    }
                    showToast("Post deleted successfully")
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))
        }
    }

    private fun onError(throwable: Throwable) {
        setIsLoading(false)
        Timber.e(throwable)
        Toast.makeText(getApplication(), "Error while fetching post", Toast.LENGTH_SHORT).show()
    }

    private fun syncDeletedPostToServer() {
        val request = OneTimeWorkRequest.Builder(SitePostsWorker::class.java).build()
        WorkManager.getInstance(getApplication()).enqueue(request)
    }
}