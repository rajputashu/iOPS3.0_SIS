package com.sisindia.ai.android.rest

import io.reactivex.Completable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by Ashu Rajput on 4/14/2020.
 */
interface AttachmentUploadAPI {
    @Deprecated("Use uploadFileWithHeader")
    @Multipart
    @Headers("x-ms-blob-type: BlockBlob", "Content-Length: 0")
    @PUT
    fun uploadFile(@Url completeURl: String?, @Part filePart: MultipartBody.Part?): Completable?

    /*@Multipart
    @PUT
    fun uploadFileWithHeader(@HeaderMap headers: Map<String, @JvmSuppressWildcards Any>,
        @Url completeURl: String?, @Part filePart: MultipartBody.Part?): Completable?*/

    @PUT
    fun uploadFileWithHeader(@HeaderMap headers: Map<String, @JvmSuppressWildcards Any>,
        @Url completeURl: String?, @Body filePart: RequestBody?): Completable?
}