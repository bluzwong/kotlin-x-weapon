package com.github.bluzwong.kotlin_x_weapon.swipeback

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Created by wangzhijie on 2015/10/8.
 */
public class SwipeLeftView(context: Context?) : ViewGroup(context) {
    val imgView = ImageView(context)
    val shadowView = LeftView(context)
    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        imgView.layout(p1, p2, p3, p4)
        shadowView.layout(p1, p2, p3, p4)
    }
    init {
        addView(imgView)
        addView(shadowView)
    }
}