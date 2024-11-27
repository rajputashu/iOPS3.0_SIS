package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.models.site.AddEditPostsMO
import com.sisindia.ai.android.room.dao.SitePostDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/27/2020.
 */
class SitePostsWorker @Inject constructor(context: Context, workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var sitePostDao: SitePostDao

    override fun doWork(): Result {
        addDisposable(sitePostDao.fetchAllCompletedButNotSynced()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ poaList ->
                if (poaList.isNotEmpty())
                    uploadAddEditPostToServer(poaList)
            }, { }))
        return Result.success()
    }

    private fun uploadAddEditPostToServer(poaList: List<AddEditPostsMO>) {
        for (post: AddEditPostsMO in poaList) {
            val tableId = post.id
            if (post.isCreated == 0) {
                post.id = 0
            }
            addDisposable(coreApi.addEditPosts(post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    if (data?.data != null && data.data.serverId != 0) {
                        addDisposable(sitePostDao.updateOnServerSync(tableId, data.data.serverId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.e("Post update success")
                            }) { Timber.e(it) })
                    }
                }, this::onApiError))
        }
    }
}