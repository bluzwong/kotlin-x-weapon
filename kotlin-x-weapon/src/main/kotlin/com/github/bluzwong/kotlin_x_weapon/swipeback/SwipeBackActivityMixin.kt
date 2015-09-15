package com.github.bluzwong.kotlin_x_weapon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SlidingPaneLayout
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.github.bluzwong.kotlin_x_weapon.R

/**
 * Created by Bruce-Home on 2015/9/9.
 */
public interface SwipeBackActivityMixin : Activity, SwipeBackActivitySupport {
    override fun provideActivity(): Activity {
        return this
    }
}

public fun startActivitySwipeIn(activity: Activity, cls: Class<*>, extras: Bundle? = null) {
    var intent = Intent(activity, cls)
    if (extras != null) {
        intent putExtras extras
    }
    activity startActivity intent
}