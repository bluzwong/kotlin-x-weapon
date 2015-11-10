package com.github.bluzwong.kotlin_x_weapon

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/9.
 */
public class EndlessAdapterWrapper(val recyclerView: RecyclerView, val anyAdapter: RecyclerView.Adapter<*>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    init {
        recyclerView setAdapter this
    }

    private val adapter = anyAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    private val autoLoadMore = R.layout.item_loading
    private var loadingView: View? = null
    private var loadingHolder:EndViewHolder?=null

    public val STATUS_NORMAL: Int = 0
    public val STATUS_IS_LOADING: Int = 1
    public val STATUS_SHOW_CLICK: Int = 2
    public val STATUS_NO_MORE: Int = 3
    public val STATUS_NORMAL_LOADING: Int = 4
    public val STATUS_DISABLE = -1

    private  var currentStatus = STATUS_NORMAL
        set(value) {
            field = if (value in -1..4) value else -1
            if (loadingHolder != null) {
                syncStatus(loadingHolder!!)
            }
        }

    public fun resetStatus() {
        currentStatus = STATUS_NORMAL
    }

    public fun setStatusNoMore() {
        currentStatus = STATUS_NO_MORE
    }

    public fun setEnable(enable:Boolean) {
        //refreshHolder()
        currentStatus = if (enable) STATUS_NORMAL else STATUS_DISABLE
        syncStatus(loadingHolder!!)
    }

    fun syncStatus(holder: EndViewHolder) {
        when (currentStatus) {
            STATUS_IS_LOADING, STATUS_NORMAL_LOADING -> {
                holder.pbLoading.visibility = View.VISIBLE
                holder.tvClick.visibility = View.GONE
            }

            STATUS_SHOW_CLICK -> {
                holder.pbLoading.visibility = View.GONE
                holder.tvClick.visibility = View.VISIBLE
            }

            STATUS_NO_MORE, STATUS_NORMAL, STATUS_DISABLE -> {
                holder.pbLoading.visibility = View.GONE
                holder.tvClick.visibility = View.GONE
            }
        }
    }

    fun shouldShowClickInsteadOfLoading() : Boolean = findLastInScreen() <= adapter.itemCount && findFirstInScreen() == 0

    fun refreshHolder() {
        if((currentStatus == STATUS_NORMAL || currentStatus == STATUS_SHOW_CLICK)) {
            if (shouldShowClickInsteadOfLoading()) {
                currentStatus = STATUS_SHOW_CLICK
            } else {
                currentStatus = STATUS_NORMAL_LOADING
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is EndViewHolder) {
            refreshHolder()
        } else {
            adapter.onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return if (viewType == autoLoadMore) {

            loadingView = recyclerView.context.getLayoutInflater().inflate(viewType, parent, false)
            val holder = EndViewHolder(loadingView!!)
            holder.itemView.setOnClickListener {
                if (currentStatus == STATUS_SHOW_CLICK) {
                    currentStatus = STATUS_IS_LOADING
                    loadingFunc!!.invoke()
                }
            }
            loadingHolder = holder
            holder
        } else adapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return adapter.itemCount + 1
    }

    private fun isLoadingView(position: Int): Boolean
        = position >= adapter.itemCount

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingView(position)) {
            autoLoadMore
        } else adapter.getItemViewType(position)
    }

    private fun findLastInScreen(): Int {
        val manager = recyclerView.layoutManager
        return when (manager) {
            is LinearLayoutManager -> manager.findLastCompletelyVisibleItemPosition()
            is GridLayoutManager -> manager.findLastCompletelyVisibleItemPosition()
            is StaggeredGridLayoutManager -> manager.findLastCompletelyVisibleItemPositions(null).lastIndex
            else -> -1
        }
    }


    private fun findFirstInScreen(): Int {
        val manager = recyclerView.layoutManager
        return when (manager) {
            is LinearLayoutManager -> manager.findFirstCompletelyVisibleItemPosition()
            is GridLayoutManager -> manager.findFirstCompletelyVisibleItemPosition()
            is StaggeredGridLayoutManager -> manager.findFirstCompletelyVisibleItemPositions(null).lastIndex
            else -> -1
        }
    }

    var loadingFunc:(() -> Unit)? = null

    fun shouldShowLoading(remainingSize: Int = 6) : Boolean = findLastInScreen() >= itemCount - 1 - remainingSize

    public fun setOnLoadListener(remainingSize: Int = 6, loadFunc: () -> Unit) {
        loadingFunc = loadFunc
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener () {
            override fun onScrolled(rv: RecyclerView?, dx: Int, dy: Int) {
                if (!(currentStatus == STATUS_NORMAL || currentStatus == STATUS_NORMAL_LOADING)) {
                    return
                }
                if (dy > 0 && shouldShowLoading(remainingSize)) {
                    currentStatus = STATUS_IS_LOADING
                    //                    loadingView?.setVisibility(View.VISIBLE)
                    loadingFunc!!()
                }
            }
        })
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