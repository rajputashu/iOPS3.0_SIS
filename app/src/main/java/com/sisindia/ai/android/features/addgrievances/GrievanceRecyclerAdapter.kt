package com.sisindia.ai.android.features.addgrievances

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.AdapterItemGrievanceBinding
import com.sisindia.ai.android.models.GrievanceModel
import com.sisindia.ai.android.room.entities.GrievanceEntity.GrievanceStatus
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit


class GrievanceRecyclerAdapter : BaseRecyclerAdapter<GrievanceModel>() {

    private var itemListener: GrievanceItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding(R.layout.adapter_item_grievance,
            parent) as AdapterItemGrievanceBinding
        return GrievanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as GrievanceViewHolder

        viewHolder.onBind(getItem(position))

    }

    fun setItemListener(itemListener: GrievanceItemListener?) {
        this.itemListener = itemListener
    }

    internal inner class GrievanceViewHolder(val itemBinding: AdapterItemGrievanceBinding) :
        BaseViewHolder<GrievanceModel>(itemBinding) {

        override fun onBind(item: GrievanceModel) {
            itemBinding.adapterItem = item

            val status = GrievanceStatus.of(item.grievanceStatus)
            if (status != null) {
                when (status) {
                    GrievanceStatus.UNKNOWN, GrievanceStatus.ASSIGNED, GrievanceStatus.NEW, GrievanceStatus.IN_PROGRESS -> {
                        itemBinding.tvPendingText.visibility = VISIBLE
                        itemBinding.ivCompletedIcon.visibility = GONE

                        val today = LocalDateTime.now()
                        val targetDate =
                            LocalDateTime.parse(item.targetDateTime)
                        val days =
                            ChronoUnit.DAYS.between(targetDate, today)

                        if (days > 0) {
                            val suffixStr: String =
                                itemBinding.root.context.getString(R.string.text_grievance_days_pending_for)
                            val prefixStr: String = TextUtils.concat(days.toString(),
                                itemBinding.root.context.getString(R.string.text_grievance_days))
                                .toString()
                            val finalStr: String = prefixStr + suffixStr
                            val sb: Spannable = SpannableString(finalStr)
                            sb.setSpan(StyleSpan(Typeface.BOLD),
                                0,
                                prefixStr.length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            itemBinding.tvPendingText.setText(sb, TextView.BufferType.SPANNABLE)


                        } else {
                            itemBinding.tvPendingText.visibility = GONE
                        }
                    }
                    GrievanceStatus.COMPLETED, GrievanceStatus.CLOSED -> {

                        itemBinding.tvPendingText.visibility = GONE
                        itemBinding.ivCompletedIcon.visibility = VISIBLE
                    }
                }
            }

            itemBinding.root.setOnClickListener {
                if (itemListener != null) {
                    itemListener!!.onGrievanceItemClick(item)
                }
            }
        }

    }
}