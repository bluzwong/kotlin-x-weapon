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
import com.github.bluzwong.kotlin_x_weapon.swipeback.SlideView

/**
 * Created by Bruce-Home on 2015/9/9.
 */
public interface SwipeBackActivitySupport {
    fun provideActivity(): Activity
    protected fun initSwipeBack() {
        val slidingPaneLayout = SlideView(provideActivity())
        val field_overHandSize = javaClass<SlidingPaneLayout>().getDeclaredField("mOverhangSize")
        field_overHandSize setAccessible true
        field_overHandSize.set(slidingPaneLayout, 0)
        slidingPaneLayout setPanelSlideListener object : SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
            }

            override fun onPanelClosed(panel: View?) {
            }

            override fun onPanelOpened(panel: View?) {
                provideActivity().finish()
                provideActivity().overridePendingTransition(0, R.anim.slide_out_right)
            }
        }
        slidingPaneLayout setSliderFadeColor provideActivity().getResources().getColor(android.R.color.transparent)

        var leftView = View(provideActivity())
        leftView setLayoutParams ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        slidingPaneLayout.addView(leftView, 0)
        var decor = provideActivity().getWindow().getDecorView() as ViewGroup


        var decorChild = (decor getChildAt 0) as LinearLayout
        // get origin activity view see http://blog.csdn.net/sunny2come/article/details/8899138
        var contentView = decorChild.getChildAt(1) as FrameLayout
        contentView setBackgroundColor provideActivity().getResources().getColor(android.R.color.white)
        // add slidingpanellayout between decor decor view and origin activity view
        slidingPaneLayout.setLayoutParams(contentView.getLayoutParams())
        decorChild removeView contentView
        slidingPaneLayout.addView(contentView, 1)
        decorChild addView slidingPaneLayout
    }

    protected fun onSwipeFinish() {
        provideActivity().overridePendingTransition(0, R.anim.slide_out_right_slow);
    }

    public fun addTouchOn(viewGroup: ViewGroup) {
        var decor = provideActivity().getWindow().getDecorView() as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        val slideView = decorChild.getChildAt(1) as SlideView
        slideView.addViewGroup(viewGroup)
    }
    public fun removeTouchOn(viewGroup:ViewGroup) {
        var decor = provideActivity().getWindow().getDecorView() as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        val slideView = decorChild.getChildAt(1) as SlideView
        slideView.removeViewGroup(viewGroup)
    }

    public fun removeAllTouchOn() {
        var decor = provideActivity().getWindow().getDecorView() as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        val slideView = decorChild.getChildAt(1) as SlideView
        slideView.removeAllViewGroup()
    }
}