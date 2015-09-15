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
public class SwipeBackActivityHelper(val activity: Activity) {
    var slideView:SlideView? = null
    public fun initSwipeBack() {
        val slidingPaneLayout = SlideView(activity)
        slideView = slidingPaneLayout
        val field_overHandSize = javaClass<SlidingPaneLayout>().getDeclaredField("mOverhangSize")
        field_overHandSize setAccessible true
        field_overHandSize.set(slidingPaneLayout, 0)
        slidingPaneLayout setPanelSlideListener object : SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
            }

            override fun onPanelClosed(panel: View?) {
            }

            override fun onPanelOpened(panel: View?) {
                activity.finish()
                activity.overridePendingTransition(0, R.anim.slide_out_right)
            }
        }
        slidingPaneLayout setSliderFadeColor activity.getResources().getColor(android.R.color.transparent)

        var leftView = View(activity)
        leftView setLayoutParams ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        slidingPaneLayout.addView(leftView, 0)
        var decor = activity.getWindow().getDecorView() as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        // get origin activity view see http://blog.csdn.net/sunny2come/article/details/8899138
        var contentView = decorChild.getChildAt(1) as FrameLayout
        contentView setBackgroundColor activity.getResources().getColor(android.R.color.white)
        // add slidingpanellayout between decor decor view and origin activity view
        slidingPaneLayout.setLayoutParams(contentView.getLayoutParams())
        decorChild removeView contentView
        slidingPaneLayout.addView(contentView, 1)
        decorChild addView slidingPaneLayout
    }
    public fun onSwipeFinish() {
        activity.overridePendingTransition(0, R.anim.slide_out_right_slow);
    }

    public fun addTouchOn(viewGroup:ViewGroup) {
        slideView?.addViewGroup(viewGroup)
    }

    public fun removeTouchOn(viewGroup:ViewGroup) {
        slideView?.removeViewGroup(viewGroup)
    }

    public fun removeAllTouchOn() {
        slideView?.removeAllViewGroup()
    }

    public fun disableFor200Ms() {
        slideView?.setEnabled(false)
        slideView?.postDelayed({ slideView?.setEnabled(true) }, 1000)
    }
}