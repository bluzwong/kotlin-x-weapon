package com.github.bluzwong.kotlin_x_weapon

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/9.
 */
public class EndViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    public val pbLoading = itemView.findViewById(R.id.loading_progress) as ProgressBar
    public val tvClick = itemView.findViewById(R.id.tv_click_to_load_more) as TextView
}