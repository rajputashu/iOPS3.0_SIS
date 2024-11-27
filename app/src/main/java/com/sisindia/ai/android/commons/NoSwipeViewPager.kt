package com.sisindia.ai.android.commons

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by Ashu Rajput on 3/6/2020.
 */
class NoSwipeViewPager : ViewPager {

    private var isPagingEnabled = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
//        return super.onTouchEvent(ev) && this.isPagingEnabled
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        return super.onInterceptTouchEvent(ev) && this.isPagingEnabled
        return false
    }

    fun enableSwipe(isPagingEnabled: Boolean) {
        this.isPagingEnabled = isPagingEnabled
    }

}