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

    public var disallowIntercept:Boolean = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.getAction()) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                requestDisallowInterceptTouchEvent(disallowIntercept)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}