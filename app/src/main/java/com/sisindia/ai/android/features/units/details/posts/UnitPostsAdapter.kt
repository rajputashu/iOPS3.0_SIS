package com.sisindia.ai.android.features.units.details.posts

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowUnitPostBinding
import com.sisindia.ai.android.uimodels.SitePostModel

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class UnitPostsAdapter : BaseRecyclerAdapter<SitePostModel>() {

    private lateinit var listener: PostsListener

    fun initListener(listener: PostsListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowUnitPostBinding =
            getViewDataBinding(R.layout.row_unit_post, parent) as RowUnitPostBinding
        view.executePendingBindings()
        return UnitPostsVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: UnitPostsVH = holder as UnitPostsVH
        viewHolder.onBind(getItem(position))
    }

    inner class UnitPostsVH(val view: RowUnitPostBinding) : BaseViewHolder<SitePostModel>(view) {
        override fun onBind(item: SitePostModel) {
            view.postsMO = item
            view.root.setOnClickListener { listener.onPostSelected(item.id) }
            view.deletePost.setOnClickListener { listener.onDeletingPost(item.id) }
        }
    }
}