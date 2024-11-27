package com.sisindia.ai.android.features.issues.complaints

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
import com.sisindia.ai.android.databinding.AdapterItemComplaintBinding
import com.sisindia.ai.android.room.entities.ComplaintEntity

import com.sisindia.ai.android.uimodels.ComplaintModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit


class ComplaintRecyclerAdapter : BaseRecyclerAdapter<ComplaintModel>() {

    private var itemListener: ComplaintItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding(R.layout.adapter_item_complaint,
            parent) as AdapterItemComplaintBinding
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ComplaintViewHolder

        viewHolder.onBind(getItem(position))

    }

    fun setItemListener(itemListener: ComplaintItemListener?) {
        this.itemListener = itemListener
    }

    internal inner class ComplaintViewHolder(val itemBinding: AdapterItemComplaintBinding) :
        BaseViewHolder<ComplaintModel>(itemBinding) {

        override fun onBind(item: ComplaintModel) {
            itemBinding.adapterItem = item

            val status = ComplaintEntity.ComplaintStatus.of(item.status)
            if (status != null) {
                when (status) {
                    ComplaintEntity.ComplaintStatus.CREATED, ComplaintEntity.ComplaintStatus.PENDING -> {
                        itemBinding.tvPendingText.visibility = VISIBLE
                        itemBinding.ivCompletedIcon.visibility = GONE

                        val today = LocalDateTime.now()
                        val targetDate =
                            LocalDateTime.parse(item.targetCompletionDate)
                        val days =
                            ChronoUnit.DAYS.between(targetDate, today)

                        if (days > 0) {
                            val suffixStr: String =
                                itemBinding.root.context.getString(R.string.text_issues_days_pending_for)
                            val prefixStr: String = TextUtils.concat(days.toString(),
                                itemBinding.root.context.getString(R.string.text_issues_days))
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
                    ComplaintEntity.ComplaintStatus.CLOSED, ComplaintEntity.ComplaintStatus.COMPLETED -> {
                        itemBinding.tvPendingText.visibility = GONE
                        itemBinding.ivCompletedIcon.visibility = VISIBLE
                    }
                }
            }

            itemBinding.root.setOnClickListener {
                if (itemListener != null) {
                    itemListener!!.onComplaintItemClick(item)
                }
            }
        }

    }
}