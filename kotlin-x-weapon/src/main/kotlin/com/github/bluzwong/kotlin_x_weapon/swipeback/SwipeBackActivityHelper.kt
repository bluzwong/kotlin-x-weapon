package com.github.bluzwong.kotlin_x_weapon

import android.app.Activity
import android.support.v4.widget.SlidingPaneLayout
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.github.bluzwong.kotlin_x_weapon.R
import com.github.bluzwong.kotlin_x_weapon.swipeback.SlideView

/**
 * Created by Bruce-Home on 2015/9/15.
 */
public class SwipeBackActivityHelper(val activity: Activity): SwipeBackActivitySupport {
    override fun provideActivity(): Activity = activity
}