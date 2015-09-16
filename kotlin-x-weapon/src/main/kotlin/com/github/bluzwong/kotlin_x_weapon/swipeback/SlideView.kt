package com.github.bluzwong.kotlin_x_weapon.swipeback

import android.content.Context
import android.support.v4.widget.SlidingPaneLayout
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by Bruce-Home on 2015/9/15.
 */
public class SlideView(context: Context?) : SlidingPaneLayout(context) {
    private val views: MutableList<ViewGroup> = ArrayList<ViewGroup>()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.getAction()) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                views forEach {
                    // important!!! make slide swipe not intercepts touch event
                    it.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    public fun addViewGroup(view:ViewGroup) {
        if (views contains view) {
            return
        }
        views add view
    }

    public fun removeViewGroup(view:ViewGroup) {
        if (views contains view) {
            views remove view
        }
    }

    public fun removeAllViewGroup() {
        views.clear()
    }
}