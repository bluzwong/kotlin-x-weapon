package com.github.bluzwong.kotlin_x_weapon.swipeback

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import com.github.bluzwong.kotlin_x_weapon.R

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/16.
 */
public class LeftView(context: Context?) : View(context) {
    val leftShadow = resources.getDrawable(R.drawable.shadow_left_code)
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.save()
        val right = width
        val left = right - 100
        val top = 0
        val bot = height
        leftShadow.setBounds(left, top, right, bot)
        leftShadow.draw(canvas)
        canvas.restore()
    }
}