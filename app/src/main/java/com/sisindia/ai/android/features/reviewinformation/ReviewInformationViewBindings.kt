package com.sisindia.ai.android.features.reviewinformation

import android.text.TextUtils
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.sisindia.ai.android.models.ReviewInformationResponse
import com.sisindia.ai.android.utils.TimeUtils.LAST_VISIT_DATE_TIME
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.abs

object ReviewInformationViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["setSiteLastVisitDateTime"])
    fun AppCompatTextView.setSiteLastVisitDateTime(lastVisitDetail: ReviewInformationResponse.IssueSummary) {
        text = if (lastVisitDetail.lastTaskDone == null) {
            "NA"
        } else {
            LocalDateTime.parse(lastVisitDetail.lastTaskDone)
                .format(DateTimeFormatter.ofPattern(LAST_VISIT_DATE_TIME))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setSiteLastVisitStrength"])
    fun AppCompatTextView.setSiteLastVisitStrength(lastVisitDetail: ReviewInformationResponse.IssueSummary) {
        text = if (lastVisitDetail.authorizedGuards == 0 || lastVisitDetail.checkedGuards == 0) {
            "NA"
        } else {
            TextUtils.concat(abs((lastVisitDetail.authorizedGuards - lastVisitDetail.checkedGuards)).toString(), " Guards")
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setIsShortage"])
    fun AppCompatTextView.setIsShortage(lastVisitDetail: ReviewInformationResponse.IssueSummary) {
        text = if (lastVisitDetail.authorizedGuards == 0 || lastVisitDetail.checkedGuards == 0) {
            "Shortage"
        } else {
            if ((lastVisitDetail.authorizedGuards - lastVisitDetail.checkedGuards) < 0) {
                " Surplus"
            } else {
                " Shortage"
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setLastActivityName"])
    fun AppCompatTextView.setLastActivityName(lastVisitDetail: ReviewInformationResponse.IssueSummary) {
        text = if (lastVisitDetail.taskType.isNullOrEmpty()) {
            "NA"
        } else {
            lastVisitDetail.taskType
        }
    }
}