package com.sisindia.ai.android.features.taskcheck.strengthcheck

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowStrengthCheckBinding
import com.sisindia.ai.android.room.entities.CacheStrengthEntity
import kotlin.math.sign

/**
 * Created by Ashu Rajput on 4/15/2020.
 */
class StrengthCheckAdapter : BaseRecyclerAdapter<CacheStrengthEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowStrengthCheckBinding = getViewDataBinding(R.layout.row_strength_check, parent) as RowStrengthCheckBinding
        view.executePendingBindings()
        return StrengthViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: StrengthViewHolder = holder as StrengthViewHolder
        viewHolder.onBind(getItem(position))
    }

    inner class StrengthViewHolder(val view: RowStrengthCheckBinding) :
        BaseViewHolder<CacheStrengthEntity>(view) {

        override fun onBind(item: CacheStrengthEntity) {
            view.model = item
            if (item.actualStrength != null) {
                view.currentStrengthET.setText(item.actualStrength.toString())
                item.shortage = if ((item.authorizedStrength - item.actualStrength).sign == -1) 0 else (item.authorizedStrength - item.actualStrength)
                view.tvShortage.visibility = if (item.shortage == 0) GONE else View.VISIBLE
            }

            view.currentStrengthET.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, c: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty())
                        item.actualStrength = s.toString().toInt()
                    else
                        item.actualStrength = 0
                }
            })
        }
    }
}