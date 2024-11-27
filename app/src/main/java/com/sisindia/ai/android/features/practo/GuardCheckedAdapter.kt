package com.sisindia.ai.android.features.practo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowGuardCheckBinding
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckViewListeners
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel

/**
 * Created by Ashu Rajput on 13-07-2023
 */
class GuardCheckedAdapter : BaseRecyclerAdapter<CheckedGuardItemModel>() {

    lateinit var listeners: PostCheckViewListeners

    fun initListener(listeners: PostCheckViewListeners) {
        this.listeners = listeners
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowGuardCheckBinding = getViewDataBinding(R.layout.row_guard_check, parent)
                as RowGuardCheckBinding
        view.executePendingBindings()
        return GuardCheckVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: GuardCheckVH = holder as GuardCheckVH
        viewHolder.onBind(getItem(position))
    }

    inner class GuardCheckVH(val view: RowGuardCheckBinding) :
        BaseViewHolder<CheckedGuardItemModel>(view) {
        override fun onBind(item: CheckedGuardItemModel) {
            view.model = item
            view.root.setOnClickListener {
                listeners.onCheckedGuardClick(item)
            }
        }
    }
}