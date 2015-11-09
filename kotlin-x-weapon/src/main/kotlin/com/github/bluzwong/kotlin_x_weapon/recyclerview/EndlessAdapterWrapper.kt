package com.github.bluzwong.kotlin_x_weapon

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.github.bluzwong.kotlin_x_weapon.R

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/9.
 */
public class EndlessAdapterWrapper(val recyclerView: RecyclerView, val anyAdapter: Any, val inflater: LayoutInflater, val layoutResId: Int = R.layout.item_loading) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    init {
        recyclerView setAdapter this
    }

    private val adapter = anyAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    private val autoLoadMore = layoutResId
    private var loadingView: View? = null

    private val STATUS_INIT: Int = 0
    private val STATUS_IS_LOADING: Int = 1
    private val STATUS_LOAD_FINISH: Int = 2
    private val STATUS_NO_MORE: Int = 3
    private var loadingStatus: Int = STATUS_INIT

    public fun loadFinish() {
        loadingStatus = STATUS_LOAD_FINISH
        //notifyDataSetChanged()
    }

    public fun noMore() {
        loadingStatus = STATUS_NO_MORE
    }

    public fun reset() {
        loadingStatus = STATUS_INIT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (!isLoadingView(position)) {
            adapter.onBindViewHolder(holder, position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return if (viewType == autoLoadMore) {
            loadingView = inflater.inflate(viewType, parent, false)
            loadingView!!.setVisibility(View.GONE)
            EndViewHolder(loadingView!!)
        } else adapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        if (loadingStatus == STATUS_NO_MORE) {
            return adapter.getItemCount()
        }
        return adapter.getItemCount() + 1
    }

    private fun isLoadingView(position: Int): Boolean
        = adapter.getItemCount() <= position

    override fun getItemViewType(position: Int): Int {
        if (findFirstInScreen() == 0 && findLastInScreen() >= adapter.getItemCount() - 1 && loadingStatus != STATUS_IS_LOADING) {
            loadingView?.setVisibility(View.GONE)
        }
        if (findLastInScreen() >= getItemCount() - 2 && loadingView?.getVisibility() == View.VISIBLE && (loadingStatus == STATUS_LOAD_FINISH || loadingStatus == STATUS_INIT)) {
            loadingStatus = STATUS_IS_LOADING
            loadingFunc!!()
        }
        return if (isLoadingView(position)) {
            autoLoadMore
        } else adapter getItemViewType position
    }

    private fun findLastInScreen(): Int {
        val manager = recyclerView.getLayoutManager()
        if (manager is LinearLayoutManager) {
            return manager.findLastCompletelyVisibleItemPosition()
        }
        if (manager is StaggeredGridLayoutManager) {
            //return manager.findLastCompletelyVisibleItemPositions(null)[0]
            return manager.findLastCompletelyVisibleItemPositions(null).lastIndex
        }
        return -1
    }


    private fun findFirstInScreen(): Int {
        val manager = recyclerView.getLayoutManager()
        if (manager is LinearLayoutManager) {
            return manager.findFirstCompletelyVisibleItemPosition()
        }
        if (manager is StaggeredGridLayoutManager) {
            //return manager.findFirstCompletelyVisibleItemPositions(null)[0]
            return manager.findFirstCompletelyVisibleItemPositions(null).lastIndex
        }
        return -1
    }

    var loadingFunc:(() -> Unit)? = null
    public fun setOnLoadListener(remainingSize: Int = 6, pullRange:Int = 200, loadFunc: () -> Unit) {
        loadingFunc = loadFunc
        recyclerView addOnScrollListener object : RecyclerView.OnScrollListener () {
            override fun onScrolled(rv: RecyclerView?, dx: Int, dy: Int) {
                var lastInScreen = findLastInScreen()
                var adapterCount = getItemCount() - 1
                val needLoad = lastInScreen >= adapterCount - remainingSize
                if (dy > 0 && needLoad && (loadingStatus == STATUS_LOAD_FINISH || loadingStatus == STATUS_INIT)) {
                    loadingStatus = STATUS_IS_LOADING
                    loadingView?.setVisibility(View.VISIBLE)
                    loadFunc()
                }
            }
        }
        var initY = -1f
        recyclerView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.getAction()) {
                MotionEvent.ACTION_DOWN -> initY = motionEvent.getY()
                MotionEvent.ACTION_MOVE -> {
                    val newY = motionEvent.getY()
                    if (initY - newY > pullRange && (loadingStatus == STATUS_LOAD_FINISH || loadingStatus == STATUS_INIT)) {
                        loadingStatus = STATUS_IS_LOADING
                        loadingView?.setVisibility(View.VISIBLE)
                        loadFunc()
                    }
                }
            }
            false
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewAttachedToWindow(holder)
        val params = holder!!.itemView.layoutParams
        if (params != null && params is StaggeredGridLayoutManager.LayoutParams
                && holder.layoutPosition == itemCount - 1) {
            params.isFullSpan = true
        }

        val manager = recyclerView.layoutManager
        when (manager) {
            is GridLayoutManager -> {
                manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (getItemViewType(position) == autoLoadMore) manager.spanCount else 1
                    }
                }
            }
        }
    }
}