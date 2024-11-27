package com.sisindia.ai.android.features.units.details.posts


/**
 * Created by Ashu Rajput on 4/6/2020.
 */
interface PostsListener {
    fun onPostSelected(postId: Int)
    fun onDeletingPost(postId: Int)
}