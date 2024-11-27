package com.sisindia.ai.android.features.imagecapture

import android.text.TextUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sisindia.ai.android.room.entities.AttachmentEntity

object ImageCaptureViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["setImagePreview"])
    fun AppCompatImageView.setImagePreview(imagePath: String?) {
        if (!TextUtils.isEmpty(imagePath)) {
            Glide.with(this).load(imagePath).into(this)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setSourceType"])
    fun AppCompatTextView.setSourceType(sourceTypeId: Int) {
        val sourceType = AttachmentEntity.AttachmentSourceType.of(sourceTypeId)
        text = sourceType.sourceName
    }


}