package com.github.bluzwong.example.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Bruce-Home on 2015/9/5.
 */
public class MainAdapter constructor(val inflater: LayoutInflater) : RecyclerView.Adapter<BaseViewHolder>() {

    public var items: MutableList<MainListItem>? = null

    override fun getItemCount(): Int = items?.count() ?: 0

    override fun getItemViewType(position: Int): Int = items?.get(position)?.itemResId ?: 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder? {
        val view = inflater.inflate(viewType, parent, false)
        return BaseViewHolder(view)
    }

    public var onStopClickListener: ((BaseViewHolder, MainListItem, Int) -> ((View) -> Unit)) ? = null
    public var onLineClickListener: ((BaseViewHolder, MainListItem) -> ((View) -> Unit)) ? = null

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        val item = items?.get(position)
        holder?.tvLineTitle?.setText(item?.lineName)
        holder?.llLine?.setOnClickListener (onLineClickListener?.invoke(holder!!, item!!))
    }
}