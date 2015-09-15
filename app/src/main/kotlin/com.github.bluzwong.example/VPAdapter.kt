package com.github.bluzwong.example

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by Bruce-Home on 2015/9/15.
 */
public class VPAdapter(val views:List<View>) : PagerAdapter() {

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return views.count()
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(views get position)
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any? {
        container?.addView(views get position, 0)
        return views get position
    }
}