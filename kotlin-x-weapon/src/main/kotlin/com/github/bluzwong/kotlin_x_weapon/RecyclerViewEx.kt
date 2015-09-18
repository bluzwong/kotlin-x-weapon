package com.github.bluzwong.kotlin_x_weapon

import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/2.
 */

fun RecyclerView.setOnItemClickListener(listener:(RecyclerView.ViewHolder) -> Unit) {
    addOnItemTouchListener (ClickItemTouchListener(this, listener))
}
fun RecyclerView.setOnItemLongPressListener(listener:(RecyclerView.ViewHolder) -> Unit) {
    addOnItemTouchListener (ClickItemLongPressListener(this, listener))
}

fun RecyclerView.startScheduleLayoutAnimation():Unit {
    val parent = this.getParent() as ViewGroup
    parent.removeView(this)
    parent.addView(this)
    this.scheduleLayoutAnimation()
}

class ClickItemTouchListener(val recycler: RecyclerView, val listener:(RecyclerView.ViewHolder) -> Unit): RecyclerView.OnItemTouchListener, GestureDetector.SimpleOnGestureListener() {
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    val gestureDetector = GestureDetectorCompat(recycler.getContext(), this)
    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {}

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean = gestureDetector.onTouchEvent(e)

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        if (e != null ) {
            val view =recycler.findChildViewUnder(e.getX(), e.getY())
            val position = recycler.getChildAdapterPosition(view)
            val viewHolder = recycler.findViewHolderForAdapterPosition(position)
            if (viewHolder != null) {
                listener(viewHolder)
            }
        }
        return false
    }
}
class ClickItemLongPressListener(val recycler: RecyclerView, val listener:(RecyclerView.ViewHolder) -> Unit): RecyclerView.OnItemTouchListener, GestureDetector.SimpleOnGestureListener() {
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    val gestureDetector = GestureDetectorCompat(recycler.getContext(), this)
    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {}

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean = gestureDetector.onTouchEvent(e)

    override fun onLongPress(e: MotionEvent?) {
        if (e != null) {
            val view =recycler.findChildViewUnder(e.getX(), e.getY())
            val position = recycler.getChildAdapterPosition(view)
            val viewHolder = recycler.findViewHolderForAdapterPosition(position)
            if (viewHolder != null) {
                listener(viewHolder)
            }
        }
    }
}
public object RecyclerHelper{
    public fun setOnRecyclerItemClickListener(rv: RecyclerView, listener:(RecyclerView.ViewHolder) -> Unit) {
        rv.setOnItemClickListener(listener)
    }
}

