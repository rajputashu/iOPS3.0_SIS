package com.sisindia.ai.android.features.units.registermap

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowMapRegisterBinding
import com.sisindia.ai.android.features.units.details.posts.PostsListener
import com.sisindia.ai.android.models.register.RegistersInfoMO

/**
 * Created by Ashu Rajput on 10/21/2020.
 */
class MapRegistersAdapter : BaseRecyclerAdapter<RegistersInfoMO>() {

    private lateinit var listener: PostsListener

    fun initListener(listener: PostsListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowMapRegisterBinding = getViewDataBinding(R.layout.row_map_register, parent) as RowMapRegisterBinding
        view.executePendingBindings()
        return MapRegisterVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: MapRegisterVH = holder as MapRegisterVH
        viewHolder.onBind(getItem(position))
    }

    inner class MapRegisterVH(val view: RowMapRegisterBinding) : BaseViewHolder<RegistersInfoMO>(view) {
        override fun onBind(item: RegistersInfoMO) {
            view.model = item
        }
    }
}