package com.github.bluzwong.kotlin_x_weapon

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import android.view.animation.LayoutAnimationController

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/18.
 */
public class XRecyclerView : RecyclerView {
    public constructor(context: Context):super(context)
    public constructor(context:Context, attrs:AttributeSet):super(context, attrs)
    public constructor(context:Context, attrs:AttributeSet, defStyle:Int):super(context, attrs, defStyle)

    override fun attachLayoutAnimationParameters(child: View?, params: ViewGroup.LayoutParams, index: Int, count: Int) {
        if (getAdapter() != null) {
            var pms = params.layoutAnimationParameters
            val manager = getLayoutManager()
            when(manager) {
                is GridLayoutManager,is StaggeredGridLayoutManager -> {
                    if (pms == null) {
                        pms = GridLayoutAnimationController.AnimationParameters()
                    }
                    val p = pms as GridLayoutAnimationController.AnimationParameters
                    p.count = count
                    p.index = index
                    p.columnsCount = if (manager is GridLayoutManager) manager.getSpanCount() else {
                        (manager as StaggeredGridLayoutManager).getSpanCount()
                    }
                    p.rowsCount = count / p.columnsCount
                    val invertedIndex = count - 1 - index
                    p.column = p.columnsCount - 1 - (invertedIndex % p.columnsCount)
                    p.row = p.rowsCount - 1 - (invertedIndex / p.rowsCount)
                    params.layoutAnimationParameters = p
                }

                else -> {
                    if (pms == null) {
                        pms = LayoutAnimationController.AnimationParameters()
                    }
                    pms.count = count
                    pms.index = index
                }
            }
        } else super.attachLayoutAnimationParameters(child, params, index, count)
    }
}